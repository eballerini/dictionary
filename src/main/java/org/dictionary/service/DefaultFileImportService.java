package org.dictionary.service;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.inject.Inject;
import javax.inject.Named;

import org.dictionary.domain.Language;
import org.dictionary.domain.Translation;
import org.dictionary.domain.Word;
import org.dictionary.repository.LanguageRepositoryCustom;
import org.dictionary.repository.TranslationRepository;
import org.dictionary.repository.TranslationRepositoryCustom;
import org.dictionary.repository.WordRepository;
import org.dictionary.repository.WordRepositoryCustom;
import org.dictionary.service.util.FileImportActionType;
import org.dictionary.util.FileImportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class DefaultFileImportService implements FileImportService {

    private final Logger log = LoggerFactory.getLogger(DefaultFileImportService.class);

    @Inject
    private LanguageRepositoryCustom languageRepositoryCustom;

    @Inject
    private WordRepositoryCustom wordRepositoryCustom;

    @Inject
    private WordRepository wordRepository;

    @Inject
    private TranslationRepositoryCustom translationRepositoryCustom;

    @Inject
    private TranslationRepository translationRepository;

    public DefaultFileImportService() {
    }

    // TODO add DB constraint to make language (string) unique
    // TODO add flag to allow to override usage

    @Override
    public Map<FileImportActionType, Integer> importFile(String fileAsStr) {
        if (fileAsStr == null) {
            throw new IllegalArgumentException("file is null");
        }
        StringTokenizer rowTokenizer = new StringTokenizer(fileAsStr, "\n");
        Language[] langs = new Language[2];
        // header
        if (rowTokenizer.hasMoreTokens()) {
            String header = rowTokenizer.nextToken();
            langs = processHeader(header);
        }
        Map<FileImportActionType, Integer> entityCreation = new HashMap<FileImportActionType, Integer>();
        for (FileImportActionType t: FileImportActionType.values()) {
            entityCreation.put(t, 0);
        }
        while (rowTokenizer.hasMoreTokens()) {
            String row = rowTokenizer.nextToken();
            processRow(row, langs, entityCreation);
        }

        // TODO create a record in a new import table to keep track of what's
        // been imported

        return entityCreation;
    }

    private void processRow(String row, Language[] langs, Map<FileImportActionType, Integer> entityCreation) {
        StringTokenizer sk = new StringTokenizer(row, ",");
        String inputWord1 = sk.nextToken().trim();
        String inputWord2 = sk.nextToken().trim();
        String usage = null;
        if (sk.hasMoreTokens()) {
            usage = sk.nextToken().trim();
        }
        Word word1 = loadOrCreateWord(inputWord1, langs[0], entityCreation);
        Word word2 = loadOrCreateWord(inputWord2, langs[1], entityCreation);
        // TODO check that this actually works since we create words in the
        // method if they don't exist
        boolean word1Exists = word1.getId() == null ? false : true;
        boolean word2Exists = word2.getId() == null ? false : true;
        Translation t = null;
        if (word1Exists && word2Exists) {
            // check if translation already exists, both ways
            t = translationRepositoryCustom.findTranslation(word1.getId(), word2.getId());
            if (t == null) {
                t = translationRepositoryCustom.findTranslation(word2.getId(), word1.getId());
            }
        }
        // create translation if it doesn't exist yet
        if (t == null) {
            t = new Translation();
            t.setFromWord(word1);
            t.setToWord(word2);
            t.setUsage(usage);
            log.debug("translation between word {} and {} doesn't exist yet. Creating it", word1, word2);
            translationRepository.save(t);
            increaseCount(FileImportActionType.TRANSLATION_CREATION, entityCreation);
        } else {
            log.debug("translation between word {} and {} already exists");
            increaseCount(FileImportActionType.TRANSLATION_NO_CREATION, entityCreation);
        }

        // TODO update if flag is set
    }

    private Word loadOrCreateWord(String wordAsStr, Language language, Map<FileImportActionType, Integer> entityCreation) {
        Word word = wordRepositoryCustom.loadWord(wordAsStr, language.getId());
        if (word == null) {
            log.debug("word {} doesn't exist yet. Creating it", wordAsStr);
            word = new Word();
            word.setWord(wordAsStr);
            word.setLanguage(language);
            wordRepository.save(word);
            increaseCount(FileImportActionType.WORD_CREATION, entityCreation);
        } else {
            log.debug("word {} already exists", wordAsStr);
            increaseCount(FileImportActionType.WORD_NO_CREATION, entityCreation);
        }
        return word;
    }

    private Language[] processHeader(String header) {
        log.debug("header: {}", header);
        StringTokenizer headerTk = new StringTokenizer(header, ",");
        if (headerTk.countTokens() != 3) {
            throw new FileImportException("header shoud contain 3 values: <language1>, <language2>, usage");
        }
        String language1 = headerTk.nextToken().trim();
        String language2 = headerTk.nextToken().trim();
        String usage = headerTk.nextToken().trim();

        Language[] langs = new Language[2];
        langs[0] = loadLanguage(language1);
        langs[1] = loadLanguage(language2);

        if (!"usage".equals(usage)) {
            throw new FileImportException("header shoud contain 3 values: <language1>, <language2>, usage");
        }
        return langs;
    }

    private Language loadLanguage(String language) {
        Language l = languageRepositoryCustom.findByLanguage(language);
        if (l == null) {
            throw new FileImportException(language + " is not a recognized language");
        }
        return l;
    }
    
    private void increaseCount(FileImportActionType fileImportActionType, Map<FileImportActionType, Integer> actionToCount) {
        Integer num = actionToCount.get(fileImportActionType);
        actionToCount.put(fileImportActionType, num + 1);
    }
}

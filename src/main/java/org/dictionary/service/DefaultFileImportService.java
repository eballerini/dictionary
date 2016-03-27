package org.dictionary.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.dictionary.api.FileImportReportAPI;
import org.dictionary.domain.FileImport;
import org.dictionary.domain.Language;
import org.dictionary.domain.Tag;
import org.dictionary.domain.Translation;
import org.dictionary.domain.Word;
import org.dictionary.repository.FileRepository;
import org.dictionary.repository.LanguageRepositoryCustom;
import org.dictionary.repository.TagRepository;
import org.dictionary.repository.TagRepositoryCustom;
import org.dictionary.repository.TranslationRepository;
import org.dictionary.repository.TranslationRepositoryCustom;
import org.dictionary.repository.WordRepository;
import org.dictionary.repository.WordRepositoryCustom;
import org.dictionary.service.util.FileImportActionType;
import org.dictionary.translator.FileImportTranslator;
import org.dictionary.util.FileImportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Named
public class DefaultFileImportService implements FileImportService {

    private static final String TAGS_DELIMITER = "tags:";
    private static final String CARRIAGE_RETURN = "\n";
    private static final int NUMBER_EXPECTED_TOKENS = 3;
    private static final String COMMA = ",";

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

    @Inject
    private FileImportTranslator fileImportTranslator;

    @Inject
    private FileRepository fileRepository;

    @Inject
    private TagRepository tagRepository;

    @Inject
    private TagRepositoryCustom tagRepositoryCustom;

    public DefaultFileImportService() {
    }

    // TODO add DB constraint to make language (string) unique
    // TODO add flag to allow to override usage

    public DefaultFileImportService(LanguageRepositoryCustom languageRepositoryCustom,
            WordRepositoryCustom wordRepositoryCustom, WordRepository wordRepository,
            TranslationRepositoryCustom translationRepositoryCustom, TranslationRepository translationRepository,
            FileImportTranslator fileImportTranslator, FileRepository fileRepository, TagRepository tagRepository,
            TagRepositoryCustom tagRepositoryCustom) {
        super();
        this.languageRepositoryCustom = languageRepositoryCustom;
        this.wordRepositoryCustom = wordRepositoryCustom;
        this.wordRepository = wordRepository;
        this.translationRepositoryCustom = translationRepositoryCustom;
        this.translationRepository = translationRepository;
        this.fileImportTranslator = fileImportTranslator;
        this.fileRepository = fileRepository;
        this.tagRepository = tagRepository;
        this.tagRepositoryCustom = tagRepositoryCustom;
    }

    @Override
    @Transactional(readOnly = false)
    public Map<FileImportActionType, Integer> importFile(String fileAsStr) {
        checkIfFileIsNullOrEmpty(fileAsStr);
        Map<FileImportActionType, Integer> entityCreation = new HashMap<FileImportActionType, Integer>();
        for (FileImportActionType t: FileImportActionType.values()) {
            entityCreation.put(t, 0);
        }
        StringTokenizer rowTokenizer = new StringTokenizer(fileAsStr, CARRIAGE_RETURN);
        String tagHeader = rowTokenizer.nextToken();
        Set<Tag> tags = new HashSet<>();
        String header;
        if (tagHeader.startsWith(TAGS_DELIMITER)) {
            tags = processTagHeader(tagHeader, entityCreation);
            header = rowTokenizer.nextToken();
        } else {
            header = tagHeader;
        }
        Language[] langs = processHeaderAndReturnUsedLanguages(header);
        while (rowTokenizer.hasMoreTokens()) {
            String row = rowTokenizer.nextToken();
            processRow(row, langs, entityCreation, tags);
        }

        return entityCreation;
    }

    @Override
    @Transactional(readOnly = false)
    public void trackImport(FileImportReportAPI fileImportReportApi) {
        FileImport fileImport = fileImportTranslator.fromAPI(fileImportReportApi);
        fileRepository.save(fileImport);
    }

    private void checkIfFileIsNullOrEmpty(String fileAsStr) {
        if (StringUtils.isEmpty(fileAsStr)) {
            throw new FileImportException("file is null");
        }
    }

    private Set<Tag> processTagHeader(String tagHeader, Map<FileImportActionType, Integer> entityCreation) {
        log.debug("tag header: {}", tagHeader);
        Set<Tag> tags = new HashSet<Tag>();
        String tagsWithoutDelimiter = tagHeader.substring(TAGS_DELIMITER.length(), tagHeader.length());
        StringTokenizer tagTokens = new StringTokenizer(tagsWithoutDelimiter, COMMA);
        while (tagTokens.hasMoreTokens()) {
            String tagName = tagTokens.nextToken().trim();
            Tag tag = tagRepositoryCustom.findByName(tagName);
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                tagRepository.save(tag);
                increaseCount(FileImportActionType.TAG_CREATION, entityCreation);
            }
            tags.add(tag);
        }
        return tags;
    }

    // TODO reduce # of args
    private void processRow(String row, Language[] langs, Map<FileImportActionType, Integer> entityCreation,
            Set<Tag> tags) {
        StringTokenizer sk = new StringTokenizer(row, COMMA);
        String inputWord1 = sk.nextToken().trim();
        String inputWord2 = sk.nextToken().trim();
        String usage = null;
        if (sk.hasMoreTokens()) {
            usage = sk.nextToken().trim();
        }
        Word word1 = loadOrCreateAndSaveWord(inputWord1, langs[0], entityCreation, tags);
        Word word2 = loadOrCreateAndSaveWord(inputWord2, langs[1], entityCreation, tags);
        Translation translation = loadTranslationIfExists(word1, word2);
        if (translation == null) {
            createAndSaveTranslation(entityCreation, usage, word1, word2);
        }

        // TODO update if flag is set
    }

    private void createAndSaveTranslation(Map<FileImportActionType, Integer> entityCreation, String usage, Word word1,
            Word word2) {
        Translation translation = new Translation();
        translation.setFromWord(word1);
        translation.setToWord(word2);
        translation.setUsage(usage);
        log.debug("translation between word {} and {} doesn't exist yet. Creating it", word1, word2);
        translationRepository.save(translation);
        increaseCount(FileImportActionType.TRANSLATION_CREATION, entityCreation);
    }

    private Translation loadTranslationIfExists(Word word1, Word word2) {
        boolean word1Exists = word1.getId() == null ? false : true;
        boolean word2Exists = word2.getId() == null ? false : true;
        Translation translation = null;
        if (word1Exists && word2Exists) {
            // check if translation already exists, both ways
            translation = translationRepositoryCustom.findTranslation(word1.getId(), word2.getId());
            if (translation == null) {
                translation = translationRepositoryCustom.findTranslation(word2.getId(), word1.getId());
            }
        }
        return translation;
    }

    // TODO reduce # of args
    private Word loadOrCreateAndSaveWord(String wordAsStr, Language language,
            Map<FileImportActionType, Integer> entityCreation, Set<Tag> tags) {
        Word word = wordRepositoryCustom.loadWord(wordAsStr, language.getId());
        if (word == null) {
            log.debug("word {} doesn't exist yet. Creating it", wordAsStr);
            word = new Word();
            word.setWord(wordAsStr);
            word.setLanguage(language);
            increaseCount(FileImportActionType.WORD_CREATION, entityCreation);
        }
        word.setTags(tags);
        wordRepository.save(word);

        return word;
    }

    private Language[] processHeaderAndReturnUsedLanguages(String header) {
        log.debug("header: {}", header);
        StringTokenizer headerTk = new StringTokenizer(header, COMMA);
        checkHeaderTokenCount(headerTk);
        String language1 = headerTk.nextToken().trim();
        String language2 = headerTk.nextToken().trim();
        String usage = headerTk.nextToken().trim();
        checkUsage(usage);

        Language[] langs = new Language[2];
        langs[0] = loadLanguage(language1);
        langs[1] = loadLanguage(language2);

        return langs;
    }

    private void checkUsage(String usage) {
        if (!"usage".equals(usage)) {
            throw new FileImportException("header shoud contain 3 values: <language1>, <language2>, usage");
        }
    }

    private void checkHeaderTokenCount(StringTokenizer headerTk) {
        if (headerTk.countTokens() != NUMBER_EXPECTED_TOKENS) {
            throw new FileImportException("header shoud contain 3 values: <language1>, <language2>, usage");
        }
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

package org.dictionary.service;

import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import org.dictionary.api.WordAPI;
import org.dictionary.domain.Word;
import org.dictionary.repository.WordRepositoryCustom;
import org.dictionary.translator.WordTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class DefaultWordService implements WordService {

    private final Logger log = LoggerFactory.getLogger(DefaultWordService.class);

    @Inject
    private WordRepositoryCustom wordRepositoryCustom;

    public DefaultWordService() {
    }

    @Override
    public WordAPI findRandomWord(Long languageId) {

        // find # of words in fromLanguageId
        int numWords = wordRepositoryCustom.countWordsInLanguage(languageId);
        log.debug("num words for language {}: {}", languageId, numWords);

        if (numWords == 0) {
            return null;
        }

        // pick a random one
        Random random = new Random();

        int wordOffset = random.nextInt(numWords);
        Word word = wordRepositoryCustom.loadWord(languageId, wordOffset);

        WordAPI wordAPI = WordTranslator.toAPI(word);

        return wordAPI;
    }

    @Override
    public long findMaxWordId() {
        Long wordId = wordRepositoryCustom.findMaxWordId();
        if (wordId == null) {
            return 0L;
        }
        return wordId;
    }
}

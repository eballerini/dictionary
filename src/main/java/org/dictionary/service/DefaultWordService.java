package org.dictionary.service;

import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import org.dictionary.api.WordAPI;
import org.dictionary.domain.Word;
import org.dictionary.repository.WordRepositoryCustom;
import org.dictionary.repository.search.WordSearchRepository;
import org.dictionary.translator.WordTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class DefaultWordService implements WordService {

    private final Logger log = LoggerFactory.getLogger(DefaultWordService.class);

    @Inject
    private WordRepositoryCustom wordRepositoryCustom;

    @Inject
    private WordSearchRepository wordSearchRepository;

    public DefaultWordService() {
    }

    @Override
    public WordAPI findRandomWord(Long languageId, Long tagId) {

        log.debug("tagId: {}", tagId);
        // find # of words in fromLanguageId
        int numWords = 0;
        // this could be / should be done in a nicer way to avoid all these if
        if (tagId == null) {
            numWords = wordSearchRepository.countByLanguageId(languageId);
            log.debug("[from ES] num words for language {}: {}", languageId, numWords);
        } else {
            numWords = wordSearchRepository.countByLanguageIdAndTagsId(languageId, tagId);
            log.debug("[from ES] num words for language {} with tag {}: {}", languageId, tagId, numWords);
        }

        if (numWords == 0) {
            return null;
        }

        // pick a random one
        Random random = new Random();

        int wordOffset = random.nextInt(numWords);
        Word word;

        if (tagId == null) {
            word = wordRepositoryCustom.loadWord(languageId, wordOffset);
        } else {
            word = wordRepositoryCustom.loadWordForLanguageAndTag(languageId, tagId, wordOffset);
        }

        WordAPI wordAPI = WordTranslator.toAPI(word);

        return wordAPI;
    }

    @Override
    public long findMaxWordId() {
        Long wordId = wordRepositoryCustom.findMaxWordId();
        if (wordId == null) {
            return -1L;
        }
        return wordId;
    }
}

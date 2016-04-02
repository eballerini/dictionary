package org.dictionary.service;

import java.util.Optional;
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
    public WordAPI findRandomWord(Long languageId, Optional<Long> tagId) {

        log.debug("tagId: {}", tagId);
        // find # of words in fromLanguageId
        int numWords = 0;
        // this could be / should be done in a nicer way to avoid all these if
        if (tagId.isPresent()) {
            numWords = wordSearchRepository.countByLanguageIdAndTagsId(languageId, tagId.get());
            log.debug("[from ES] num words for language {} with tag {}: {}", languageId, tagId.get(), numWords);
        } else {
            numWords = wordSearchRepository.countByLanguageId(languageId);
            log.debug("[from ES] num words for language {}: {}", languageId, numWords);
        }

        if (numWords == 0) {
            return null;
        }

        // pick a random one
        Random random = new Random();

        int wordOffset = random.nextInt(numWords);
        Word word;

        if (tagId.isPresent()) {
            word = wordRepositoryCustom.loadWordForLanguageAndTag(languageId, tagId.get(), wordOffset);
        } else {
            word = wordRepositoryCustom.loadWord(languageId, wordOffset);
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

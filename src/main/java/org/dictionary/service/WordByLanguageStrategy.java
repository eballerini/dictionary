package org.dictionary.service;

import org.dictionary.domain.Word;
import org.dictionary.repository.WordRepositoryCustom;
import org.dictionary.repository.search.WordSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordByLanguageStrategy implements WordStrategy {

    private final Logger log = LoggerFactory.getLogger(WordByLanguageStrategy.class);

    private long languageId;

    private WordSearchRepository wordSearchRepository;
    private WordRepositoryCustom wordRepositoryCustom;

    public WordByLanguageStrategy(WordSearchRepository wordSearchRepository, WordRepositoryCustom wordRepositoryCustom,
            long languageId) {
        super();
        this.languageId = languageId;
        this.wordRepositoryCustom = wordRepositoryCustom;
        this.wordSearchRepository = wordSearchRepository;
    }

    @Override
    public int countWords() {
        int numWords = wordSearchRepository.countByLanguageId(languageId);
        log.debug("[from ES] num words for language {}: {}", languageId, numWords);
        return numWords;
    }

    @Override
    public Word loadWord(int wordOffset) {
        return wordRepositoryCustom.loadWord(languageId, wordOffset);
    }
}

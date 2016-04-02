package org.dictionary.service;

import org.dictionary.domain.Word;
import org.dictionary.repository.WordRepositoryCustom;
import org.dictionary.repository.search.WordSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordByLanguageAndTagStrategy implements WordStrategy {

    private final Logger log = LoggerFactory.getLogger(WordByLanguageAndTagStrategy.class);

    private long languageId;
    private long tagId;

    private WordSearchRepository wordSearchRepository;
    private WordRepositoryCustom wordRepositoryCustom;

    public WordByLanguageAndTagStrategy(WordSearchRepository wordSearchRepository,
            WordRepositoryCustom wordRepositoryCustom, long languageId, long tagId) {
        super();
        this.wordRepositoryCustom = wordRepositoryCustom;
        this.wordSearchRepository = wordSearchRepository;
        this.languageId = languageId;
        this.tagId = tagId;
    }

    @Override
    public int countWords() {
        int numWords = wordSearchRepository.countByLanguageIdAndTagsId(languageId, tagId);
        log.debug("[from ES] num words for language {} with tag {}: {}", languageId, tagId, numWords);
        return numWords;
    }

    @Override
    public Word loadWord(int wordOffset) {
        return wordRepositoryCustom.loadWordForLanguageAndTag(languageId, tagId, wordOffset);
    }

}

package org.dictionary.service;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.dictionary.repository.WordRepositoryCustom;
import org.dictionary.repository.search.WordSearchRepository;

@Named
public class WordStrategyFactory {

    @Inject
    private WordSearchRepository wordSearchRepository;

    @Inject
    private WordRepositoryCustom wordRepositoryCustom;

    public WordStrategy createWordStrategy(Long languageId, Optional<Long> tagId) {
        if (tagId.isPresent()) {
            return new WordByLanguageAndTagStrategy(wordSearchRepository, wordRepositoryCustom, languageId, tagId.get());
        } else {
            return new WordByLanguageStrategy(wordSearchRepository, wordRepositoryCustom, languageId);
        }
    }
}

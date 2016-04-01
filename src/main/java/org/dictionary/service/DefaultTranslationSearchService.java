package org.dictionary.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.dictionary.domain.Translation;
import org.dictionary.repository.TranslationRepositoryCustom;
import org.dictionary.repository.search.TranslationSearchRepository;
import org.dictionary.util.DictionaryConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class DefaultTranslationSearchService implements TranslationSearchService {

    private final Logger log = LoggerFactory.getLogger(DefaultTranslationSearchService.class);

    @Inject
    private TranslationSearchRepository translationSearchRepository;

    @Inject
    private TranslationRepositoryCustom translationRepositoryCustom;

    public DefaultTranslationSearchService() {
    }

    @Override
    public void indexTranslations(long startIndex) {

        long id = startIndex;
        int numResults = DictionaryConstants.PAGE_SIZE;

        while (numResults >= DictionaryConstants.PAGE_SIZE) {
            List<Translation> translations = translationRepositoryCustom.load(id, DictionaryConstants.PAGE_SIZE);
            numResults = translations.size();
            if (!translations.isEmpty()) {
                translationSearchRepository.save(translations);
                id = translations.get(numResults - 1).getId() + 1;
                log.debug("indexed {} translations", numResults);
            }
        }
    }
}

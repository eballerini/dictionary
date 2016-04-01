package org.dictionary.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.dictionary.domain.Word;
import org.dictionary.repository.WordRepositoryCustom;
import org.dictionary.repository.search.WordSearchRepository;
import org.dictionary.util.DictionaryConstants;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Named
public class DefaultWordSearchService implements WordSearchService {

    private final Logger log = LoggerFactory.getLogger(DefaultWordSearchService.class);

    @Inject
    private WordSearchRepository wordSearchRepository;

    @Inject
    private WordRepositoryCustom wordRepositoryCustom;

    public DefaultWordSearchService() {
    }

    @Override
    @Transactional(readOnly = false)
    public void indexWords(long startIndex) {

        long id = startIndex;
        int numResults = DictionaryConstants.PAGE_SIZE;

        while (numResults >= DictionaryConstants.PAGE_SIZE) {
            List<Word> words = wordRepositoryCustom.load(id, DictionaryConstants.PAGE_SIZE);
            numResults = words.size();
            if (!words.isEmpty()) {
                // this works but is not efficient - we should try to load the
                // tags along with the words
                words.stream().forEach(w -> Hibernate.initialize(w.getTags()));
                wordSearchRepository.save(words);
                id = words.get(numResults - 1).getId() + 1;
                log.debug("indexed {} words", numResults);
            }
        }
    }

}

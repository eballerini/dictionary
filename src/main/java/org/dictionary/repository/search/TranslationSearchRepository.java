package org.dictionary.repository.search;

import org.dictionary.domain.Translation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Translation entity.
 */
public interface TranslationSearchRepository extends ElasticsearchRepository<Translation, Long> {

    int countByFromWordLanguageId(long languageId);

    int countByToWordLanguageId(long languageId);

    int countByFromWordIdAndToWordId(long fromWordId, long toWordId);
}

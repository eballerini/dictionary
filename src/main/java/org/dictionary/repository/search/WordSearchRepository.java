package org.dictionary.repository.search;

import org.dictionary.domain.Word;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Word entity.
 */
public interface WordSearchRepository extends ElasticsearchRepository<Word, Long> {

    int countByLanguageId(long languageId);

    int countByLanguageIdAndTagsId(long languageId, long tagId);

    int countByIdAndTagsId(long wordId, long tagId);
}

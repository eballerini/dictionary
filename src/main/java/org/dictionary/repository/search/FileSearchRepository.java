package org.dictionary.repository.search;

import org.dictionary.domain.FileImport;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FileImport entity.
 */
public interface FileSearchRepository extends ElasticsearchRepository<FileImport, Long> {
}

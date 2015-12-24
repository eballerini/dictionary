package org.dictionary.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.dictionary.domain.Translation;
import org.dictionary.repository.TranslationRepository;
import org.dictionary.repository.search.TranslationSearchRepository;
import org.dictionary.web.rest.util.HeaderUtil;
import org.dictionary.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Translation.
 */
@RestController
@RequestMapping("/api")
public class TranslationResource {

    private final Logger log = LoggerFactory.getLogger(TranslationResource.class);

    @Inject
    private TranslationRepository translationRepository;

    @Inject
    private TranslationSearchRepository translationSearchRepository;

    /**
     * POST  /translations -> Create a new translation.
     */
    @RequestMapping(value = "/translations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Translation> createTranslation(@RequestBody Translation translation) throws URISyntaxException {
        log.debug("REST request to save Translation : {}", translation);
        if (translation.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new translation cannot already have an ID").body(null);
        }
        Translation result = translationRepository.save(translation);
        translationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/translations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("translation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /translations -> Updates an existing translation.
     */
    @RequestMapping(value = "/translations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Translation> updateTranslation(@RequestBody Translation translation) throws URISyntaxException {
        log.debug("REST request to update Translation : {}", translation);
        if (translation.getId() == null) {
            return createTranslation(translation);
        }
        Translation result = translationRepository.save(translation);
        translationSearchRepository.save(translation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("translation", translation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /translations -> get all the translations.
     */
    @RequestMapping(value = "/translations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Translation>> getAllTranslations(Pageable pageable)
        throws URISyntaxException {
        Page<Translation> page = translationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/translations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /translations/:id -> get the "id" translation.
     */
    @RequestMapping(value = "/translations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Translation> getTranslation(@PathVariable Long id) {
        log.debug("REST request to get Translation : {}", id);
        return Optional.ofNullable(translationRepository.findOne(id))
            .map(translation -> new ResponseEntity<>(
                translation,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /translations/:id -> delete the "id" translation.
     */
    @RequestMapping(value = "/translations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTranslation(@PathVariable Long id) {
        log.debug("REST request to delete Translation : {}", id);
        translationRepository.delete(id);
        translationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("translation", id.toString())).build();
    }

    /**
     * SEARCH  /_search/translations/:query -> search for the translation corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/translations/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Translation> searchTranslations(@PathVariable String query) {
        return StreamSupport
            .stream(translationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

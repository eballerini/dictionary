package org.dictionary.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.dictionary.domain.Language;
import org.dictionary.repository.LanguageRepository;
import org.dictionary.repository.search.LanguageSearchRepository;
import org.dictionary.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Language.
 */
@RestController
@RequestMapping("/api")
public class LanguageResource {

    private final Logger log = LoggerFactory.getLogger(LanguageResource.class);

    @Inject
    private LanguageRepository languageRepository;

    @Inject
    private LanguageSearchRepository languageSearchRepository;

    /**
     * POST  /languages -> Create a new language.
     */
    @RequestMapping(value = "/languages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Language> createLanguage(@Valid @RequestBody Language language) throws URISyntaxException {
        log.debug("REST request to save Language : {}", language);
        if (language.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new language cannot already have an ID").body(null);
        }
        Language result = languageRepository.save(language);
        languageSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/languages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("language", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /languages -> Updates an existing language.
     */
    @RequestMapping(value = "/languages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Language> updateLanguage(@Valid @RequestBody Language language) throws URISyntaxException {
        log.debug("REST request to update Language : {}", language);
        if (language.getId() == null) {
            return createLanguage(language);
        }
        Language result = languageRepository.save(language);
        languageSearchRepository.save(language);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("language", language.getId().toString()))
            .body(result);
    }

    /**
     * GET  /languages -> get all the languages.
     */
    @RequestMapping(value = "/languages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Language> getAllLanguages() {
        log.debug("REST request to get all Languages");
        return languageRepository.findAll();
    }

    /**
     * GET  /languages/:id -> get the "id" language.
     */
    @RequestMapping(value = "/languages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Language> getLanguage(@PathVariable Long id) {
        log.debug("REST request to get Language : {}", id);
        return Optional.ofNullable(languageRepository.findOne(id))
            .map(language -> new ResponseEntity<>(
                language,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /languages/:id -> delete the "id" language.
     */
    @RequestMapping(value = "/languages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id) {
        log.debug("REST request to delete Language : {}", id);
        languageRepository.delete(id);
        languageSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("language", id.toString())).build();
    }

    /**
     * SEARCH  /_search/languages/:query -> search for the language corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/languages/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Language> searchLanguages(@PathVariable String query) {
        return StreamSupport
            .stream(languageSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

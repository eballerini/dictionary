package org.dictionary.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.validation.Valid;

import org.dictionary.api.WordAPI;
import org.dictionary.domain.Word;
import org.dictionary.repository.WordRepository;
import org.dictionary.repository.search.WordSearchRepository;
import org.dictionary.service.WordService;
import org.dictionary.web.rest.util.HeaderUtil;
import org.dictionary.web.rest.util.PaginationUtil;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Word.
 */
@RestController
@RequestMapping("/api")
public class WordResource {

    private final Logger log = LoggerFactory.getLogger(WordResource.class);

    @Inject
    private WordRepository wordRepository;

    @Inject
    private WordSearchRepository wordSearchRepository;

    @Inject
    private WordService wordService;

    /**
     * POST  /words -> Create a new word.
     */
    @RequestMapping(value = "/words",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Word> createWord(@Valid @RequestBody Word word) throws URISyntaxException {
        log.debug("REST request to save Word : {}", word);
        if (word.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new word cannot already have an ID").body(null);
        }
        Word result = wordRepository.save(word);
        wordSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/words/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("word", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /words -> Updates an existing word.
     */
    @RequestMapping(value = "/words",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Word> updateWord(@Valid @RequestBody Word word) throws URISyntaxException {
        log.debug("REST request to update Word : {}", word);
        if (word.getId() == null) {
            return createWord(word);
        }
        Word result = wordRepository.save(word);
        wordSearchRepository.save(word);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("word", word.getId().toString()))
            .body(result);
    }

    /**
     * GET  /words -> get all the words.
     */
    @RequestMapping(value = "/words",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Word>> getAllWords(Pageable pageable)
        throws URISyntaxException {
        Page<Word> page = wordRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/words");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /words/:id -> get the "id" word.
     */
    @RequestMapping(value = "/words/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
	@Transactional(readOnly = true)
    public ResponseEntity<Word> getWord(@PathVariable Long id) {
        log.debug("REST request to get Word : {}", id);
		// TODO move this to a service
		Optional<Word> optionalWord = Optional.ofNullable(wordRepository.findOne(id));
		if (optionalWord.isPresent()) {
			Hibernate.initialize(optionalWord.get().getTags());
		}
		return optionalWord
            .map(word -> new ResponseEntity<>(
                word,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /words/:id -> delete the "id" word.
     */
    @RequestMapping(value = "/words/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWord(@PathVariable Long id) {
        log.debug("REST request to delete Word : {}", id);
        wordRepository.delete(id);
        wordSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("word", id.toString())).build();
    }

    /**
     * SEARCH  /_search/words/:query -> search for the word corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/words/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Word> searchWords(@PathVariable String query) {
        return StreamSupport
            .stream(wordSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
    
    // TODO add doc
    // TODO check if ES offers this out-of-the-box
    // TODO refactor: this should become part of the query that we send to the
    // searchWords method
    @RequestMapping(value = "/_search/words/random/{languageId}", 
            method = RequestMethod.GET, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WordAPI> findRandomWordInLanguage(@PathVariable Long languageId,
            @RequestParam(value = "tagId", required = false) Long tagId) {
        log.debug("languageId: {}", languageId);
        log.debug("tagId: {}", tagId);
        WordAPI wordAPI = wordService.findRandomWord(languageId);

        return Optional.ofNullable(wordAPI)
                .map(word -> new ResponseEntity<>(wordAPI, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
}

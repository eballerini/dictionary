package org.dictionary.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.dictionary.domain.FileImport;
import org.dictionary.repository.FileRepository;
import org.dictionary.repository.search.FileSearchRepository;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing FileImport.
 */
@RestController
@RequestMapping("/api")
public class FileResource {

    private final Logger log = LoggerFactory.getLogger(FileResource.class);

    @Inject
    private FileRepository fileRepository;

    @Inject
    private FileSearchRepository fileSearchRepository;

    /**
     * POST  /files -> Create a new file.
     */
    @RequestMapping(value = "/files",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FileImport> createFile(@Valid @RequestBody FileImport fileImport) throws URISyntaxException {
        log.debug("REST request to save FileImport : {}", fileImport);
        if (fileImport.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new file cannot already have an ID").body(null);
        }
        FileImport result = fileRepository.save(fileImport);
        fileSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("file", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /files -> Updates an existing file.
     */
    @RequestMapping(value = "/files",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FileImport> updateFile(@Valid @RequestBody FileImport fileImport) throws URISyntaxException {
        log.debug("REST request to update FileImport : {}", fileImport);
        if (fileImport.getId() == null) {
            return createFile(fileImport);
        }
        FileImport result = fileRepository.save(fileImport);
        fileSearchRepository.save(fileImport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("file", fileImport.getId().toString()))
            .body(result);
    }

    /**
     * GET  /files -> get all the files.
     */
    @RequestMapping(value = "/files",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FileImport>> getAllFiles(Pageable pageable)
        throws URISyntaxException {
        Page<FileImport> page = fileRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/files");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /files/:id -> get the "id" file.
     */
    @RequestMapping(value = "/files/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FileImport> getFile(@PathVariable Long id) {
        log.debug("REST request to get FileImport : {}", id);
        return Optional.ofNullable(fileRepository.findOne(id))
            .map(file -> new ResponseEntity<>(
                file,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /files/:id -> delete the "id" file.
     */
    @RequestMapping(value = "/files/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        log.debug("REST request to delete FileImport : {}", id);
        fileRepository.delete(id);
        fileSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("file", id.toString())).build();
    }

    /**
     * SEARCH  /_search/files/:query -> search for the file corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/files/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FileImport> searchFiles(@PathVariable String query) {
        return StreamSupport
            .stream(fileSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

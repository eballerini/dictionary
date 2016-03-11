package org.dictionary.web.rest;

import javax.inject.Inject;

import org.dictionary.security.AuthoritiesConstants;
import org.dictionary.service.TranslationSearchService;
import org.dictionary.service.WordSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping("/api")
public class IndexResource {

    private final Logger log = LoggerFactory.getLogger(IndexResource.class);

    @Inject
    private WordSearchService wordSearchService;

    @Inject
    private TranslationSearchService translationSearchService;

    @RequestMapping(value = "/indices/build", method = RequestMethod.POST)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Boolean> buildIndex() {
        try {
            wordSearchService.indexWords(0);
            translationSearchService.indexTranslations(0);
            // TODO file import + ??
        } catch (Exception e) {
            log.error("could not index all documents: ", e);
            return ResponseEntity.ok().body(Boolean.FALSE);
        }

        log.debug("re-indexing done");
        return ResponseEntity.ok().body(Boolean.TRUE);
    }
}

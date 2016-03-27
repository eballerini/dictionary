package org.dictionary.web.rest;

import java.util.Map;

import javax.inject.Inject;

import org.dictionary.api.FileImportReportAPI;
import org.dictionary.service.FileImportService;
import org.dictionary.service.TranslationSearchService;
import org.dictionary.service.TranslationService;
import org.dictionary.service.WordSearchService;
import org.dictionary.service.WordService;
import org.dictionary.service.util.FileImportActionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping("/api")
public class FileImportResource {

    private final Logger log = LoggerFactory.getLogger(FileImportResource.class);

    @Inject
    private FileImportService fileImportService;

    @Inject
    private WordService wordService;

    @Inject
    private WordSearchService wordSearchService;

    @Inject
    private TranslationService translationService;

    @Inject
    private TranslationSearchService translationSearchService;

    public FileImportResource() {
    }

    @RequestMapping(value = "/files/import", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<FileImportReportAPI> importFile(@RequestParam("file") MultipartFile file,
            @RequestParam("name") String name) {
        // getting the name through file.getName() used to work but not anymore.
        // Not sure what happened
        FileImportReportAPI report = new FileImportReportAPI(false);
        report.setFilename(name);
        log.debug("REST request to import file with name {}", name);

        try {
            checkContentType(file.getContentType());
            String fileAsStr = new String(file.getBytes());
            
            long maxWordId = wordService.findMaxWordId();
            long maxTranslationId = translationService.findMaxTranslationId();

            Map<FileImportActionType, Integer> entityCreation = fileImportService.importFile(fileAsStr);
            updateReport(name, report, entityCreation);

            indexWordsAndTranslations(maxWordId, maxTranslationId);
        } catch (Exception e) {
            log.error("You failed to upload " + name + " => ", e);
            throw new RuntimeException("File could not be uploaded: " + e.getMessage(), e);
        } finally {
            trackImport(report);
        }

        return ResponseEntity.ok().body(report);
    }

    private void checkContentType(String contentType) {
        // TODO would be better to use something like MediaType.PLAIN_TEXT_UTF_8
        if (!"text/plain".equals(contentType)) {
            log.error("Cannot upload file. Content type is {}", contentType);
            throw new IllegalArgumentException("only text files are allowed");
        }
    }

    private void updateReport(String fileName, FileImportReportAPI report,
            Map<FileImportActionType, Integer> entityCreation) {
        Integer numWordsCreated = entityCreation.get(FileImportActionType.WORD_CREATION);
        report.setNumWordsCreated(numWordsCreated);
        Integer numTranslationsCreated = entityCreation.get(FileImportActionType.TRANSLATION_CREATION);
        report.setNumTranslationsCreated(numTranslationsCreated);
        Integer numTagsCreated = entityCreation.get(FileImportActionType.TAG_CREATION);
        report.setNumTagsCreated(numTagsCreated);
        report.setSuccess(true);
        log.debug("You successfully uploaded {}", fileName);
        log.debug("report: {}", report);
    }

    private void indexWordsAndTranslations(long maxWordId, long maxTranslationId) {
        wordSearchService.indexWords(maxWordId + 1);
        translationSearchService.indexTranslations(maxTranslationId + 1);
    }

    private void trackImport(FileImportReportAPI report) {
        try {
            fileImportService.trackImport(report);
        } catch (Exception e) {
            log.error("report could not be tracked: " + report, e);
        }
    }
}

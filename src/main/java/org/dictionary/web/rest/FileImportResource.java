package org.dictionary.web.rest;

import java.util.Map;

import javax.inject.Inject;

import org.dictionary.api.FileImportReportAPI;
import org.dictionary.service.FileImportService;
import org.dictionary.service.util.FileImportActionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

    public FileImportResource() {
    }

    @RequestMapping(value = "/files",
            method = RequestMethod.POST)
    @Timed
    @Transactional(readOnly = false)
    public ResponseEntity<FileImportReportAPI> importFile(@RequestParam("file") MultipartFile file) {
        FileImportReportAPI report = new FileImportReportAPI(false);
        if (file == null) {
            throw new RuntimeException("cannot import a null file");
        }
        String name = file.getName();
        log.debug("REST request to import file with name {}", name);
        String contentType = file.getContentType();
        // TODO would be better to use something like MediaType.PLAIN_TEXT_UTF_8
        if (!"text/plain".equals(contentType)) {
            log.error("Cannot upload file. Content type is {}", contentType);
            report.setMessage("only text files are allowed");
            return ResponseEntity.ok().body(report);
        }
            
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String fileAsStr = new String(bytes);
                Map<FileImportActionType, Integer> entityCreation = fileImportService.importFile(fileAsStr);
                Integer numWordsCreated = entityCreation.get(FileImportActionType.WORD_CREATION);
                report.setNumWordsCreated(numWordsCreated);
                Integer numWordsNotCreated = entityCreation.get(FileImportActionType.WORD_NO_CREATION);
                report.setNumWordsNotCreated(numWordsNotCreated);
                Integer numTranslationsCreated = entityCreation.get(FileImportActionType.TRANSLATION_CREATION);
                report.setNumTranslationsCreated(numTranslationsCreated);
                Integer numTranslationsNotCreated = entityCreation.get(FileImportActionType.TRANSLATION_NO_CREATION);
                report.setNumTranslationsNotCreated(numTranslationsNotCreated);
                report.setSuccess(true);
                log.debug("You successfully uploaded {}", name);
                log.debug("report: {}", report);
            } catch (Exception e) {
                log.error("You failed to upload " + name + " => ", e);
                // ideally we'd set the report's message when the exception is
                // of type FileImportException
                throw new RuntimeException("file could not be uploaded", e);
            }
        } else {
            report.setMessage("file is empty");
            log.error("You failed to upload " + name + " because the file was empty.");
        }

        return ResponseEntity.ok().body(report);
    }

}

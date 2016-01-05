package org.dictionary.translator;

import java.time.LocalDate;

import javax.inject.Named;

import org.dictionary.api.FileImportReportAPI;
import org.dictionary.domain.FileImport;

@Named
public class FileImportTranslator {

    public FileImportTranslator() {
    }

    public FileImport fromAPI(FileImportReportAPI api) {
        FileImport fileImport = new FileImport();
        fileImport.setDate(LocalDate.now());
        fileImport.setName(api.getFilename());
        if (api.isSuccess()) {
            fileImport.setStatus("success");
            fileImport.setComments("num words created: " + api.getNumWordsCreated() + ", num existing words: "
                    + api.getNumWordsNotCreated() + ", num translations created: " + api.getNumTranslationsCreated()
                    + ", num existing translations: " + api.getNumTranslationsNotCreated());
        } else {
            fileImport.setStatus("failure");
        }
        return fileImport;
    }

}

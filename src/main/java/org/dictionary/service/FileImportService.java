package org.dictionary.service;

import java.util.Map;

import org.dictionary.service.util.FileImportActionType;

public interface FileImportService {

    Map<FileImportActionType, Integer> importFile(String fileAsStr);

}
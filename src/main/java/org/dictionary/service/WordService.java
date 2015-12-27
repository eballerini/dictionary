package org.dictionary.service;

import org.dictionary.api.WordAPI;

public interface WordService {

    WordAPI findRandomWord(Long languageId);

}
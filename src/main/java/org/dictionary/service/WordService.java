package org.dictionary.service;

import java.util.Optional;

import org.dictionary.api.WordAPI;

public interface WordService {

    Optional<WordAPI> findRandomWord(Long languageId, Optional<Long> tagId);

    long findMaxWordId();
}
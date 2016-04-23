package org.dictionary.service;

import java.util.Optional;
import java.util.Set;

import org.dictionary.api.WordAPI;

public interface WordService {

    Optional<WordAPI> findRandomWord(Long languageId, Optional<Long> tagId);

    Optional<WordAPI> findRandomWord(Long languageId, Optional<Long> tagId, Set<Long> notInIds);

    long findMaxWordId();
}
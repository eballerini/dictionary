package org.dictionary.service;

import java.util.List;

import org.dictionary.api.TranslationAPI;

public interface TranslationService {

    List<TranslationAPI> findTranslations(Long wordId, Long toLanguageId);

    long findMaxTranslationId();
}
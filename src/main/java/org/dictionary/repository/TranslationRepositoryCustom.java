package org.dictionary.repository;

import java.util.List;

import org.dictionary.domain.Translation;

public interface TranslationRepositoryCustom {

    List<Translation> findTranslationsFromWord(Long wordId, Long toLanguageId);

    List<Translation> findTranslationsToWord(Long wordId, Long toLanguageId);

    Translation findTranslation(Long wordId1, Long wordId2);

    Long findMaxTranslationId();

    List<Translation> load(long id, int pageSize);
}

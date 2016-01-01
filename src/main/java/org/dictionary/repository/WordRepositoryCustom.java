package org.dictionary.repository;

import org.dictionary.domain.Word;

public interface WordRepositoryCustom {

    // there may be a way to do this with JPA directly
    int countWordsInLanguage(long id);

    Word loadWord(long id, int offset);

    Word loadWord(String word, Long languageId);
}

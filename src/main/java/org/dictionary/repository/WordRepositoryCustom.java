package org.dictionary.repository;

import java.util.List;

import org.dictionary.domain.Word;

public interface WordRepositoryCustom {

    // there may be a way to do this with JPA directly
    int countWordsInLanguage(long languageId);

    Word loadWord(long languageId, int offset);

    Word loadWord(String word, Long languageId);

    List<Word> load(long indexStart, int pageSize);

    Long findMaxWordId();
}

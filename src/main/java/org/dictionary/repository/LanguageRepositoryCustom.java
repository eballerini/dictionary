package org.dictionary.repository;

import org.dictionary.domain.Language;

public interface LanguageRepositoryCustom {

    Language findByLanguage(String language);

}
package org.dictionary.translator;

import javax.inject.Named;

import org.dictionary.api.LanguageAPI;
import org.dictionary.domain.Language;

@Deprecated
@Named
public class LanguageTranslator {

    public LanguageAPI toAPI(Language language) {
        if (language == null) {
            return null;
        }
        LanguageAPI languageAPI = new LanguageAPI();
        languageAPI.setId(language.getId());
        languageAPI.setLanguage(language.getLanguage());

        return languageAPI;
    }
}

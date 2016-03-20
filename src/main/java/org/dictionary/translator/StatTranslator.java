package org.dictionary.translator;

import javax.inject.Named;

import org.dictionary.api.LanguageAPI;
import org.dictionary.api.StatAPI;

@Named
public class StatTranslator {

    public StatAPI toAPI(LanguageAPI language, int numWords, int numTranslations) {
        StatAPI stat = new StatAPI();
        stat.setLanguage(language);
        stat.setNumWords(numWords);
        stat.setNumTranslations(numTranslations);
        return stat;
    }
}

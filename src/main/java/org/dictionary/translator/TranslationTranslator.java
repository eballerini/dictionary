package org.dictionary.translator;

import org.dictionary.api.TranslationAPI;
import org.dictionary.api.WordAPI;
import org.dictionary.domain.Translation;

public class TranslationTranslator {

    @Deprecated
    public static TranslationAPI toAPI(WordAPI toWordAPI, Translation translation) {
        if (translation == null) {
            return null;
        }
        TranslationAPI translationAPI = new TranslationAPI();
        translationAPI.setId(translation.getId());
        // TODO need to add fromWord
        translationAPI.setToWord(toWordAPI);
        translationAPI.setPriority(translation.getPriority());
        translationAPI.setUsage(translation.getUsage());
        return translationAPI;
    }

    public static TranslationAPI toAPI(WordAPI fromWordAPI, WordAPI toWordAPI, Translation translation) {
        if (translation == null) {
            return null;
        }
        TranslationAPI translationAPI = new TranslationAPI();
        translationAPI.setId(translation.getId());
        translationAPI.setToWord(toWordAPI);
        translationAPI.setFromWord(fromWordAPI);
        translationAPI.setPriority(translation.getPriority());
        translationAPI.setUsage(translation.getUsage());
        return translationAPI;
    }
}

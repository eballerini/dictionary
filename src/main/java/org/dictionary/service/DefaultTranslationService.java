package org.dictionary.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.dictionary.api.TranslationAPI;
import org.dictionary.api.WordAPI;
import org.dictionary.domain.Translation;
import org.dictionary.repository.TranslationRepositoryCustom;
import org.dictionary.translator.TranslationTranslator;
import org.dictionary.translator.WordTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class DefaultTranslationService implements TranslationService {

    private final Logger log = LoggerFactory.getLogger(DefaultTranslationService.class);

    @Inject
    private TranslationRepositoryCustom translationRepositoryCustom;

    public DefaultTranslationService() {
    }

    @Override
    public List<TranslationAPI> findTranslations(Long wordId, Long toLanguageId) {
        if (wordId == null) {
            throw new IllegalArgumentException("wordId cannot be null");
        }
        if (toLanguageId == null) {
            throw new IllegalArgumentException("toLanguageId cannot be null");
        }
        List<Translation> translationsFromWord = translationRepositoryCustom.findTranslationsFromWord(wordId,
                toLanguageId);
        log.debug("# of translations from word: {}", translationsFromWord.size());

        List<Translation> translationsToWord = translationRepositoryCustom.findTranslationsToWord(wordId,
                toLanguageId);
        log.debug("# of translations to word: {}", translationsToWord.size());

        List<TranslationAPI> translationsAPI = new ArrayList<TranslationAPI>();
        for (Translation translation: translationsFromWord) {
            WordAPI wordAPI = WordTranslator.toAPI(translation.getToWord());
            TranslationAPI translationAPI = TranslationTranslator.toAPI(wordAPI, translation);
            translationsAPI.add(translationAPI);
        }

        for (Translation translation: translationsToWord) {
            WordAPI wordAPI = WordTranslator.toAPI(translation.getFromWord());
            TranslationAPI translationAPI = TranslationTranslator.toAPI(wordAPI, translation);
            translationsAPI.add(translationAPI);
        }
        return translationsAPI;
    }


}

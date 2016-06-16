package org.dictionary.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.dictionary.api.DashboardAPI;
import org.dictionary.api.DashboardStatAPI;
import org.dictionary.api.LanguageAPI;
import org.dictionary.api.QuizResultAPI;
import org.dictionary.api.StatAPI;
import org.dictionary.domain.Language;
import org.dictionary.domain.QuizResult;
import org.dictionary.domain.User;
import org.dictionary.repository.DefaultQuizResultRepositoryCustom;
import org.dictionary.repository.LanguageRepository;
import org.dictionary.repository.UserRepository;
import org.dictionary.repository.search.TranslationSearchRepository;
import org.dictionary.repository.search.WordSearchRepository;
import org.dictionary.translator.LanguageTranslator;
import org.dictionary.translator.StatTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class DefaultDashboardService implements DashboardService {

    private final Logger log = LoggerFactory.getLogger(DefaultDashboardService.class);

    @Inject
    private WordSearchRepository wordSearchRepository;

    @Inject
    private TranslationSearchRepository translationSearchRepository;

    @Inject
    private LanguageRepository languageRepository;

    @Inject
    private LanguageTranslator languageTranslator;

    @Inject
    private StatTranslator statTranslator;

    @Inject
    private DefaultQuizResultRepositoryCustom quizResultRepositoryCustom;

    @Inject
    private UserRepository userRepository;

    @Override
    public DashboardAPI getDashboard() {

        // This is only ok as long as we have very few languages
        List<Language> allLanguages = languageRepository.findAll();

        log.debug("there are {} languages", allLanguages.size());

        DashboardAPI dashboard = new DashboardAPI();
        List<StatAPI> stats = getStats(allLanguages);
        dashboard.setStats(stats);


        return dashboard;
    }

    @Override
    public DashboardStatAPI getStats(String login, LocalDate startDate, LocalDate endDate) {
        Optional<User> user = userRepository.findOneByLogin(login);
        if (!user.isPresent()) {
            throw new RuntimeException("could not find user with login " + login);
        }
        List<QuizResult> quizResults = quizResultRepositoryCustom.find(user.get().getId(), startDate, endDate);
        List<QuizResultAPI> quizResultsAPI = new ArrayList<QuizResultAPI>();
        // TODO try to use stream.map
        quizResults.forEach(q -> quizResultsAPI.add(q.toAPI()));
        DashboardStatAPI dashboardStat = new DashboardStatAPI();
        dashboardStat.setQuizResults(quizResultsAPI);
        return dashboardStat;
    }

    private List<StatAPI> getStats(List<Language> allLanguages) {
        List<StatAPI> stats = new ArrayList<StatAPI>();
        for (Language language: allLanguages) {
            LanguageAPI languageAPI = languageTranslator.toAPI(language);
            StatAPI stat = statTranslator.toAPI(languageAPI, countWords(languageAPI.getId()),
                    countTranslations(languageAPI.getId()));
            stats.add(stat);
        }
        return stats;
    }

    private int countWords(long languageId) {
        int numWordsForLanguage = wordSearchRepository.countByLanguageId(languageId);
        log.debug("num of words for language with id {}: {}", languageId, numWordsForLanguage);
        return numWordsForLanguage;
    }

    private int countTranslations(long languageId) {
        int numTranslationsFromWordForLanguage = translationSearchRepository.countByFromWordLanguageId(languageId);
        int numTranslationsToWordForLanguage = translationSearchRepository.countByToWordLanguageId(languageId);
        int numTranslationsForLanguage = numTranslationsFromWordForLanguage + numTranslationsToWordForLanguage;
        log.debug("num of translation for language with id {}: {}", languageId, numTranslationsForLanguage);
        return numTranslationsForLanguage;
    }
}

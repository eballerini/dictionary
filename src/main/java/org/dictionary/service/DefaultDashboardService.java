package org.dictionary.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.mutable.MutableLong;
import org.dictionary.api.DashboardAPI;
import org.dictionary.api.LanguageAPI;
import org.dictionary.api.StatAPI;
import org.dictionary.domain.Language;
import org.dictionary.domain.Word;
import org.dictionary.repository.LanguageRepository;
import org.dictionary.repository.search.WordSearchRepository;
import org.dictionary.translator.LanguageTranslator;
import org.dictionary.translator.StatTranslator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class DefaultDashboardService implements DashboardService {

    private final Logger log = LoggerFactory.getLogger(DefaultDashboardService.class);

    @Inject
    private WordSearchRepository wordSearchRepository;

    @Inject
    private LanguageRepository languageRepository;

    @Inject
    private LanguageTranslator languageTranslator;

    @Inject
    private StatTranslator statTranslator;

    @Override
    public DashboardAPI getDashboard() {

        // This is only ok as long as we have very few languages
        List<Language> allLanguages = languageRepository.findAll();

        log.debug("there are {} languages", allLanguages.size());

        DashboardAPI dashboard = new DashboardAPI();
        List<StatAPI> stats = new ArrayList<StatAPI>();

        for (Language language: allLanguages) {
            LanguageAPI languageAPI = languageTranslator.toAPI(language);
            StatAPI stat = statTranslator.toAPI(languageAPI, countWords(languageAPI.getId()));
            stats.add(stat);
        }

        dashboard.setStats(stats);

        return dashboard;
    }

    private long countWords(long languageId) {

        // TODO quick and dirty eeww
        // not efficient - need to include the aggregate into the query
        QueryBuilder searchQuery = QueryBuilders.matchQuery("language.id", languageId);
        log.debug("query: {}", searchQuery);
        Iterable<Word> words = wordSearchRepository.search(searchQuery);
        final MutableLong numWords = new MutableLong(0);
        words.forEach(word -> numWords.increment());

        return numWords.longValue();
    }
}

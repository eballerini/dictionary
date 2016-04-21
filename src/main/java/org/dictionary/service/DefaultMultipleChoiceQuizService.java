package org.dictionary.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.dictionary.api.MultipleChoiceQuestionAPI;
import org.dictionary.api.MultipleChoiceQuizAPI;
import org.dictionary.api.TranslationAPI;
import org.dictionary.api.WordAPI;
import org.dictionary.repository.search.TranslationSearchRepository;
import org.dictionary.web.rest.errors.CustomParameterizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class DefaultMultipleChoiceQuizService implements MultipleChoiceQuizService {

    private final Logger log = LoggerFactory.getLogger(DefaultMultipleChoiceQuizService.class);

    private static final int DEFAULT_NUM_WORDS = 2;
    public static final int NUM_CHOICES = 5;

    @Inject
    private WordService wordService;

    @Inject
    private TranslationService translationService;

    // TODO maybe move this into TranslationService
    @Inject
    private TranslationSearchRepository translationSearchRepository;

    @Inject
    private WordStrategyFactory wordStrategyFactory;

    @Override
    public MultipleChoiceQuizAPI getMultipleChoiceQuizAPI(long fromLanguageId, long toLanguageId, Optional<Long> tagId,
            Optional<Integer> selectedNumWords) {

        int numWords = selectedNumWords.orElse(DEFAULT_NUM_WORDS);

        // http://localhost:3000/api/quiz/languages/1/to/2?selectedNumWords=3

        MultipleChoiceQuizAPI quiz = new MultipleChoiceQuizAPI();
        List<MultipleChoiceQuestionAPI> questions = new ArrayList<MultipleChoiceQuestionAPI>();

        checkTotalNumWords(fromLanguageId, tagId, numWords);

        for (int i = 0; i < numWords; i++) {
            MultipleChoiceQuestionAPI question = new MultipleChoiceQuestionAPI();
            Optional<WordAPI> word = wordService.findRandomWord(fromLanguageId, tagId);
            // TODO make sure that there is actually a word
            if (word.isPresent()) {
                question.setWord(word.get());
                Set<WordAPI> answers = new HashSet<WordAPI>();

                // TODO fix TranslationTranslator.toAPI(toWordAPI, translation)
                List<TranslationAPI> translations = translationService.findTranslations(word.get().getId(),
                        toLanguageId);
                answers.add(getWordFromTranslations(translations, word.get()));
                // TODO make sure there are translations
                // TODO make sure that the other words are not potential
                // translations
                List<WordAPI> otherWords = new ArrayList<WordAPI>();
                for (int j = 0; j < NUM_CHOICES - 1; j++) {
                    // TODO make sure all the other words are unique
                    Optional<WordAPI> otherWord = wordService.findRandomWord(toLanguageId, Optional.empty());
                    if (otherWord.isPresent()) {
                        otherWords.add(otherWord.get());
                    } else {
                        // TODO drop this current word
                    }
                }
                answers.addAll(otherWords);
                question.setAnswers(answers);

            }
            questions.add(question);
        }
        quiz.setQuestions(questions);

        return quiz;
    }

    @Override
    public void setCorrectAnswers(MultipleChoiceQuizAPI quiz) {
        for (MultipleChoiceQuestionAPI question: quiz.getQuestions()) {
            log.debug("for the word {}", question.getWord());
            for (WordAPI possibleAnswer: question.getAnswers()) {
                int translationsCount = countTranslations(question.getWord(), possibleAnswer);
                if (translationsCount > 0) {
                    question.setCorrectAnswerWordId(possibleAnswer.getId());
                    break;
                }
            }
        }
    }

    private int countTranslations(WordAPI fromWord, WordAPI toWord) {
        long fromWordId = fromWord.getId();
        long toWordId = toWord.getId();
        int translationsCount = translationSearchRepository.countByFromWordIdAndToWordId(fromWordId, toWordId);
        if (translationsCount == 0) {
            translationsCount = translationSearchRepository.countByFromWordIdAndToWordId(toWordId, fromWordId);
        }
        log.debug("translations count between word {} and word {}: {}", fromWordId, toWordId, translationsCount);
        return translationsCount;
    }

    private WordAPI getWordFromTranslations(List<TranslationAPI> translations, WordAPI word) {
        // TODO pick a random translation
        for (TranslationAPI translation: translations) {
            // should be able to just use equals between words
            if (word.getWord().equals(translation.getFromWord().getWord())) {
                return translation.getToWord();
            } else {
                return translation.getFromWord();
            }
        }
        return null;
    }

    private void checkTotalNumWords(long fromLanguageId, Optional<Long> tagId, int numWords) {
        WordStrategy wordStrategy = wordStrategyFactory.createWordStrategy(fromLanguageId, tagId);
        int totalNumWords = wordStrategy.countWords();
        log.debug("from language id {} with tag {}, there are {} words", fromLanguageId, tagId, totalNumWords);
    
        if (totalNumWords < numWords) {
            throw new CustomParameterizedException(
                    "Could not create quiz because there aren't enough words with this tags");
        }
    }
}

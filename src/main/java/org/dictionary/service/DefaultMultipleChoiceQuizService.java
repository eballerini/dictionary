package org.dictionary.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.dictionary.api.MultipleChoiceQuestionAPI;
import org.dictionary.api.MultipleChoiceQuizAPI;
import org.dictionary.api.TranslationAPI;
import org.dictionary.api.WordAPI;
import org.dictionary.repository.search.TranslationSearchRepository;
import org.dictionary.repository.search.WordSearchRepository;
import org.dictionary.web.rest.errors.CustomParameterizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

@Named
public class DefaultMultipleChoiceQuizService implements MultipleChoiceQuizService {

    private final Logger log = LoggerFactory.getLogger(DefaultMultipleChoiceQuizService.class);

    private static final int DEFAULT_NUM_WORDS = 2;
    public static final String DEFAULT_TOTAL_NUM_CHOICES = "5";

    @Value(value = DEFAULT_TOTAL_NUM_CHOICES)
    private int totalNumOtherChoices;

    @Inject
    private WordService wordService;

    @Inject
    private WordSearchRepository wordSearchRepository;

    @Inject
    private TranslationService translationService;

    // TODO maybe move this into TranslationService
    @Inject
    private TranslationSearchRepository translationSearchRepository;

    @Inject
    private WordStrategyFactory wordStrategyFactory;

    // TODO remove
    // http://localhost:3000/api/quiz/languages/1/to/2?selectedNumWords=3
    @Override
    public MultipleChoiceQuizAPI getQuiz(long fromLanguageId, long toLanguageId, Optional<Long> tagId,
            Optional<Integer> selectedNumWords) {

        int numWords = selectedNumWords.orElse(DEFAULT_NUM_WORDS);

        checkTotalNumWords(fromLanguageId, tagId, numWords);

        MultipleChoiceQuizAPI quiz = new MultipleChoiceQuizAPI();
        List<MultipleChoiceQuestionAPI> questions = new ArrayList<MultipleChoiceQuestionAPI>();
        Set<Long> wordIds = new HashSet<Long>();

        for (int i = 0; i < numWords; i++) {
            MultipleChoiceQuestionAPI question = new MultipleChoiceQuestionAPI();
            boolean foundWordWithTranslations = false;
            WordAPI word;
            List<TranslationAPI> translations;
            do {
                word = findRandomWord(fromLanguageId, tagId, wordIds);
                wordIds.add(word.getId());
                translations = translationService.findTranslations(word.getId(), toLanguageId);
                if (!translations.isEmpty()) {
                    foundWordWithTranslations = true;
                }
            } while (!foundWordWithTranslations);
            question.setWord(word);
            Set<WordAPI> answers = new HashSet<WordAPI>();
            WordAPI translatedWord = getWordFromTranslations(translations, word, tagId);
            answers.add(translatedWord);

            // TODO make sure that the other words are not potential
            // translations
            // TODO make sure that if there is a tag, other answers do have this
            // tag as well
            List<WordAPI> otherWords = new ArrayList<WordAPI>();
            Set<Long> notInAnswerIds = new HashSet<Long>();
            notInAnswerIds.add(translatedWord.getId());
            for (int j = 0; j < totalNumOtherChoices - 1; j++) {
                WordAPI otherWord = findRandomWord(toLanguageId, tagId, notInAnswerIds);
                otherWords.add(otherWord);
                notInAnswerIds.add(otherWord.getId());
            }
            answers.addAll(otherWords);
            question.setAnswers(answers);

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

    void setTotalNumOtherChoices(int totalNumOtherChoices) {
        this.totalNumOtherChoices = totalNumOtherChoices;
    }

    private WordAPI findRandomWord(long fromLanguageId, Optional<Long> tagId, Set<Long> wordIds) {
        Optional<WordAPI> word = wordService.findRandomWord(fromLanguageId, tagId, wordIds);
        if (!word.isPresent()) {
            new CustomParameterizedException("Could not create quiz");
        }
        return word.get();
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

    private WordAPI getWordFromTranslations(List<TranslationAPI> translations, WordAPI word, Optional<Long> tagId) {
        if (tagId.isPresent()) {
            if (translations.size() == 1) {
                TranslationAPI uniqueTranslation = translations.get(0);
                return getOtherWord(word, uniqueTranslation);
            } else {
                for (TranslationAPI translation: translations) {
                    WordAPI otherWord = getOtherWord(word, translation);
                    int numWordsWithTag = wordSearchRepository.countByIdAndTagsId(otherWord.getId(), tagId.get());
                    if (numWordsWithTag > 0) {
                        return otherWord;
                    }
                }
            }
        } else {
            return pickRandomTranslationWord(translations, word);
        }
        return null;
    }

    private WordAPI pickRandomTranslationWord(List<TranslationAPI> translations, WordAPI word) {
        Random random = new Random();
        int numTranslations = translations.size();
        TranslationAPI randomTranslation = translations.get(random.nextInt(numTranslations));
        return getOtherWord(word, randomTranslation);
    }

    private WordAPI getOtherWord(WordAPI word, TranslationAPI uniqueTranslation) {
        if (word.equals(uniqueTranslation.getFromWord())) {
            return uniqueTranslation.getToWord();
        } else {
            return uniqueTranslation.getFromWord();
        }
    }

    private void checkTotalNumWords(long fromLanguageId, Optional<Long> tagId, int numWords) {
        WordStrategy wordStrategy = wordStrategyFactory.createWordStrategy(fromLanguageId, tagId);
        int totalNumWords = wordStrategy.countWords();
        log.debug("from language id {} with tag {}, there are {} words", fromLanguageId, tagId, totalNumWords);
    
        if (totalNumWords < numWords) {
            throw new CustomParameterizedException(
                    "Could not create quiz because there aren't enough words in this language with this tag");
        }
    }
}

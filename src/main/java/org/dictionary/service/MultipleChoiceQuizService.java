package org.dictionary.service;

import java.util.Optional;

import org.dictionary.api.MultipleChoiceQuizAPI;

public interface MultipleChoiceQuizService {

    MultipleChoiceQuizAPI getQuiz(long fromLanguageId, long toLanguageId, Optional<Long> tagId,
            Optional<Integer> selectedNumWords);

    void setCorrectAnswers(MultipleChoiceQuizAPI quiz);

    long trackQuizResult(long fromLanguageId, long toLanguageId, Optional<Long> tagId,
            Optional<Integer> selectedNumWords);

    void setNumCorrectAnswersInQuizResult(MultipleChoiceQuizAPI quiz);
}
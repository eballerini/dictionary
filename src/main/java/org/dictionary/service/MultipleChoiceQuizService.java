package org.dictionary.service;

import java.util.Optional;

import org.dictionary.api.MultipleChoiceQuizAPI;

public interface MultipleChoiceQuizService {

    MultipleChoiceQuizAPI getMultipleChoiceQuizAPI(long fromLanguageId, long toLanguageId, Optional<Long> tagId,
            Optional<Integer> selectedNumWords);

    void validateAndSetCorrectAnswer(MultipleChoiceQuizAPI quiz);
}
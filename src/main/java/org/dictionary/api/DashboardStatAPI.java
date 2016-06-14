package org.dictionary.api;

import java.util.List;

public class DashboardStatAPI {

    private List<QuizResultAPI> quizResults;

    public List<QuizResultAPI> getQuizResults() {
        return quizResults;
    }

    public void setQuizResults(List<QuizResultAPI> quizResults) {
        this.quizResults = quizResults;
    }

}

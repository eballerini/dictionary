package org.dictionary.api;

import java.util.List;

public class MultipleChoiceQuizAPI {

    private long quizResultId;
    private List<MultipleChoiceQuestionAPI> questions;

    public List<MultipleChoiceQuestionAPI> getQuestions() {
        return questions;
    }

    public void setQuestions(List<MultipleChoiceQuestionAPI> questions) {
        this.questions = questions;
    }

    public long getQuizResultId() {
        return quizResultId;
    }

    public void setQuizResultId(long quizResultId) {
        this.quizResultId = quizResultId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((questions == null) ? 0 : questions.hashCode());
        result = prime * result + (int) (quizResultId ^ (quizResultId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MultipleChoiceQuizAPI other = (MultipleChoiceQuizAPI) obj;
        if (questions == null) {
            if (other.questions != null)
                return false;
        } else if (!questions.equals(other.questions))
            return false;
        if (quizResultId != other.quizResultId)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "MultipleChoiceQuizAPI [quizResultId=" + quizResultId + ", questions=" + questions + "]";
    }
    
}

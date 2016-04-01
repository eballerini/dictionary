package org.dictionary.api;

import java.util.List;

public class MultipleChoiceQuizAPI {

    private List<MultipleChoiceQuestionAPI> questions;

    public List<MultipleChoiceQuestionAPI> getQuestions() {
        return questions;
    }

    public void setQuestions(List<MultipleChoiceQuestionAPI> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "MultipleChoiceQuizAPI [questions=" + questions + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((questions == null) ? 0 : questions.hashCode());
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
        return true;
    }
    
}

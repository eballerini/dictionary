package org.dictionary.api;

import java.util.Set;

public class MultipleChoiceQuestionAPI {

    private WordAPI word;
    private Set<WordAPI> answers;
    private Long answerWordId;
    private Long correctAnswerWordId;

    public WordAPI getWord() {
        return word;
    }

    public Long getAnswerWordId() {
        return answerWordId;
    }

    public void setAnswerWordId(Long answerWordId) {
        this.answerWordId = answerWordId;
    }

    public void setWord(WordAPI word) {
        this.word = word;
    }

    public Set<WordAPI> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<WordAPI> answers) {
        this.answers = answers;
    }

    public Long getCorrectAnswerWordId() {
        return correctAnswerWordId;
    }

    public void setCorrectAnswerWordId(Long correctAnswerWordId) {
        this.correctAnswerWordId = correctAnswerWordId;
    }

    public boolean isAnswerCorrect() {
        if (correctAnswerWordId == null) {
            return false;
        }
        return correctAnswerWordId.equals(answerWordId);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((answerWordId == null) ? 0 : answerWordId.hashCode());
        result = prime * result + ((answers == null) ? 0 : answers.hashCode());
        result = prime * result + ((correctAnswerWordId == null) ? 0 : correctAnswerWordId.hashCode());
        result = prime * result + ((word == null) ? 0 : word.hashCode());
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
        MultipleChoiceQuestionAPI other = (MultipleChoiceQuestionAPI) obj;
        if (answerWordId == null) {
            if (other.answerWordId != null)
                return false;
        } else if (!answerWordId.equals(other.answerWordId))
            return false;
        if (answers == null) {
            if (other.answers != null)
                return false;
        } else if (!answers.equals(other.answers))
            return false;
        if (correctAnswerWordId == null) {
            if (other.correctAnswerWordId != null)
                return false;
        } else if (!correctAnswerWordId.equals(other.correctAnswerWordId))
            return false;
        if (word == null) {
            if (other.word != null)
                return false;
        } else if (!word.equals(other.word))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "MultipleChoiceQuestionAPI [word=" + word + ", answers=" + answers + ", answerWordId=" + answerWordId
                + ", correctAnswerWordId=" + correctAnswerWordId + "]";
    }

}

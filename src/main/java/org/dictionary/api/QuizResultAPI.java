package org.dictionary.api;

import java.time.LocalDate;

public class QuizResultAPI {

    private Long id;
    private int numWords;
    private int numCorrectAnswers;
    private LanguageAPI fromLanguage;
    private LanguageAPI toLanguage;
    private TagAPI tagAPI;
    private LocalDate date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumWords() {
        return numWords;
    }

    public void setNumWords(int numWords) {
        this.numWords = numWords;
    }

    public int getNumCorrectAnswers() {
        return numCorrectAnswers;
    }

    public void setNumCorrectAnswers(int numCorrectAnswers) {
        this.numCorrectAnswers = numCorrectAnswers;
    }

    public LanguageAPI getFromLanguage() {
        return fromLanguage;
    }

    public void setFromLanguage(LanguageAPI fromLanguage) {
        this.fromLanguage = fromLanguage;
    }

    public LanguageAPI getToLanguage() {
        return toLanguage;
    }

    public void setToLanguage(LanguageAPI toLanguage) {
        this.toLanguage = toLanguage;
    }

    public TagAPI getTag() {
        return tagAPI;
    }

    public void setTag(TagAPI tagAPI) {
        this.tagAPI = tagAPI;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "QuizResultAPI [id=" + id + ", numWords=" + numWords + ", numCorrectAnswers=" + numCorrectAnswers
                + ", fromLanguage=" + fromLanguage + ", toLanguage=" + toLanguage + ", tagAPI=" + tagAPI + ", date="
                + date + "]";
    }


}

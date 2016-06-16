package org.dictionary.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.dictionary.api.QuizResultAPI;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "quiz_result")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class QuizResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "num_words", nullable = false)
    private int numWords;

    @Column(name = "num_correct_answers", nullable = false)
    private int numCorrectAnswers;

    @ManyToOne()
    @JoinColumn(name = "from_language_id", nullable = false)
    private Language fromLanguage;

    @ManyToOne
    @JoinColumn(name = "to_language_id", nullable = false)
    private Language toLanguage;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    public Language getFromLanguage() {
        return fromLanguage;
    }

    public void setFromLanguage(Language fromLanguage) {
        this.fromLanguage = fromLanguage;
    }

    public Language getToLanguage() {
        return toLanguage;
    }

    public void setToLanguage(Language toLanguage) {
        this.toLanguage = toLanguage;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "QuizResult [id=" + id + ", numWords=" + numWords + ", numCorrectAnswers=" + numCorrectAnswers
                + ", fromLanguage=" + fromLanguage + ", toLanguage=" + toLanguage + ", tag=" + tag + ", date=" + date
                + ", user=" + user + "]";
    }

    public QuizResultAPI toAPI() {
        QuizResultAPI quizResultAPI = new QuizResultAPI();
        quizResultAPI.setDate(date);
        quizResultAPI.setFromLanguage(fromLanguage.toAPI());
        quizResultAPI.setToLanguage(toLanguage.toAPI());
        quizResultAPI.setId(id);
        quizResultAPI.setNumCorrectAnswers(numCorrectAnswers);
        quizResultAPI.setNumWords(numWords);
        if (tag != null) {
            quizResultAPI.setTag(tag.toAPI());
        }
        return quizResultAPI;
    }

}

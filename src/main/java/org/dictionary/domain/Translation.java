package org.dictionary.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A Translation.
 */
@Entity
@Table(name = "translation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "translation")
public class Translation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "usage")
    private String usage;

    @Column(name = "priority")
    private Integer priority;

    // TODO rename field, getter and setter
    @ManyToOne
    private Word from_word;

    // TODO rename field, getter and setter
    @ManyToOne
    private Word to_word;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Word getFrom_word() {
        return from_word;
    }

    public void setFrom_word(Word word) {
        this.from_word = word;
    }

    public Word getTo_word() {
        return to_word;
    }

    public void setTo_word(Word word) {
        this.to_word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Translation translation = (Translation) o;

        if ( ! Objects.equals(id, translation.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Translation{" +
            "id=" + id +
            ", usage='" + usage + "'" +
            ", priority='" + priority + "'" +
            '}';
    }
}

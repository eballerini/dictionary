package org.dictionary.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A Word.
 */
@Entity
@Table(name = "word")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "word")
public class Word implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "word", nullable = false)
    private String word;

    @Column(name = "original_word")
    private String original_word;

    @NotNull
    @ManyToOne
    private Language language;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "WORD_TAG", joinColumns = {
			@JoinColumn(name = "WORD_ID", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "TAG_ID", 
					nullable = false, updatable = false) })
	@Cascade({ CascadeType.SAVE_UPDATE })
	private Set<Tag> tags = new HashSet<Tag>(0);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getOriginal_word() {
        return original_word;
    }

    public void setOriginal_word(String original_word) {
        this.original_word = original_word;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }


	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((original_word == null) ? 0 : original_word.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
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
		Word other = (Word) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		}
		else if (!language.equals(other.language))
			return false;
		if (original_word == null) {
			if (other.original_word != null)
				return false;
		}
		else if (!original_word.equals(other.original_word))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		}
		else if (!tags.equals(other.tags))
			return false;
		if (word == null) {
			if (other.word != null)
				return false;
		}
		else if (!word.equals(other.word))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Word [id=" + id + ", word=" + word + ", original_word=" + original_word + ", language=" + language + "]";
	}
}

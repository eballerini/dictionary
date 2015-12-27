package org.dictionary.api;

public class TranslationAPI {

    private Long id;
    private WordAPI fromWord;
    private WordAPI toWord;
    private Integer priority;
    private String usage;

    public TranslationAPI() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WordAPI getFromWord() {
        return fromWord;
    }

    public void setFromWord(WordAPI fromWord) {
        this.fromWord = fromWord;
    }

    public WordAPI getToWord() {
        return toWord;
    }

    public void setToWord(WordAPI toWord) {
        this.toWord = toWord;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fromWord == null) ? 0 : fromWord.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((priority == null) ? 0 : priority.hashCode());
        result = prime * result + ((toWord == null) ? 0 : toWord.hashCode());
        result = prime * result + ((usage == null) ? 0 : usage.hashCode());
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
        TranslationAPI other = (TranslationAPI) obj;
        if (fromWord == null) {
            if (other.fromWord != null)
                return false;
        } else if (!fromWord.equals(other.fromWord))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (priority == null) {
            if (other.priority != null)
                return false;
        } else if (!priority.equals(other.priority))
            return false;
        if (toWord == null) {
            if (other.toWord != null)
                return false;
        } else if (!toWord.equals(other.toWord))
            return false;
        if (usage == null) {
            if (other.usage != null)
                return false;
        } else if (!usage.equals(other.usage))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TranslationAPI [id=" + id + ", fromWord=" + fromWord + ", toWord=" + toWord + ", priority=" + priority
                + ", usage=" + usage + "]";
    }


}

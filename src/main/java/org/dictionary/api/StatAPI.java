package org.dictionary.api;

public class StatAPI {

    private LanguageAPI language;
    private long numWords;

    public StatAPI() {
        super();
    }

    public LanguageAPI getLanguage() {
        return language;
    }

    public void setLanguage(LanguageAPI language) {
        this.language = language;
    }

    public long getNumWords() {
        return numWords;
    }

    public void setNumWords(long numWords) {
        this.numWords = numWords;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((language == null) ? 0 : language.hashCode());
        result = prime * result + (int) (numWords ^ (numWords >>> 32));
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
        StatAPI other = (StatAPI) obj;
        if (language == null) {
            if (other.language != null)
                return false;
        } else if (!language.equals(other.language))
            return false;
        if (numWords != other.numWords)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "StatAPI [language=" + language + ", numWords=" + numWords + "]";
    }

}

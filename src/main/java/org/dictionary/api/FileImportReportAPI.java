package org.dictionary.api;

public class FileImportReportAPI {

    private boolean success;
    private String filename;
    private int numWordsCreated;
    private int numTranslationsCreated;
    private int numWordsNotCreated;
    private int numTranslationsNotCreated;

    private String message; // used for errors

    public FileImportReportAPI(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getNumWordsCreated() {
        return numWordsCreated;
    }

    public void setNumWordsCreated(int numWords) {
        this.numWordsCreated = numWords;
    }

    public int getNumTranslationsCreated() {
        return numTranslationsCreated;
    }

    public void setNumTranslationsCreated(int numTranslations) {
        this.numTranslationsCreated = numTranslations;
    }

    public int getNumWordsNotCreated() {
        return numWordsNotCreated;
    }

    public void setNumWordsNotCreated(int numWordsNotCreated) {
        this.numWordsNotCreated = numWordsNotCreated;
    }

    public int getNumTranslationsNotCreated() {
        return numTranslationsNotCreated;
    }

    public void setNumTranslationsNotCreated(int numTranslationsNotCreated) {
        this.numTranslationsNotCreated = numTranslationsNotCreated;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((filename == null) ? 0 : filename.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + numTranslationsCreated;
        result = prime * result + numTranslationsNotCreated;
        result = prime * result + numWordsCreated;
        result = prime * result + numWordsNotCreated;
        result = prime * result + (success ? 1231 : 1237);
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
        FileImportReportAPI other = (FileImportReportAPI) obj;
        if (filename == null) {
            if (other.filename != null)
                return false;
        } else if (!filename.equals(other.filename))
            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (numTranslationsCreated != other.numTranslationsCreated)
            return false;
        if (numTranslationsNotCreated != other.numTranslationsNotCreated)
            return false;
        if (numWordsCreated != other.numWordsCreated)
            return false;
        if (numWordsNotCreated != other.numWordsNotCreated)
            return false;
        if (success != other.success)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "FileImportReportAPI [success=" + success + ", filename=" + filename + ", numWordsCreated="
                + numWordsCreated + ", numTranslationsCreated=" + numTranslationsCreated + ", numWordsNotCreated="
                + numWordsNotCreated + ", numTranslationsNotCreated=" + numTranslationsNotCreated + ", message="
                + message + "]";
    }



}

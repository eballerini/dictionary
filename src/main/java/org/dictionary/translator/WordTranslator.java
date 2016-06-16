package org.dictionary.translator;

import org.dictionary.api.WordAPI;
import org.dictionary.domain.Word;

public class WordTranslator {

    // TODO move this to the Word class
    public static WordAPI toAPI(Word word) {
        if (word == null) {
            return null;
        }
        WordAPI wordAPI = new WordAPI();
        wordAPI.setId(word.getId());
        wordAPI.setWord(word.getWord());

        return wordAPI;
    }
}

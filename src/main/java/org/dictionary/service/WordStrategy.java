package org.dictionary.service;

import org.dictionary.domain.Word;

/**
 * Defines a strategy that allows to count the number of words and load one with
 * an offset. Name could be a little better maybe?
 */
public interface WordStrategy {

    int countWords();

    Word loadWord(int wordOffset);

}
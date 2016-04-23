package org.dictionary.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.dictionary.api.WordAPI;
import org.dictionary.domain.Word;
import org.dictionary.repository.WordRepositoryCustom;
import org.dictionary.translator.WordTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class DefaultWordService implements WordService {

    private final Logger log = LoggerFactory.getLogger(DefaultWordService.class);

    @Inject
    private WordRepositoryCustom wordRepositoryCustom;

    @Inject
    private WordStrategyFactory wordStrategyFactory;

    public DefaultWordService() {
    }

    @Override
    public Optional<WordAPI> findRandomWord(Long languageId, Optional<Long> tagId) {

        return findRandomWord(languageId, tagId, new HashSet<Long>());
    }

    @Override
    public Optional<WordAPI> findRandomWord(Long languageId, Optional<Long> tagId, Set<Long> notInIds) {

        log.debug("tagId: {}", tagId);
        WordStrategy wordStrategy = wordStrategyFactory.createWordStrategy(languageId, tagId);
        int numWords = wordStrategy.countWords();

        if (numWords == 0) {
            return Optional.empty();
        }

        return findRandomWord(wordStrategy, numWords, notInIds);
    }

    @Override
    public long findMaxWordId() {
        Long wordId = wordRepositoryCustom.findMaxWordId();
        if (wordId == null) {
            return -1L;
        }
        return wordId;
    }

    private Optional<WordAPI> findRandomWord(WordStrategy wordStrategy, int numWords, Set<Long> notInIds) {
        Word word = null;
        boolean foundWordNotExcluded = false;
        int numWordsNotToPick = 0;
        Set<Integer> offsetAlreadySelected = new HashSet<>();
        do {
            int wordOffset = pickRandomOffset(numWords, offsetAlreadySelected);
            word = wordStrategy.loadWord(wordOffset);
            if (notInIds.contains(word.getId())) {
                numWordsNotToPick++;
                word = null;
            } else {
                foundWordNotExcluded = true;
            }
        } while (!foundWordNotExcluded && numWordsNotToPick < numWords);
        return createWordAPIIfExist(word);
    }

    private int pickRandomOffset(int numWords, Set<Integer> offsetAlreadySelected) {
        Random random = new Random();
        boolean foundNewOffset = false;
        int wordOffset = 0;
        do {
            wordOffset = random.nextInt(numWords);
            if (!offsetAlreadySelected.contains(wordOffset)) {
                foundNewOffset = true;
                offsetAlreadySelected.add(wordOffset);
            }
        } while (!foundNewOffset);
        return wordOffset;
    }

    private Optional<WordAPI> createWordAPIIfExist(Word word) {
        WordAPI wordAPI = null;
        if (word != null) {
            wordAPI = WordTranslator.toAPI(word);
        }
        return Optional.ofNullable(wordAPI);
    }
}

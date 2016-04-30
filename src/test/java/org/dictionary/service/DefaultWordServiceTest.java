package org.dictionary.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.dictionary.api.WordAPI;
import org.dictionary.domain.Word;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class DefaultWordServiceTest {

    private WordService service;
    private WordStrategyFactory wordStrategyFactory = mock(WordStrategyFactory.class);
    private Long languageId = 1L;
    private Optional<Long> tagId = Optional.empty();

    @Before
    public void init() {
        service = new DefaultWordService();
        ReflectionTestUtils.setField(service, "wordStrategyFactory", wordStrategyFactory);
    }

    @Test
    public void testFindRandomWord() {
        initTest();

        Optional<WordAPI> word1 = service.findRandomWord(languageId, tagId, Collections.emptySet());

        Assert.assertTrue(word1.isPresent());
        Set<Long> notInIds = new HashSet<Long>();
        notInIds.add(word1.get().getId());

        Optional<WordAPI> word2 = service.findRandomWord(languageId, tagId, notInIds);
        Assert.assertTrue(word2.isPresent());

        Assert.assertNotEquals(word1, word2);

        notInIds.add(word2.get().getId());

        Optional<WordAPI> word3 = service.findRandomWord(languageId, tagId, notInIds);
        Assert.assertFalse(word3.isPresent());
    }

    @Test
    public void testCountWordIsZero() throws Exception {

        WordStrategy wordStrategy = mock(WordStrategy.class);
        when(wordStrategyFactory.createWordStrategy(languageId, tagId)).thenReturn(wordStrategy);
        when(wordStrategy.countWords()).thenReturn(0);

        Optional<WordAPI> word = service.findRandomWord(languageId, tagId, Collections.emptySet());
        Assert.assertFalse(word.isPresent());
    }

    private void initTest() {
        WordStrategy wordStrategy = mock(WordStrategy.class);
        when(wordStrategyFactory.createWordStrategy(languageId, tagId)).thenReturn(wordStrategy);
        when(wordStrategy.countWords()).thenReturn(2);
        Word word01 = mock(Word.class);
        when(word01.getId()).thenReturn(11L);
        when(wordStrategy.loadWord(0)).thenReturn(word01);
        Word word02 = mock(Word.class);
        when(word02.getId()).thenReturn(21L);
        when(wordStrategy.loadWord(1)).thenReturn(word02);
    }
}

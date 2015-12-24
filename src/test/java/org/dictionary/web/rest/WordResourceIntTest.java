package org.dictionary.web.rest;

import org.dictionary.Application;
import org.dictionary.domain.Word;
import org.dictionary.repository.WordRepository;
import org.dictionary.repository.search.WordSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the WordResource REST controller.
 *
 * @see WordResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class WordResourceIntTest {

    private static final String DEFAULT_WORD = "AAAAA";
    private static final String UPDATED_WORD = "BBBBB";
    private static final String DEFAULT_ORIGINAL_WORD = "AAAAA";
    private static final String UPDATED_ORIGINAL_WORD = "BBBBB";

    @Inject
    private WordRepository wordRepository;

    @Inject
    private WordSearchRepository wordSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWordMockMvc;

    private Word word;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WordResource wordResource = new WordResource();
        ReflectionTestUtils.setField(wordResource, "wordRepository", wordRepository);
        ReflectionTestUtils.setField(wordResource, "wordSearchRepository", wordSearchRepository);
        this.restWordMockMvc = MockMvcBuilders.standaloneSetup(wordResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        word = new Word();
        word.setWord(DEFAULT_WORD);
        word.setOriginal_word(DEFAULT_ORIGINAL_WORD);
    }

    @Test
    @Transactional
    public void createWord() throws Exception {
        int databaseSizeBeforeCreate = wordRepository.findAll().size();

        // Create the Word

        restWordMockMvc.perform(post("/api/words")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(word)))
                .andExpect(status().isCreated());

        // Validate the Word in the database
        List<Word> words = wordRepository.findAll();
        assertThat(words).hasSize(databaseSizeBeforeCreate + 1);
        Word testWord = words.get(words.size() - 1);
        assertThat(testWord.getWord()).isEqualTo(DEFAULT_WORD);
        assertThat(testWord.getOriginal_word()).isEqualTo(DEFAULT_ORIGINAL_WORD);
    }

    @Test
    @Transactional
    public void checkWordIsRequired() throws Exception {
        int databaseSizeBeforeTest = wordRepository.findAll().size();
        // set the field null
        word.setWord(null);

        // Create the Word, which fails.

        restWordMockMvc.perform(post("/api/words")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(word)))
                .andExpect(status().isBadRequest());

        List<Word> words = wordRepository.findAll();
        assertThat(words).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWords() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the words
        restWordMockMvc.perform(get("/api/words"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(word.getId().intValue())))
                .andExpect(jsonPath("$.[*].word").value(hasItem(DEFAULT_WORD.toString())))
                .andExpect(jsonPath("$.[*].original_word").value(hasItem(DEFAULT_ORIGINAL_WORD.toString())));
    }

    @Test
    @Transactional
    public void getWord() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get the word
        restWordMockMvc.perform(get("/api/words/{id}", word.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(word.getId().intValue()))
            .andExpect(jsonPath("$.word").value(DEFAULT_WORD.toString()))
            .andExpect(jsonPath("$.original_word").value(DEFAULT_ORIGINAL_WORD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWord() throws Exception {
        // Get the word
        restWordMockMvc.perform(get("/api/words/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWord() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

		int databaseSizeBeforeUpdate = wordRepository.findAll().size();

        // Update the word
        word.setWord(UPDATED_WORD);
        word.setOriginal_word(UPDATED_ORIGINAL_WORD);

        restWordMockMvc.perform(put("/api/words")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(word)))
                .andExpect(status().isOk());

        // Validate the Word in the database
        List<Word> words = wordRepository.findAll();
        assertThat(words).hasSize(databaseSizeBeforeUpdate);
        Word testWord = words.get(words.size() - 1);
        assertThat(testWord.getWord()).isEqualTo(UPDATED_WORD);
        assertThat(testWord.getOriginal_word()).isEqualTo(UPDATED_ORIGINAL_WORD);
    }

    @Test
    @Transactional
    public void deleteWord() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

		int databaseSizeBeforeDelete = wordRepository.findAll().size();

        // Get the word
        restWordMockMvc.perform(delete("/api/words/{id}", word.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Word> words = wordRepository.findAll();
        assertThat(words).hasSize(databaseSizeBeforeDelete - 1);
    }
}

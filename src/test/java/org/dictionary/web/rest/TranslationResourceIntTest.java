package org.dictionary.web.rest;

import org.dictionary.Application;
import org.dictionary.domain.Translation;
import org.dictionary.repository.TranslationRepository;
import org.dictionary.repository.search.TranslationSearchRepository;

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
 * Test class for the TranslationResource REST controller.
 *
 * @see TranslationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TranslationResourceIntTest {

    private static final String DEFAULT_USAGE = "AAAAA";
    private static final String UPDATED_USAGE = "BBBBB";

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;

    @Inject
    private TranslationRepository translationRepository;

    @Inject
    private TranslationSearchRepository translationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTranslationMockMvc;

    private Translation translation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TranslationResource translationResource = new TranslationResource();
        ReflectionTestUtils.setField(translationResource, "translationRepository", translationRepository);
        ReflectionTestUtils.setField(translationResource, "translationSearchRepository", translationSearchRepository);
        this.restTranslationMockMvc = MockMvcBuilders.standaloneSetup(translationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        translation = new Translation();
        translation.setUsage(DEFAULT_USAGE);
        translation.setPriority(DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    public void createTranslation() throws Exception {
        int databaseSizeBeforeCreate = translationRepository.findAll().size();

        // Create the Translation

        restTranslationMockMvc.perform(post("/api/translations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(translation)))
                .andExpect(status().isCreated());

        // Validate the Translation in the database
        List<Translation> translations = translationRepository.findAll();
        assertThat(translations).hasSize(databaseSizeBeforeCreate + 1);
        Translation testTranslation = translations.get(translations.size() - 1);
        assertThat(testTranslation.getUsage()).isEqualTo(DEFAULT_USAGE);
        assertThat(testTranslation.getPriority()).isEqualTo(DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    public void getAllTranslations() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translations
        restTranslationMockMvc.perform(get("/api/translations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(translation.getId().intValue())))
                .andExpect(jsonPath("$.[*].usage").value(hasItem(DEFAULT_USAGE.toString())))
                .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)));
    }

    @Test
    @Transactional
    public void getTranslation() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get the translation
        restTranslationMockMvc.perform(get("/api/translations/{id}", translation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(translation.getId().intValue()))
            .andExpect(jsonPath("$.usage").value(DEFAULT_USAGE.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY));
    }

    @Test
    @Transactional
    public void getNonExistingTranslation() throws Exception {
        // Get the translation
        restTranslationMockMvc.perform(get("/api/translations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTranslation() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

		int databaseSizeBeforeUpdate = translationRepository.findAll().size();

        // Update the translation
        translation.setUsage(UPDATED_USAGE);
        translation.setPriority(UPDATED_PRIORITY);

        restTranslationMockMvc.perform(put("/api/translations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(translation)))
                .andExpect(status().isOk());

        // Validate the Translation in the database
        List<Translation> translations = translationRepository.findAll();
        assertThat(translations).hasSize(databaseSizeBeforeUpdate);
        Translation testTranslation = translations.get(translations.size() - 1);
        assertThat(testTranslation.getUsage()).isEqualTo(UPDATED_USAGE);
        assertThat(testTranslation.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    public void deleteTranslation() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

		int databaseSizeBeforeDelete = translationRepository.findAll().size();

        // Get the translation
        restTranslationMockMvc.perform(delete("/api/translations/{id}", translation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Translation> translations = translationRepository.findAll();
        assertThat(translations).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package org.dictionary.web.rest;

import org.dictionary.Application;
import org.dictionary.domain.FileImport;
import org.dictionary.repository.FileRepository;
import org.dictionary.repository.search.FileSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the FileResource REST controller.
 *
 * @see FileResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FileResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_STATUS = "AAAAA";
    private static final String UPDATED_STATUS = "BBBBB";
    private static final String DEFAULT_COMMENTS = "AAAAA";
    private static final String UPDATED_COMMENTS = "BBBBB";

    @Inject
    private FileRepository fileRepository;

    @Inject
    private FileSearchRepository fileSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFileMockMvc;

    private FileImport fileImport;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FileResource fileResource = new FileResource();
        ReflectionTestUtils.setField(fileResource, "fileRepository", fileRepository);
        ReflectionTestUtils.setField(fileResource, "fileSearchRepository", fileSearchRepository);
        this.restFileMockMvc = MockMvcBuilders.standaloneSetup(fileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fileImport = new FileImport();
        fileImport.setName(DEFAULT_NAME);
        fileImport.setDate(DEFAULT_DATE);
        fileImport.setStatus(DEFAULT_STATUS);
        fileImport.setComments(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    public void createFile() throws Exception {
        int databaseSizeBeforeCreate = fileRepository.findAll().size();

        // Create the FileImport

        restFileMockMvc.perform(post("/api/files")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fileImport)))
                .andExpect(status().isCreated());

        // Validate the FileImport in the database
        List<FileImport> fileImports = fileRepository.findAll();
        assertThat(fileImports).hasSize(databaseSizeBeforeCreate + 1);
        FileImport testFile = fileImports.get(fileImports.size() - 1);
        assertThat(testFile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFile.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testFile.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFile.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileRepository.findAll().size();
        // set the field null
        fileImport.setName(null);

        // Create the FileImport, which fails.

        restFileMockMvc.perform(post("/api/files")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fileImport)))
                .andExpect(status().isBadRequest());

        List<FileImport> fileImports = fileRepository.findAll();
        assertThat(fileImports).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileRepository.findAll().size();
        // set the field null
        fileImport.setDate(null);

        // Create the FileImport, which fails.

        restFileMockMvc.perform(post("/api/files")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fileImport)))
                .andExpect(status().isBadRequest());

        List<FileImport> fileImports = fileRepository.findAll();
        assertThat(fileImports).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileRepository.findAll().size();
        // set the field null
        fileImport.setStatus(null);

        // Create the FileImport, which fails.

        restFileMockMvc.perform(post("/api/files")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fileImport)))
                .andExpect(status().isBadRequest());

        List<FileImport> fileImports = fileRepository.findAll();
        assertThat(fileImports).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFiles() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(fileImport);

        // Get all the files
        restFileMockMvc.perform(get("/api/files"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fileImport.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())));
    }

    @Test
    @Transactional
    public void getFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(fileImport);

        // Get the fileImport
        restFileMockMvc.perform(get("/api/files/{id}", fileImport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fileImport.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFile() throws Exception {
        // Get the fileImport
        restFileMockMvc.perform(get("/api/files/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(fileImport);

		int databaseSizeBeforeUpdate = fileRepository.findAll().size();

        // Update the fileImport
        fileImport.setName(UPDATED_NAME);
        fileImport.setDate(UPDATED_DATE);
        fileImport.setStatus(UPDATED_STATUS);
        fileImport.setComments(UPDATED_COMMENTS);

        restFileMockMvc.perform(put("/api/files")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fileImport)))
                .andExpect(status().isOk());

        // Validate the FileImport in the database
        List<FileImport> fileImports = fileRepository.findAll();
        assertThat(fileImports).hasSize(databaseSizeBeforeUpdate);
        FileImport testFile = fileImports.get(fileImports.size() - 1);
        assertThat(testFile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFile.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testFile.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFile.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void deleteFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(fileImport);

		int databaseSizeBeforeDelete = fileRepository.findAll().size();

        // Get the fileImport
        restFileMockMvc.perform(delete("/api/files/{id}", fileImport.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FileImport> fileImports = fileRepository.findAll();
        assertThat(fileImports).hasSize(databaseSizeBeforeDelete - 1);
    }
}

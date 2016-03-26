package org.dictionary.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.NoSuchElementException;

import org.dictionary.domain.Language;
import org.dictionary.domain.Translation;
import org.dictionary.domain.Word;
import org.dictionary.repository.FileRepository;
import org.dictionary.repository.LanguageRepositoryCustom;
import org.dictionary.repository.TranslationRepository;
import org.dictionary.repository.TranslationRepositoryCustom;
import org.dictionary.repository.WordRepository;
import org.dictionary.repository.WordRepositoryCustom;
import org.dictionary.service.util.FileImportActionType;
import org.dictionary.translator.FileImportTranslator;
import org.dictionary.util.FileImportException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.verification.Times;

public class DefaultFileImportServiceTest {

    private LanguageRepositoryCustom languageRepositoryCustom = mock(LanguageRepositoryCustom.class);
    private WordRepositoryCustom wordRepositoryCustom = mock(WordRepositoryCustom.class);
    private WordRepository wordRepository = mock(WordRepository.class);
    private TranslationRepositoryCustom translationRepositoryCustom = mock(TranslationRepositoryCustom.class);
    private TranslationRepository translationRepository = mock(TranslationRepository.class);
    private FileImportTranslator fileImportTranslator = mock(FileImportTranslator.class);
    private FileRepository fileRepository = mock(FileRepository.class);
    
    private FileImportService service;
    private Language englishLanguage;
    private Language cantoneseLanguage;

    @Before
    public void init() {
        service = new DefaultFileImportService(languageRepositoryCustom, wordRepositoryCustom, wordRepository,
                translationRepositoryCustom, translationRepository, fileImportTranslator, fileRepository);
        englishLanguage = mock(Language.class);
        when(englishLanguage.getId()).thenReturn(1L);
        cantoneseLanguage = mock(Language.class);
        when(cantoneseLanguage.getId()).thenReturn(2L);
        when(languageRepositoryCustom.findByLanguage("latin")).thenReturn(null);
        when(languageRepositoryCustom.findByLanguage("english")).thenReturn(englishLanguage);
        when(languageRepositoryCustom.findByLanguage("cantonese")).thenReturn(cantoneseLanguage);
        Word yan = mock(Word.class);
        when(wordRepositoryCustom.loadWord("yan4", cantoneseLanguage.getId())).thenReturn(yan);
        Word france = mock(Word.class);
        when(wordRepositoryCustom.loadWord("France", englishLanguage.getId())).thenReturn(france);
        Word yat = mock(Word.class);
        when(yat.getId()).thenReturn(1L);
        when(wordRepositoryCustom.loadWord("yat1", cantoneseLanguage.getId())).thenReturn(yat);
        Word one = mock(Word.class);
        when(one.getId()).thenReturn(2L);
        when(wordRepositoryCustom.loadWord("one", englishLanguage.getId())).thenReturn(one);
        Translation yatOne = mock(Translation.class);
        when(translationRepositoryCustom.findTranslation(one.getId(), yat.getId())).thenReturn(yatOne);
    }

    @After
    public void tearDown() {
        service = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImportNullFile() {
        service.importFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImportEmptyFile() {
        service.importFile("");
    }

    @Test(expected = FileImportException.class)
    public void testImportFileInvalidHeaderOneToken() {
        service.importFile("Invalid header");
    }

    @Test(expected = FileImportException.class)
    public void testImportFileInvalidHeaderTwoTokens() {
        service.importFile("english, cantonese");
    }

    @Test(expected = FileImportException.class)
    public void testImportFileInvalidHeaderWrongUsage() {
        service.importFile("english, cantonese, usagex");
    }

    @Test(expected = FileImportException.class)
    public void testImportFileInvalidUnknownLanguage1() {
        service.importFile("latin, cantonese, usage");
    }

    @Test(expected = FileImportException.class)
    public void testImportFileInvalidUnknownLanguage2() {
        service.importFile("cantonese, latin, usage");
    }

    @Test
    public void testImportFileCreateNothingNoWords() {
        Map<FileImportActionType, Integer> actionTypes = service.importFile("english, cantonese, usage");
        int expectedWordsCreated = 0;
        int expectedTranslationsCreated = 0;
        checkResult(actionTypes, expectedWordsCreated, expectedTranslationsCreated);
    }

    @Test
    public void testImportFileCreate4Words2Translations() {
        Map<FileImportActionType, Integer> actionTypes = 
                service.importFile("english, cantonese, usage"
                + "\ngood, hou2, hou2 hou2"
                + "\ncold, dung1");
        int expectedWordsCreated = 4;
        int expectedTranslationsCreated = 2;
        checkResult(actionTypes, expectedWordsCreated, expectedTranslationsCreated);
    }
    
    @Test
    public void testImportFileCreate2Words2Translations() {
        Map<FileImportActionType, Integer> actionTypes = 
                service.importFile("english, cantonese, usage"
                + "\nperson, yan4"
                + "\nFrance, faat3 gwok3");
        int expectedWordsCreated = 2;
        int expectedTranslationsCreated = 2;
        checkResult(actionTypes, expectedWordsCreated, expectedTranslationsCreated);

    }
    
    @Test
    public void testImportFileCreate0Words1Translation() {
        Map<FileImportActionType, Integer> actionTypes = 
                service.importFile("english, cantonese, usage"
                + "\nFrance, yan4, not real");
        int expectedWordsCreated = 0;
        int expectedTranslationsCreated = 1;
        checkResult(actionTypes, expectedWordsCreated, expectedTranslationsCreated);
    }
    
    @Test
    public void testImportFileCreate0Words0Translation() {
        Map<FileImportActionType, Integer> actionTypes = 
                service.importFile("english, cantonese, usage"
                + "\none, yat1");
        int expectedWordsCreated = 0;
        int expectedTranslationsCreated = 0;
        checkResult(actionTypes, expectedWordsCreated, expectedTranslationsCreated);
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testImportFileCreateNothingBadRow() {
        service.importFile("english, cantonese, usage" 
                + "\ngood, hou2, hou2 hou2" 
                + "\nBAD ROW" 
                + "\ncold, dung1");
    }

    private void checkResult(Map<FileImportActionType, Integer> actionTypes, int expectedWordsCreated,
            int expectedTranslationsCreated) {
        Assert.assertNotNull(actionTypes);
        Assert.assertEquals(2, actionTypes.keySet().size());
        Assert.assertEquals(expectedWordsCreated, actionTypes.get(FileImportActionType.WORD_CREATION).intValue());
        Assert.assertEquals(expectedTranslationsCreated, actionTypes.get(FileImportActionType.TRANSLATION_CREATION)
                .intValue());
        
        verify(wordRepository, new Times(expectedWordsCreated)).save(any(Word.class));
        verify(translationRepository, new Times(expectedTranslationsCreated)).save(any(Translation.class));
    }
}

package org.dictionary.web.rest;

import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.dictionary.api.MultipleChoiceQuizAPI;
import org.dictionary.service.MultipleChoiceQuizService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping("/api")
public class MultipleChoiceQuizResource {

    private final Logger log = LoggerFactory.getLogger(MultipleChoiceQuizResource.class);

    @Inject
    private MultipleChoiceQuizService multipleChoiceQuizService;

    @Timed
    @RequestMapping(value = "/quiz/languages/{fromLanguageId}/to/{toLanguageId}",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MultipleChoiceQuizAPI> getQuiz(@PathVariable Long fromLanguageId,
            @PathVariable Long toLanguageId,
            @RequestParam(value = "tagId", required = false) Long tagId,
            @RequestParam(value = "selectedNumWords", required = false) Integer selectedNumWords) {

        log.debug("quiz from language {} to language {} tag {} with number of words {}", fromLanguageId, toLanguageId,
                tagId, selectedNumWords);
        
        MultipleChoiceQuizAPI quiz = multipleChoiceQuizService.getMultipleChoiceQuizAPI(fromLanguageId, toLanguageId,
                Optional.ofNullable(tagId), Optional.ofNullable(selectedNumWords));

        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }
    
    @Timed
    @RequestMapping(value = "/quiz",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MultipleChoiceQuizAPI> submitQuiz(@Valid @RequestBody MultipleChoiceQuizAPI quiz) {

        log.debug("quiz: {}", quiz);
        
        multipleChoiceQuizService.setCorrectAnswers(quiz);

        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }
    
}

// 'use strict';

angular.module('dictionaryApp')
    .service('MultipleChoiceQuizQuestionService', function ($resource) {
        var words = [];

        function setWords(newWords) {
        	words = newWords;
        }

        function getWords() {
        	return words;
        }

        function submit() {
        	console.log('submitting quiz');
        	var Quiz = $resource('api/quiz'); 
        	var myQuiz = new Quiz();

        	myQuiz.questions = words;
        	myQuiz.$save();
        	
        }

		return {
			setWords: setWords,
			getWords: getWords,
			submit: submit
		}
    });

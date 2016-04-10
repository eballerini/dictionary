// 'use strict';

angular.module('dictionaryApp')
    .service('MultipleChoiceQuizQuestionService', function () {
        var words = [];

        function setWords(newWords) {
        	words = newWords;
        }

        function getWords() {
        	return words;
        }

		return {
			setWords: setWords,
			getWords: getWords
		}
    });

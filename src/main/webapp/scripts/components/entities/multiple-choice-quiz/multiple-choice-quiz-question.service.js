// 'use strict';

angular.module('dictionaryApp')
    .service('MultipleChoiceQuizQuestionService', function ($resource) {

        // TODO revisit this
        var words = [];
        var quizResultId;

        function setWords(newWords) {
        	words = newWords;
        }

        function getWords() {
        	return words;
        }

        function setQuizResultId(newQuizResultId) {
            quizResultId = newQuizResultId;
        }

        function getQuizResultId() {
            return quizResultId;
        }

        function submit() {
        	console.log('submitting quiz');
        	var Quiz = $resource('api/quiz'); 
        	var myQuiz = new Quiz();

        	myQuiz.questions = words;
            myQuiz.quizResultId = quizResultId;
        	return myQuiz.$save();
        }

		return {
			setWords: setWords,
			getWords: getWords,
            setQuizResultId: setQuizResultId,
            getQuizResultId: getQuizResultId,
			submit: submit
		}
    });

'use strict';

angular.module('dictionaryApp')
    .controller('MultipleChoiceQuizQuestionController', function ($scope, $state, $modal, Language, WordSearch, TranslationSearch, Tag, MultipleChoiceQuiz, MultipleChoiceQuizQuestionService) {

        $scope.words = [];
        $scope.current_word_index = 0;
        $scope.total_num_words = 0;

        function getWords() {
            $scope.words = MultipleChoiceQuizQuestionService.getWords();
            $scope.total_num_words = $scope.words.length;
            if ($scope.total_num_words > 0) {
                $scope.loaded = true;
            } else {
                $scope.errorLoading = true;
            }
        }

        getWords();

        $scope.submit = function() {
            MultipleChoiceQuizQuestionService.setWords($scope.words);
            MultipleChoiceQuizQuestionService.submit();
        }

        $scope.isLastWord = function() {
            if ($scope.current_word_index < $scope.total_num_words - 1) {
                return false;
            }
            return true;
        }

        $scope.isFirstWord = function() {
            return $scope.current_word_index == 0;
        }

        $scope.previousWord = function() {
            if ($scope.isFirstWord()) {
                return;
            }
            $scope.current_word_index--;
        }

        $scope.nextWord = function() {
            if ($scope.isLastWord()) {
                return;
            }
            $scope.current_word_index++;
        }

    });

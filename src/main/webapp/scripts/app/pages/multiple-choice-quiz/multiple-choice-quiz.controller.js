'use strict';

angular.module('dictionaryApp')
    .controller('MultipleChoiceQuizController', function ($scope, $state, $modal, Language, WordSearch, TranslationSearch, Tag, MultipleChoiceQuiz, MultipleChoiceQuizQuestionService) {

      
        $scope.languages = [];
        $scope.tags = [];
        $scope.selected_num_words = 2;
        $scope.num_words = [2, 5, 10, 20];
        $scope.words = [];
        $scope.current_word_index = 0;
        $scope.total_num_words = 0;

        $scope.loadLanguages = function() {
            Language.query(function(result) {
               $scope.languages = result;
               $scope.from_language = $scope.languages[0];
               $scope.to_language = $scope.languages[1];
            });
        };

        $scope.loadTags = function() {
            Tag.query(function(result) {
                $scope.tags = result;
            });
        };

        $scope.loadLanguages();
        $scope.loadTags();

        $scope.submit = function() {

            var tagId = $scope.tag == null ? null : $scope.tag.id;

            MultipleChoiceQuiz.query({fromLanguageId: $scope.from_language.id, 
                toLanguageId: $scope.to_language.id, 
                tagId: tagId, 
                selectedNumWords: $scope.selected_num_words}, function(result) {

                console.log('loaded');
                $scope.words = result.questions;
                console.log($scope.words);
                $scope.total_num_words = $scope.words.length;
                $scope.loaded = true;
                
                if ($scope.total_num_words > 0) {
                    MultipleChoiceQuizQuestionService.setWords(result.questions);    
                    $state.go('multiple-choice-quiz.questions');
                } else {

                }
                
            }, function(response) {
                alert('error');
                if(response.status === 404) {
                    $scope.loaded = false;
                    $scope.errorLoading = true;
                    console.log('no words found');
                }
            });
        }

        $scope.switchLanguages = function() {
            var tmp_language = $scope.from_language;
            $scope.from_language = $scope.to_language;
            $scope.to_language = tmp_language;
            $scope.reset();
        }

        $scope.pickRandomTag = function() {
            var numTags = $scope.tags.length;
            var tagNum = getRandomNumber(0, numTags - 1);
            $scope.tag = $scope.tags[tagNum];
            $scope.reset();
        }

        $scope.reset = function() {
            $scope.loaded = false;
            $scope.show = false;
            $scope.translations = null;
        }

        function getRandomNumber(min, max) {
            return Math.floor(Math.random() * (max - min + 1)) + min;
        }

    });

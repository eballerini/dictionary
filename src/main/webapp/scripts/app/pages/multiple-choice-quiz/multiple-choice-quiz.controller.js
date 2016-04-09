'use strict';

angular.module('dictionaryApp')
    .controller('MultipleChoiceQuizController', function ($scope, $state, $modal, Language, WordSearch, TranslationSearch, Tag, MultipleChoiceQuiz) {

      
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

        $scope.loadWords = function() {

            var tagId = $scope.tag == null ? null : $scope.tag.id;

            console.log('$scope.selected_num_words: ' + $scope.selected_num_words);
            MultipleChoiceQuiz.query({fromLanguageId: $scope.from_language.id, 
                toLanguageId: $scope.to_language.id, 
                tagId: tagId, 
                selectedNumWords: $scope.selected_num_words}, function(result) {

                console.log('loaded');
                $scope.words = result.questions;
                console.log($scope.words);
                $scope.total_num_words = $scope.words.length;
                $scope.loaded = true;
            }, function(response) {
                alert('error');
                if(response.status === 404) {
                    $scope.loaded = false;
                    $scope.errorLoading = true;
                    console.log('no words found');
                }
            });
        }

        $scope.submit = function() {
            alert('TODO');
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

        $scope.isLastWord = function() {
            if ($scope.current_word_index < $scope.total_num_words - 1) {
                return false;
            }
            return true;
        }

        $scope.nextWord = function() {
            if ($scope.isLastWord()) {
                return;
            }
            $scope.current_word_index++;
        }

        function getRandomNumber(min, max) {
            return Math.floor(Math.random() * (max - min + 1)) + min;
        }

    });

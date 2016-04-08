'use strict';

angular.module('dictionaryApp')
    .controller('MultipleChoiceQuizController', function ($scope, $state, $modal, Language, WordSearch, TranslationSearch, Tag, MultipleChoiceQuiz) {

        // TODO clean
      
        $scope.languages = [];
        // $scope.numWordsSeen = 0;
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

            MultipleChoiceQuiz.query({fromLanguageId: $scope.from_language.id, 
                toLanguageId: $scope.to_language.id, 
                tagId: tagId, 
                selectedNumWords: $scope.selected_num_words}, function(result) {

                console.log('loaded');
                $scope.words = result.questions;
                console.log($scope.words);
                $scope.total_num_words = $scope.words.length;
                // $scope.word = result;
                $scope.loaded = true;
                // $scope.errorLoading = false;
                // findTranslations($scope.word.id, $scope.to_language.id);
            }, function(response) {
                alert('error');
                if(response.status === 404) {
                    $scope.loaded = false;
                    $scope.errorLoading = true;
                    console.log('no words found');
                }
            });
            // $scope.translations = null;
            // $scope.show = false;
            // $scope.numWordsSeen = $scope.numWordsSeen + 1;

            // var tagId = $scope.tag == null ? null : $scope.tag.id;

            // WordSearch.findRandom({languageId: $scope.from_language.id, tagId: tagId}, function(result) {
            //     $scope.word = result;
            //     $scope.loaded = true;
            //     $scope.errorLoading = false;
            //     findTranslations($scope.word.id, $scope.to_language.id);
            // }, function(response) {
            //     if(response.status === 404) {
            //         $scope.loaded = false;
            //         $scope.errorLoading = true;
            //         console.log('no words found');
            //     }
            // });            

        }

        // $scope.showTranslations = function () {
        //     $scope.show = true;
        // }

        // $scope.hideTranslations = function () {
        //     $scope.show = false;
        // }

        // function findTranslations(wordId, toLanguageId) {
        //     TranslationSearch.find({wordId: wordId, toLanguageId: toLanguageId}, function(result) {
        //         $scope.translations = result;
        //         $scope.translationsReady = true;
        //     }, function(response) {
        //         if(response.status === 404) {
        //             $scope.translationsReady = false;
        //             console.log('could not load translations');
        //         }
        //     });
        // }

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
            // $scope.numWordsSeen = 0;
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

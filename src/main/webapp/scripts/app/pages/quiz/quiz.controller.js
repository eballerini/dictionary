'use strict';

angular.module('dictionaryApp')
    .controller('QuizController', function ($scope, $state, $modal, Language, WordSearch, TranslationSearch, Tag) {
      
        $scope.languages = [];
        $scope.numWordsSeen = 0;
        $scope.tags = [];

        $scope.loadAll = function() {
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

        $scope.loadAll();
        $scope.loadTags();

        $scope.submit = function() {
            $scope.translations = null;
            $scope.show = false;
            $scope.numWordsSeen = $scope.numWordsSeen + 1;

            var tagId = $scope.tag == null ? null : $scope.tag.id;

            WordSearch.findRandom({languageId: $scope.from_language.id, tagId: tagId}, function(result) {
                $scope.word = result;
                $scope.loaded = true;
                $scope.errorLoading = false;
                findTranslations($scope.word.id, $scope.to_language.id);
            }, function(response) {
                if(response.status === 404) {
                    $scope.loaded = false;
                    $scope.errorLoading = true;
                    console.log('no words found');
                }
            });            

        }

        $scope.showTranslations = function () {
            $scope.show = true;
        }

        $scope.hideTranslations = function () {
            $scope.show = false;
        }

        function findTranslations(wordId, toLanguageId) {
            TranslationSearch.find({wordId: wordId, toLanguageId: toLanguageId}, function(result) {
                $scope.translations = result;
                $scope.translationsReady = true;
            }, function(response) {
                if(response.status === 404) {
                    $scope.translationsReady = false;
                    console.log('could not load translations');
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
            $scope.numWordsSeen = 0;
            $scope.loaded = false;
            $scope.show = false;
            $scope.translations = null;
        }

        function getRandomNumber(min, max) {
            return Math.floor(Math.random() * (max - min + 1)) + min;
        }

    });

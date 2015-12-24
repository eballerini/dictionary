'use strict';

angular.module('dictionaryApp')
    .controller('WordController', function ($scope, $state, $modal, Word, WordSearch, ParseLinks) {
      
        $scope.words = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Word.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.words = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            WordSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.words = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.word = {
                word: null,
                original_word: null,
                id: null
            };
        };
    });

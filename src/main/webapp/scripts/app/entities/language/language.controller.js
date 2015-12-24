'use strict';

angular.module('dictionaryApp')
    .controller('LanguageController', function ($scope, $state, $modal, Language, LanguageSearch) {
      
        $scope.languages = [];
        $scope.loadAll = function() {
            Language.query(function(result) {
               $scope.languages = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            LanguageSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.languages = result;
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
            $scope.language = {
                language: null,
                id: null
            };
        };
    });

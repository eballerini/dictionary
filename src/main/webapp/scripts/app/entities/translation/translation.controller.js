'use strict';

angular.module('dictionaryApp')
    .controller('TranslationController', function ($scope, $state, $modal, Translation, TranslationSearch, ParseLinks) {
      
        $scope.translations = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Translation.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.translations = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            TranslationSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.translations = result;
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
            $scope.translation = {
                usage: null,
                priority: null,
                id: null
            };
        };
    });

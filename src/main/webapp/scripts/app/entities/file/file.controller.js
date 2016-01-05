'use strict';

angular.module('dictionaryApp')
    .controller('FileController', function ($scope, $state, $modal, File, FileSearch, ParseLinks) {
      
        $scope.files = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            File.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.files = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            FileSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.files = result;
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
            $scope.file = {
                name: null,
                date: null,
                status: null,
                comments: null,
                id: null
            };
        };
    });

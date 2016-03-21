'use strict';

angular.module('dictionaryApp')
    .controller('TagController', function ($scope, $state, $modal, Tag, TagSearch, ParseLinks) {
      
        $scope.tags = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Tag.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.tags = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            TagSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.tags = result;
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
            $scope.tag = {
                name: null,
                id: null
            };
        };
    });

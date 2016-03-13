'use strict';

angular.module('dictionaryApp')
    .controller('DashboardController', function ($scope, $state, Language, WordSearch, TranslationSearch) {
      
        $scope.languages = [];

        $scope.loadAll = function() {
            Language.query(function(result) {
               $scope.languages = result;
            });
        };
        $scope.loadAll();

    });

'use strict';

angular.module('dictionaryApp')
    .controller('WordDetailController', function ($scope, $rootScope, $stateParams, entity, Word, Language) {
        $scope.word = entity;
        $scope.load = function (id) {
            Word.get({id: id}, function(result) {
                $scope.word = result;
            });
        };
        var unsubscribe = $rootScope.$on('dictionaryApp:wordUpdate', function(event, result) {
            $scope.word = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

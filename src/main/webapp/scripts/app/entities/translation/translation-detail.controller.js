'use strict';

angular.module('dictionaryApp')
    .controller('TranslationDetailController', function ($scope, $rootScope, $stateParams, entity, Translation, Word) {
        $scope.translation = entity;
        $scope.load = function (id) {
            Translation.get({id: id}, function(result) {
                $scope.translation = result;
            });
        };
        var unsubscribe = $rootScope.$on('dictionaryApp:translationUpdate', function(event, result) {
            $scope.translation = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

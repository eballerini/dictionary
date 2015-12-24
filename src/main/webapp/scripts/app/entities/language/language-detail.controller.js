'use strict';

angular.module('dictionaryApp')
    .controller('LanguageDetailController', function ($scope, $rootScope, $stateParams, entity, Language) {
        $scope.language = entity;
        $scope.load = function (id) {
            Language.get({id: id}, function(result) {
                $scope.language = result;
            });
        };
        var unsubscribe = $rootScope.$on('dictionaryApp:languageUpdate', function(event, result) {
            $scope.language = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

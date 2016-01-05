'use strict';

angular.module('dictionaryApp')
    .controller('FileDetailController', function ($scope, $rootScope, $stateParams, entity, File) {
        $scope.file = entity;
        $scope.load = function (id) {
            File.get({id: id}, function(result) {
                $scope.file = result;
            });
        };
        var unsubscribe = $rootScope.$on('dictionaryApp:fileUpdate', function(event, result) {
            $scope.file = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

'use strict';

angular.module('dictionaryApp')
	.controller('LanguageDeleteController', function($scope, $modalInstance, entity, Language) {

        $scope.language = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Language.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
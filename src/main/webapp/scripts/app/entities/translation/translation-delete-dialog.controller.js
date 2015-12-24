'use strict';

angular.module('dictionaryApp')
	.controller('TranslationDeleteController', function($scope, $modalInstance, entity, Translation) {

        $scope.translation = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Translation.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
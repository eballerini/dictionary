'use strict';

angular.module('dictionaryApp')
	.controller('FileDeleteController', function($scope, $modalInstance, entity, File) {

        $scope.file = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            File.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
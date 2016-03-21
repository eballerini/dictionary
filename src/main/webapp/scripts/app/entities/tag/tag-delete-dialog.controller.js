'use strict';

angular.module('dictionaryApp')
	.controller('TagDeleteController', function($scope, $modalInstance, entity, Tag) {

        $scope.tag = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Tag.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
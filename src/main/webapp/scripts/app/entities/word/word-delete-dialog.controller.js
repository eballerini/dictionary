'use strict';

angular.module('dictionaryApp')
	.controller('WordDeleteController', function($scope, $modalInstance, entity, Word) {

        $scope.word = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Word.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
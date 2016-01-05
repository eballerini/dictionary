'use strict';

angular.module('dictionaryApp').controller('FileDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'File',
        function($scope, $stateParams, $modalInstance, entity, File) {

        $scope.file = entity;
        $scope.load = function(id) {
            File.get({id : id}, function(result) {
                $scope.file = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('dictionaryApp:fileUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.file.id != null) {
                File.update($scope.file, onSaveSuccess, onSaveError);
            } else {
                File.save($scope.file, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

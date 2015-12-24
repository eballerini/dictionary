'use strict';

angular.module('dictionaryApp').controller('LanguageDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Language',
        function($scope, $stateParams, $modalInstance, entity, Language) {

        $scope.language = entity;
        $scope.load = function(id) {
            Language.get({id : id}, function(result) {
                $scope.language = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('dictionaryApp:languageUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.language.id != null) {
                Language.update($scope.language, onSaveSuccess, onSaveError);
            } else {
                Language.save($scope.language, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

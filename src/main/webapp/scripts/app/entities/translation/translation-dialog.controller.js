'use strict';

angular.module('dictionaryApp').controller('TranslationDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Translation', 'Word',
        function($scope, $stateParams, $modalInstance, entity, Translation, Word) {

        $scope.translation = entity;
        $scope.words = Word.query();
        $scope.load = function(id) {
            Translation.get({id : id}, function(result) {
                $scope.translation = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('dictionaryApp:translationUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.translation.id != null) {
                Translation.update($scope.translation, onSaveSuccess, onSaveError);
            } else {
                Translation.save($scope.translation, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

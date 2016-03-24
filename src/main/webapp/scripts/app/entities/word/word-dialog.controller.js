'use strict';

angular.module('dictionaryApp').controller('WordDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Word', 'Language', 'Tag',
        function($scope, $stateParams, $modalInstance, entity, Word, Language, Tag) {

        $scope.word = entity;
        $scope.languages = Language.query();
        $scope.tags = [];
        loadAllTags();

        $scope.load = function(id) {
            Word.get({id : id}, function(result) {
                $scope.word = result;
            });
        };

        function loadAllTags() {
            Tag.query({}, function(result, headers) {
                $scope.tags = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('dictionaryApp:wordUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.word.id != null) {
                Word.update($scope.word, onSaveSuccess, onSaveError);
            } else {
                Word.save($scope.word, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

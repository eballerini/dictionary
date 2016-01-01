'use strict';

angular.module('dictionaryApp')
    .controller('FileImportController', function ($scope, $state, $modal, Upload) {
      
        $scope.resetStatus = function() {
            $scope.success = false;
            $scope.failure = false;
        };

        $scope.resetStatus();

        $scope.submit = function() {
            console.log('importing');
            if ($scope.file) {
                $scope.upload($scope.file);
            }
            

        }
        
        $scope.upload = function (file) {
            console.log('file name: ' + file.name);
            Upload.upload({
                url: 'api/files',
                fields: {'name': file.name}, // additional data to send. TODO remove
                file: file
            }).progress(function (evt) {
                var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                console.log('progress: ' + progressPercentage + '% ' + evt.config.file.name);
            }).success(function (data, status, headers, config) {
                console.log('file ' + config.file.name + ' uploaded. Response: ' + data);
                if (data.success) {
                    $scope.success = true;    
                    $scope.numWordsCreated = data.numWordsCreated;
                    $scope.numTranslationsCreated = data.numTranslationsCreated;
                    $scope.numWordsNotCreated = data.numWordsNotCreated;
                    $scope.numTranslationsNotCreated = data.numTranslationsNotCreated;
                } else {
                    $scope.failure = true;    
                    $scope.message = data.message;
                }
                
            }).error(function (data, status, headers, config) {
                console.log('error. Status: ' + status);
                $scope.failure = true;
            });
        };

        

    });

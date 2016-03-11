'use strict';

angular.module('dictionaryApp')
    .controller('ElasticSearchController', function ($scope, $state, $modal, $http) {
      
        $scope.resetStatus = function() {
            $scope.success = false;
            $scope.processed = false;
        };

        $scope.resetStatus();
        
        $scope.submit = function() {
        	$http.post('api/indices/build')
        	.success(function (data, status, headers, config) {
            	$scope.processed = true;    
                if (data.success) {
                    $scope.success = true;    
                } else {
                    $scope.message = data.message;
                }
                
            }).error(function (data, status, headers, config) {
                $scope.processed = true;
            });
        };

        

    });

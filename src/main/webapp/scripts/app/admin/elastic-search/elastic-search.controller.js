'use strict';

angular.module('dictionaryApp')
    .controller('ElasticSearchController', function ($scope, $state, $http) {
      
        $scope.resetStatus = function() {
            $scope.success = false;
            $scope.processed = false;
        };

        $scope.resetStatus();
        
        $scope.submit = function() {
        	$("#submit").addClass("disabled");
        	$http.post('api/tasks/build-search-index')
        	.success(function (data, status) {
            	$scope.processed = true;    
                if (data) {
                    $scope.success = true;    
                }
            }).error(function (data, status) {
                $scope.processed = true;
            });
        };
    });

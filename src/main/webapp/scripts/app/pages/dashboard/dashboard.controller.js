'use strict';

angular.module('dictionaryApp')
    .controller('DashboardController', function ($scope, $state, Dashboard) {
      
        $scope.dashboard = null;

        $scope.load = function() {
        	Dashboard.query(function(result) {
               $scope.dashboard = result;
            });
        };
        
        $scope.load();

    });

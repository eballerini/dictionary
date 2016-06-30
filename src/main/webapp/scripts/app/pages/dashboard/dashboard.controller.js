'use strict';

angular.module('dictionaryApp')
    .controller('DashboardController', function ($scope, $state, Dashboard) {
      
        $scope.dashboard = null;
        $scope.dashboardstats = null;

        $scope.loadAll = function() {
          $scope.loadDashboard();
          $scope.loadDashboardStats();
        };

        $scope.loadDashboard = function() {
        	Dashboard.query(function(result) {
               $scope.dashboard = result;
            });
        };
        
        $scope.loadDashboardStats = function() {
        };

        $scope.loadAll();

    });

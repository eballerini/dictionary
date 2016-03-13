'use strict';

angular.module('dictionaryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('dashboard', {
                url: '/dashboard',
                parent: 'site',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Dashboard'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/pages/dashboard/dashboard.html',
                        controller: 'DashboardController'
                    }
                },
                resolve: {
                    
                }
            })
     });

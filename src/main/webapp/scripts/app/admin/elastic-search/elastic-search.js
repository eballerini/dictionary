'use strict';

angular.module('dictionaryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('elastic-search', {
                parent: 'admin',
                url: '/elastic-search',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Elastic Search'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/elastic-search/elastic-search.html',
                        controller: 'ElasticSearchController'
                    }
                },
                resolve: {
                    
                }
            });
    });

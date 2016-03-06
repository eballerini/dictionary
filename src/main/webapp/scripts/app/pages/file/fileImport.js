'use strict';

angular.module('dictionaryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('fileImport', {
                url: '/fileImport',
                parent: 'site',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'File import'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/pages/file/fileImport.html',
                        controller: 'FileImportController'
                    }
                },
                resolve: {
                    
                }
            })
     });

'use strict';

angular.module('dictionaryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('file', {
                url: '/file',
                parent: 'entity',
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

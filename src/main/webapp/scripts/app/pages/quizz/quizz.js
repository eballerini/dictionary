'use strict';

angular.module('dictionaryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('quizz', {
                url: '/quizz',
                parent: 'site',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Quizz'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/pages/quizz/quizz.html',
                        controller: 'QuizzController'
                    }
                },
                resolve: {
                    
                }
            })
     });

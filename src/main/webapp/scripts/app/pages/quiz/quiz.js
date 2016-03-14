'use strict';

angular.module('dictionaryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('quiz', {
                url: '/quiz',
                parent: 'site',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Quiz'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/pages/quiz/quiz.html',
                        controller: 'QuizController'
                    }
                },
                resolve: {
                    
                }
            })
     });

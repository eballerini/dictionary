'use strict';

angular.module('dictionaryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('multiple-choice-quiz', {
                url: '/multiple-choice-quiz',
                parent: 'site',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MultipleChoiceQuiz'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/pages/multiple-choice-quiz/multiple-choice-quiz.html',
                        controller: 'MultipleChoiceQuizController'
                    }
                },
                resolve: {
                    
                }
            })
            .state('multiple-choice-quiz.questions', {
                url: '/multiple-choice-quiz-questions',
                parent: 'site',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MultipleChoiceQuiz'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/pages/multiple-choice-quiz/multiple-choice-quiz-questions.html',
                        controller: 'MultipleChoiceQuizQuestionController'
                    }
                },
                resolve: {
                    
                }
            })
     });

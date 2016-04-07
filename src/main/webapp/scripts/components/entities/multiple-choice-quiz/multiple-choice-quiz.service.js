'use strict';

angular.module('dictionaryApp')
    .factory('MultipleChoiceQuiz', function ($resource) {
        return $resource('api/quiz/languages/:fromLanguageId/to/:toLanguageId', {}, {
            'query': { method: 'GET', params: {fromLanguageId: '@fromlanguageId', toLanguageId: '@tolanguageId'}}
        });
    });

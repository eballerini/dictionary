'use strict';

angular.module('dictionaryApp')
    .factory('TranslationSearch', function ($resource) {
        return $resource('api/_search/translations/:query', {}, {
            'query': { method: 'GET', isArray: true},
            'find' : { method: 'GET', isArray: true, url: 'api/translations/words/:wordId/languages/:toLanguageId', 
            	parameter: {wordId: '@wordId', toLanguageId: '@toLanguageId'}}
        });
    });

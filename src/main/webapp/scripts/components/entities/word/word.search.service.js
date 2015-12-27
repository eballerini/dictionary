'use strict';

angular.module('dictionaryApp')
    .factory('WordSearch', function ($resource) {
        return $resource('api/_search/words/:query', {}, {
            'query': { method: 'GET', isArray: true},
            'findRandom': { method: 'GET', url: 'api/_search/words/random/:languageId', params: {languageId: '@languageId'} }
        });
    });

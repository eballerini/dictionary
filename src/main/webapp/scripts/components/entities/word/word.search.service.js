'use strict';

angular.module('dictionaryApp')
    .factory('WordSearch', function ($resource) {
        return $resource('api/_search/words/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });

'use strict';

angular.module('dictionaryApp')
    .factory('LanguageSearch', function ($resource) {
        return $resource('api/_search/languages/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });

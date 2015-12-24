'use strict';

angular.module('dictionaryApp')
    .factory('TranslationSearch', function ($resource) {
        return $resource('api/_search/translations/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });

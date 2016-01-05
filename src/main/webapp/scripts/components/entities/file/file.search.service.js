'use strict';

angular.module('dictionaryApp')
    .factory('FileSearch', function ($resource) {
        return $resource('api/_search/files/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });

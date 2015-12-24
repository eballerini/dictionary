'use strict';

angular.module('dictionaryApp')
    .factory('Translation', function ($resource, DateUtils) {
        return $resource('api/translations/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });

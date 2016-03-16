'use strict';

angular.module('dictionaryApp')
    .factory('Dashboard', function ($resource, DateUtils) {
        return $resource('api/dashboard', {}, {
            'query': { method: 'GET', isArray: false},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    });

'use strict';

angular.module('dictionaryApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });



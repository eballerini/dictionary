/* globals $ */
'use strict';

angular.module('dictionaryApp')
    .directive('dictionaryAppPagination', function() {
        return {
            templateUrl: 'scripts/components/form/pagination.html'
        };
    });

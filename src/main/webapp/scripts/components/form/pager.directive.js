/* globals $ */
'use strict';

angular.module('dictionaryApp')
    .directive('dictionaryAppPager', function() {
        return {
            templateUrl: 'scripts/components/form/pager.html'
        };
    });

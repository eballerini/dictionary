 'use strict';

angular.module('dictionaryApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-dictionaryApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-dictionaryApp-params')});
                }
                return response;
            }
        };
    });

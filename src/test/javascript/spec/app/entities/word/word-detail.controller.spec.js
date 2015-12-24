'use strict';

describe('Word Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockWord, MockLanguage;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockWord = jasmine.createSpy('MockWord');
        MockLanguage = jasmine.createSpy('MockLanguage');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Word': MockWord,
            'Language': MockLanguage
        };
        createController = function() {
            $injector.get('$controller')("WordDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'dictionaryApp:wordUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

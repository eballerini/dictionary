'use strict';

describe('Translation Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockTranslation, MockWord;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockTranslation = jasmine.createSpy('MockTranslation');
        MockWord = jasmine.createSpy('MockWord');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Translation': MockTranslation,
            'Word': MockWord
        };
        createController = function() {
            $injector.get('$controller')("TranslationDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'dictionaryApp:translationUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

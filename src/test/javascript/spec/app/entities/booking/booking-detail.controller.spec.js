'use strict';

describe('Controller Tests', function() {

    describe('Booking Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBooking, MockCustomer, MockField;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBooking = jasmine.createSpy('MockBooking');
            MockCustomer = jasmine.createSpy('MockCustomer');
            MockField = jasmine.createSpy('MockField');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Booking': MockBooking,
                'Customer': MockCustomer,
                'Field': MockField
            };
            createController = function() {
                $injector.get('$controller')("BookingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bookingApp:bookingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

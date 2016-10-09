(function() {
    'use strict';

    angular
        .module('bookingApp')
        .controller('BookingDetailController', BookingDetailController);

    BookingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Booking', 'Customer', 'Field'];

    function BookingDetailController($scope, $rootScope, $stateParams, previousState, entity, Booking, Customer, Field) {
        var vm = this;

        vm.booking = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bookingApp:bookingUpdate', function(event, result) {
            vm.booking = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

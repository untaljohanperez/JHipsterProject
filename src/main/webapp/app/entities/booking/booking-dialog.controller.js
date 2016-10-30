(function() {
    'use strict';

    angular
        .module('bookingApp')
        .controller('BookingDialogController', BookingDialogController);

    BookingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Booking', 'Customer', 'Field'];

    function BookingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Booking, Customer, Field) {
        var vm = this;

        vm.booking = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.customers = Customer.query();
        vm.fields = Field.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            validateBooking();
        }
        
        function saveBooking(){
        	if (vm.booking.id !== null) {
                Booking.update(vm.booking, onSaveSuccess, onSaveError);
            } else {
                Booking.save(vm.booking, onSaveSuccess, onSaveError);
            }
        }
        
        function validateBooking(){
        	if(vm.booking.dateStart >= vm.booking.dateEnding){
        		sweetAlert("Oops...", "The date range is incorrect", "error");
        		onSaveError ();
        		return;
        	}
        	
        	let query = {
        			dateStart: vm.booking.dateStart,
        			dateEnding: vm.booking.dateEnding,
        			fieldId: vm.booking.fiel.id,
        	}
        	Booking.bookingsByDateAndField(query)
        		.$promise.then( bookings => {
        			if(bookings.length > 0){
        				sweetAlert("Oops...", "This field is already reserved of a customer", "error");
        				onSaveError ();
        			}else{
        				saveBooking();
        			}	
    		});
        }

        function onSaveSuccess (result) {
            $scope.$emit('bookingApp:bookingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateStart = false;
        vm.datePickerOpenStatus.dateEnding = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

(function() {
    'use strict';

    angular
        .module('bookingApp')
        .controller('FieldDetailController', FieldDetailController);

    FieldDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Field'];

    function FieldDetailController($scope, $rootScope, $stateParams, previousState, entity, Field) {
        var vm = this;

        vm.field = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bookingApp:fieldUpdate', function(event, result) {
            vm.field = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

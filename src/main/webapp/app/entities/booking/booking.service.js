(function() {
    'use strict';
    angular
        .module('bookingApp')
        .factory('Booking', Booking);

    Booking.$inject = ['$resource', 'DateUtils'];

    function Booking ($resource, DateUtils) {
        var resourceUrl =  'api/bookings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateStart = DateUtils.convertDateTimeFromServer(data.dateStart);
                        data.dateEnding = DateUtils.convertDateTimeFromServer(data.dateEnding);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

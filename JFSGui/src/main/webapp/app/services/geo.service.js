(function () {
    'use strict';

    angular
        .module('app')
        .factory('GeoService', GeoServiceFactory);

    GeoServiceFactory.$inject = ['$http'];
    function GeoServiceFactory($http) {

        /**
         * connecting internal functions for public access to GeoService
         */

        var service = {};

        service.getCoordinates = getCoordinates;
        service.getAddresses = getAddresses;

        return service;

        /**
         * functions that this service provides
         */

        function getCoordinates(address) {
            return $http.get('http://maps.googleapis.com/maps/api/geocode/json?&address=' + address)
                .then(handleSuccess, handleError('Error getting coordinates!'));
        }

        function getAddresses(lat, lng) {
            return $http.get('http://maps.googleapis.com/maps/api/geocode/json?&latlng=' + lat + ',' + lng)
                .then(handleSuccess, handleError('Error getting nearby addresses!'));
        }

        /**
         * private functions
         */

        function handleSuccess(res) {
            return { success: true, data: res.data };
        }

        function handleError(error) {
            return function () {
                return { success: false, message: error };
            };
        }
    }
})();
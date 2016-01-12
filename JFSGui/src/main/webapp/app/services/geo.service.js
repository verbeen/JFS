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

        service.getLocations = getLocations;
        service.getAddresses = getAddresses;
        service.createMap = createMap;
        service.createGreenIcon = createGreenIcon;
        service.createVioletIcon = createVioletIcon;

        return service;

        /**
         * functions that this service provides
         */

        function getLocations(address) {
            return $http.get('http://maps.googleapis.com/maps/api/geocode/json?address=' + address)
                .then(handleSuccess, handleError('Error getting coordinates!'));
        }

        function getAddresses(lat, lng) {
            return $http.get('http://maps.googleapis.com/maps/api/geocode/json?latlng=' + lat + ',' + lng)
                .then(handleSuccess, handleError('Error getting nearby addresses!'));
        }

        function createMap(elementId, clickFunction){
            var map = L.map(elementId).setView([48.7758459,9.1829321], 13);
            L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
                attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
            }).addTo(map);
            if(clickFunction != null) {
                map.on('click', clickFunction);
            }
            return map;
        }

        function createGreenIcon(){
            return new L.Icon.Default({
                iconUrl: 'vendor/leaflet-0.7.7/images/marker-icon-green.png',
                iconRetinaUrl: 'vendor/leaflet-0.7.7/images/marker-icon-green-2x.png'
            });
        }

        function createVioletIcon(){
            return new L.Icon.Default({
                iconUrl: 'vendor/leaflet-0.7.7/images/marker-icon-violet.png',
                iconRetinaUrl: 'vendor/leaflet-0.7.7/images/marker-icon-violet-2x.png'
            });
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
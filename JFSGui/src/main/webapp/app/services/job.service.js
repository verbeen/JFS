(function () {
    'use strict';

    angular
        .module('app')
        .factory('JobService', JobServiceFactory);

    JobServiceFactory.$inject = ['$http'];
    function JobServiceFactory($http) {
        var service = {};

        service.getAllJobs = getAllJobs;
        service.getJobProfile = getJobProfile;
        service.getRecentJobs = getRecentJobs;
        service.getJobsBySearchterm = getJobsBySearchterm;

        return service;

        function getAllJobs() {
            return $http.post('/service/offers/getall')
                .then(handleSuccess, handleError('Error getting all jobs!'));
        }

        function getJobProfile() {
            return $http.get('data/job-profile.json')
                .then(handleSuccess, handleError('Error getting job profile!'));
        }

        function getRecentJobs(amount) {
            return $http.post('/service/offers/search/recent', amount)
                .then(handleSuccess, handleError('Error getting most recent job offers!'));
        }

        function getJobsBySearchterm(term) {
            return $http.post('/service/offers/search/text', term)
                .then(handleSuccess, handleError('Error getting job offers by "' + term + '"!'));
        }

        // private functions

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
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
        service.getJobsBySearch = getJobsBySearch;
        service.createJob = createJob;

        return service;

        function getAllJobs(token) {
            return $http.post('/service/offers/getall', token)
                .then(handleSuccess, handleError('Error getting all jobs!'));
        }

        function getJobProfile(offerId) {
            return $http.get('/service/offers/' + offerId)
                .then(handleSuccess, handleError('Error getting job profile!'));
        }

        function getRecentJobs(amount) {
            return $http.post('/service/offers/search/recent', amount)
                .then(handleSuccess, handleError('Error getting most recent job offers!'));
        }

        function getJobsBySearch(searchParams) {
            return $http.post('/service/offers/search', searchParams)
                .then(handleSuccess, handleError('Error getting job offers by "' + searchParams + '"!'));
        }

        function createJob(jobDetails) {
            return $http.post('/service/offers/add', jobDetails)
                .then(handleSuccess, handleError('Error adding job offers by "' + jobDetails + '"!'));
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
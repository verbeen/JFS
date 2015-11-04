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

        return service;

        function getAllJobs() {
            return $http.get('data/job-list.json')
                .then(handleSuccess, handleError('Error getting all jobs!'));
        }

        function getJobProfile() {
            return $http.get('data/job-profile.json')
                .then(handleSuccess, handleError('Error getting job profile!'));
        }

        // private functions

        function handleSuccess(res) {
            return res.data;
        }

        function handleError(error) {
            return function () {
                return { success: false, message: error };
            };
        }
    }
})();
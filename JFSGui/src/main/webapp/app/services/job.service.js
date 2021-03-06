(function () {
    'use strict';

    angular
        .module('app')
        .factory('JobService', JobServiceFactory);

    JobServiceFactory.$inject = ['$http'];
    function JobServiceFactory($http) {
        var service = {};

        service.getAllJobs = getAllJobs;
        service.getAllJobsCompany = getAllJobsCompany;
        service.getJobProfile = getJobProfile;
        service.getAllJobMetricsByCompany = getAllJobMetricsByCompany;
        service.getRecentJobs = getRecentJobs;
        service.getJobsBySearch = getJobsBySearch;
        service.createJob = createJob;
        service.createJobMulti = createJobMulti;
        service.addSubscription = addSubscription;
        service.checkSubscription = checkSubscription;
        service.getJobsSubs = getJobsSubs;
        service.subsUpdate = subsUpdate;
        service.deleteJobOffer = deleteJobOffer;
        service.createEmptyJobObject = createEmptyJobObject;


        return service;

        function getAllJobMetricsByCompany(token){
            return $http.post('service/offers/metrics/company', token)
                .then(handleSuccess, handleError('Error getting metrics for this user!'))
        }

        function getAllJobs(token) {
            return $http.post('/service/offers/getall', token)
                .then(handleSuccess, handleError('Error getting all jobs!'));

        }

        function getAllJobsCompany(token) {
            return $http.post('/service/offers/getallcompany', token)
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

        function createJobMulti(jobsArray) {
            return $http.post('/service/offers/addmulti', jobsArray)
                .then(handleSuccess, handleError('Error adding job offers by "' + jobsArray + '"!'));
        }

        //function for subscription
        function addSubscription(subDetails) {
            return $http.post('/service/studentsubscriptions/add', subDetails)
                .then(handleSuccess, handleError('Error adding job offers by "' + subDetails + '"!'));
        }

        function checkSubscription(user) {
            return $http.post('/service/studentsubscriptions/get', user)
                .then(handleSuccess, handleError('Error getting subscription "' + user + '"!'));
        }

        //This function is used to get all the
        function getJobsSubs(token) {
            return $http.post('/service/studentsubscriptions/checkSubscriptions', token)
                .then(handleSuccess, handleError('Error getting jobs!'));
        }

        //updating existing subscription details
        function subsUpdate(userDetails) {
            return $http.post('/service/studentsubscriptions/update', userDetails)
                .then(handleSuccess, handleError('Error updating subs "' + userDetails + '"!'));
        }

        function deleteJobOffer(obj){
            return $http.post('/service/offers/delete', obj)
                .then(handleSuccess, handleError('Error deleting the job offer'));
        }

        function createEmptyJobObject(){
            return {
                "jobName": "",
                "type": "",
                "jobDescription": "",
                "jobTask": "",
                "jobSkill": "",
                "validTilldate": "",
                "startDate": "",
                "duration": "",
                "location": { address: "", coordinates: ""},
                "jobWebsite": "",
                "jobContactEmail": ""
            };
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
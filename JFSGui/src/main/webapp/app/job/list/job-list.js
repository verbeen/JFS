(function () {
    'use strict';

    angular
        .module('app')
        .controller('JobListController', JobListController);

    JobListController.$inject = ['JobService', '$scope'];
    function JobListController(JobService, $scope) {
        JobService.getAllJobs()
            .then(function(data) {
                $scope.jobs = data;
            });
    }

})();
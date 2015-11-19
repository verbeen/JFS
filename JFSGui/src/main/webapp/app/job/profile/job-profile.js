(function () {
    'use strict';

    angular
        .module('app')
        .controller('JobProfileController', JobProfileController);

    JobProfileController.$inject = ['JobService', '$scope'];
    function JobProfileController(JobService, $scope) {
        JobService.getJobProfile()
            .then(function(data) {
                $scope.jobProfile = data;
            });
    }

})();
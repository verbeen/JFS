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
                $scope.svalue = true;
                $scope.sampleObjectForFunction = [
                    {"value":"1Gear","label":"1Gear"},
                    {"value":"1Globe","label":"1Globe"},
                    {"value":"1Heart","label":"1Heart"}
                ];
                $scope.sampleObjectForDuration = [
                    {"value":"2Gear","label":"2Gear"},
                    {"value":"2Globe","label":"2Globe"},
                    {"value":"2Heart","label":"2Heart"}
                ];
                $scope.sampleObjectForJobType = [
                    {"value":"3Gear","label":"3Gear"},
                    {"value":"3Globe","label":"3Globe"},
                    {"value":"3Heart","label":"3Heart"}
                ];



            });

        $scope.foo = function() {
            if($scope.svalue==false)
            {
                $scope.svalue=true;
            }
            else{
                $scope.svalue = false;
            }

        };
    }
})();
(function () {
    'use strict';

    angular
        .module('app')
        .controller('AdministrationJobListController', AdministrationJobListController);

    AdministrationJobListController.$inject = ['JobService', '$scope'];
    function AdministrationJobListController(JobService, $scope) {

        $scope.edit = function() {
            console.log("edit()");
        };

        $scope.remove = function() {
            console.log("remove()");
        };

        $scope.getTimes = function(n) {
            return new Array(n);
        };

        $scope.quicksearch = function() {
            console.log("quicksearch()");
        };

        $scope.jobFunction = [
            { "value": "Developer", "label": "Developer" },
            { "value": "Designer", "label": "Designer" },
            { "value": "Project Manager", "label": "Project Manager" }
        ];

        $scope.jobDuration = [
            { "value": "1month", "label": "One month" },
            { "value": "2months", "label": "Two months" },
            { "value": "3months", "label": "Three months" },
            { "value": "6months", "label": "Six months" }
        ];

        $scope.jobLocation = ["Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut",
            "Delaware","Florida","Georgia","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky",
            "Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri",
            "Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico","New York","North Dakota",
            "North Carolina","Ohio","Oklahoma","Oregon","Pennsylvania","Rhode Island","South Carolina",
            "South Dakota","Tennessee","Texas","Utah","Vermont","Virginia","Washington","West Virginia",
            "Wisconsin","Wyoming"];

        /*
        var amount = 5;

        JobService.getRecentJobs(amount)
            .then(function (response) {
                console.debug(response);
            });

        var searchterm = "Apple";

        JobService.getJobsBySearchterm(searchterm)
            .then(function (response) {
               console.debug(response);
            });
        */
    }
})();
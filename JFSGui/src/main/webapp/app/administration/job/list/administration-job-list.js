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

        $scope.getTimes = function(n){
            return new Array(n);
        };

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
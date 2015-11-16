(function () {
    'use strict';

    angular
        .module('app')
        .controller('AdministrationMembersListController', AdministrationMembersListController);


    AdministrationMembersListController.$inject = ['JobService', '$scope'];
    function AdministrationMembersListController(JobService, $scope) {



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
/**
 * Created by Kernal on 12/25/2015.
 */
(function () {
    'use strict';

    angular
        .module('app')
        .controller('CompanyDeleteJobs', CompanyDeleteJobs);

    CompanyDeleteJobs.$inject = ['JobService', '$scope', '$rootScope'];
    function CompanyDeleteJobs(JobService, $scope, $rootScope) {
        // method declarations
        $scope.getAll = getAll;
        $scope.remove = remove;
        $scope.responseMessage = {};


        // gets executed on initial load
        (function initController() {
            getAll();
        })();

        function remove(jobOfferId) {
            var obj = {
                "token": $rootScope.globals.currentUser.authdata,
                "jobOfferId": jobOfferId
            };

            JobService.deleteJobOffer(obj)
                .then(function(response) {
                    $scope.noResults = {};
                    $scope.noResults.info = false;
                    $scope.noResults.error = false;
                    $scope.responseMessage = {};
                    if (response.success) {
                        $scope.responseMessage.success = true;
                        $scope.responseMessage.text = "The job has been deleted successfully.";
                        getAll();
                    }
                    else {
                        console.error(response);
                        $scope.noResults.error = true;
                        $scope.noResults.title = "An error occurred!";
                        $scope.noResults.text = "Please try again later.";
                    }
                });
        };

        function getAll() {
            JobService.getAllJobsCompany($rootScope.globals.currentUser.authdata)
                .then(function(response) {
                    $scope.noResults = {};
                    $scope.noResults.info = false;
                    $scope.noResults.error = false;
                    if (response.success) {
                        if (response.data.offers.length > 0) {
                            $scope.offers = response.data.offers;
                        } else {
                            $scope.offers = [];
                            $scope.noResults.info = true;
                            $scope.noResults.title = "No results!";
                            $scope.noResults.text = "No job offers found. Please change your search parameters.";
                        }
                    } else {
                        console.error(response);
                        $scope.offers = [];
                        $scope.noResults.error = true;
                        $scope.noResults.title = "An error occurred!";
                        $scope.noResults.text = "Please try again later.";
                    }
                });
        };
    }
})();
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
        $scope.edit = edit;
        $scope.getAll = getAll;
        $scope.remove = remove;
        $scope.search = search;
        $scope.userIdNow = userIdNow;
        var userIdNow;

        // gets executed on initial load
        (function initController() {
            $scope.dataLoading = false;

            $scope.jobSearch = {
                "type": [
                    { "value": "", "label": "All" },
                    { "value": "master_thesis", "label": "Master thesis" },
                    { "value": "bachelor_thesis", "label": "Bachelor thesis" },
                    { "value": "part_time", "label": "Part time" },
                    { "value": "full_time", "label": "Full time" },
                    { "value": "internship", "label": "Internship" },
                    { "value": "contract", "label": "Contract" }
                ]
            };
            userIdNow = $rootScope.globals.currentUser.username;
            console.log(userIdNow);
            getAll();
        })();

        function edit() {
            console.log("edit()");
        };

        function remove(jobOfferId) {
            console.log("remove()");
            $scope.jobOfferId = jobOfferId;
            JobService.deleteJobOffer($scope.jobOfferId)
                .then(function(response) {
                    $scope.noResults = {};
                    $scope.noResults.info = false;
                    $scope.noResults.error = false;
                    if (response.success) {
                        $scope.dataLoading = false;
                        getAll();
                    }
                    else {
                        console.error(response);
                        $scope.noResults.error = true;
                        $scope.noResults.title = "An error occurred!";
                        $scope.noResults.text = "Please try again later.";
                        $scope.dataLoading = false;
                    }
                });
        };

        function search() {
            $scope.dataLoading = true;

            if (!$scope.selectedJobSearch
                || $scope.selectedJobSearch.type == ""
                || $scope.selectedJobSearch.location == "") {
                $scope.selectedJobSearch = {};
            }

            JobService.getJobsBySearch($scope.selectedJobSearch)
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
                        $scope.dataLoading = false;
                    } else {
                        console.error(response);
                        $scope.offers = [];
                        $scope.noResults.error = true;
                        $scope.noResults.title = "An error occurred!";
                        $scope.noResults.text = "Please try again later.";
                        $scope.dataLoading = false;
                    }
                });
        };

        function getAll() {
            $scope.dataLoading = true;

            JobService.getAllJobsCompany($rootScope.globals.currentUser.authdata,$rootScope.globals.currentUser.username)
                .then(function(response) {
                    console.log($rootScope.globals.currentUser.authdata);
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
                        $scope.dataLoading = false;
                    } else {
                        console.error(response);
                        $scope.offers = [];
                        $scope.noResults.error = true;
                        $scope.noResults.title = "An error occurred!";
                        $scope.noResults.text = "Please try again later.";
                        $scope.dataLoading = false;
                    }
                });
        };
    }
})();
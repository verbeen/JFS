(function () {
    'use strict';

    angular
        .module('app')
        .controller('AdministrationJobListController', AdministrationJobListController);

    AdministrationJobListController.$inject = ['JobService', '$scope', '$rootScope'];
    function AdministrationJobListController(JobService, $scope, $rootScope) {
        // method declarations
        $scope.edit = edit;
        $scope.getAll = getAll;
        $scope.remove = remove;
        $scope.search = search;

        // gets executed on initial load
        (function initController() {
            $scope.dataLoading = false;

            $scope.jobSearch = {
                "type": [
                    { "value": "all", "label": "All" },
                    { "value": "master_thesis", "label": "Master thesis" },
                    { "value": "bachelor_thesis", "label": "Bachelor thesis" },
                    { "value": "part_time", "label": "Part time" },
                    { "value": "full_time", "label": "Full time" },
                    { "value": "internship", "label": "Internship" },
                    { "value": "contract", "label": "Contract" },
                    { "value": "others", "label": "Others" }
                ]
            };

            getAll();
        })();

        function edit() {
            console.log("edit()");
        };

        function remove(jobOfferId) {
            var obj = {
                "token": $rootScope.globals.currentUser.authdata,
                "jobOfferId": jobOfferId
            };

            JobService.deleteJobOffer(obj)
                .then(function(response) {
                    $scope.responseMessage = {};
                    if (response.success) {
                        $scope.responseMessage.success = true;
                        $scope.responseMessage.text = "The job offer has been deleted successfully.";
                        getAll();
                    }
                    else {
                        console.error(response);
                        $scope.responseMessage.error = true;
                        $scope.responseMessage.title = "An error occurred!";
                        $scope.responseMessage.text = "Please try again later.";
                    }
                });
        };

        function search() {
            $scope.dataLoading = true;

            if (!$scope.selectedJobSearch) {
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

            JobService.getAllJobs($rootScope.globals.currentUser.authdata)
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
    }
})();
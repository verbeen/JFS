(function () {
    'use strict';

    angular
        .module('app')
        .controller('JobListController', JobListController);

    JobListController.$inject = ['JobService', '$scope'];
    function JobListController(JobService, $scope) {
        // method declarations
        $scope.search = search;
        $scope.getRecentJobs = getRecentJobs;

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
                    { "value": "contract", "label": "Contract" },
                    { "value": "others", "label": "Others" }
                ]
            };

            getRecentJobs();
        })();

        function getRecentJobs() {
            $scope.dataLoading = true;

            JobService.getRecentJobs(30)
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

        /*
        $scope.jobFilter = {
            "type": [
                { "value": "1", "label": "Part Time" },
                { "value": "2", "label": "Full Time" },
                { "value": "3", "label": "Master Thesis" }
            ],
            "salary": [
                { "value": "s1", "label": "Less than 20" },
                { "value": "s2", "label": "Between 20 and 40" },
                { "value": "s3", "label": "More than 40" }
            ],
            "duration": [
                { "value": "d1", "label": "Less than 6 month" },
                { "value": "d2", "label": "Between 6 and 11 months" },
                { "value": "d3", "label": "More than one year" }
            ],
            "company": ["Accenture","IBM","Google"],
            "skill": ["CSS","Java","Angular JS"]
        };
        */
    }
})();
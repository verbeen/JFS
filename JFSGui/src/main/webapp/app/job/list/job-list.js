(function () {
    'use strict';

    angular
        .module('app')
        .controller('JobListController', JobListController);

    JobListController.$inject = ['JobService', '$scope'];
    function JobListController(JobService, $scope) {
        var vm = this;

        vm.dataLoading = false;

        JobService.getRecentJobs(20)
            .then(function(response) {
                if (response.success) {
                    $scope.offers = response.data.offers;
                } else {
                    console.error(response);
                }
            });

        vm.search = search;

        function search() {
            vm.dataLoading = true;

            if (!vm.selectedJobSearch) {
                vm.selectedJobSearch = {};
            }

            JobService.getJobsBySearch(vm.selectedJobSearch)
                .then(function(response) {
                    vm.noResults = {};
                    vm.noResults.info = false;
                    vm.noResults.error = false;
                    if (response.success) {
                        if (response.data.offers.length > 0) {
                            $scope.offers = response.data.offers;
                        } else {
                            $scope.offers = [];
                            vm.noResults.info = true;
                            vm.noResults.title = "No results!";
                            vm.noResults.text = "No job offers found. Please change your search parameters.";
                        }
                        vm.dataLoading = false;
                    } else {
                        console.error(response);
                        $scope.offers = [];
                        vm.noResults.error = true;
                        vm.noResults.title = "An error occurred!";
                        vm.noResults.text = "Please try again later.";
                        vm.dataLoading = false;
                    }
                });
        };

        $scope.jobSearch = {
            "type": [
                { "value": "master_thesis", "label": "Master thesis" },
                { "value": "bachelor_thesis", "label": "Bachelor thesis" },
                { "value": "part_time", "label": "Part time" },
                { "value": "full_time", "label": "Full time" },
                { "value": "internship", "label": "Internship" },
                { "value": "contract", "label": "Contract" }
            ]
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
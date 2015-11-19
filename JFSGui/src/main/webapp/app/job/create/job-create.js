(function () {
    'use strict';

    angular
        .module('app')
        .controller('JobCreateController', JobCreateController);

    JobCreateController.$inject = ['JobService', '$scope', '$rootScope'];
    function JobCreateController(JobService, $scope, $rootScope) {
        var vm = this;

        vm.jobProfileParams = {
            "type": [
                { "value": "master_thesis", "label": "Master thesis" },
                { "value": "bachelor_thesis", "label": "Bachelor thesis" },
                { "value": "part_time", "label": "Part time" },
                { "value": "full_time", "label": "Full time" },
                { "value": "internship", "label": "Internship" },
                { "value": "contract", "label": "Contract" }
            ]
        };

        vm.initializeNewJobOffer = initializeNewJobOffer;
        vm.create = create;
        vm.reset = reset;

        // gets executed on initial load
        (function initController() {
            // reset job create view
            vm.initializeNewJobOffer();
        })();

        function initializeNewJobOffer() {
            vm.responseMessage = {};
            vm.responseMessage.showForm = true;
            vm.responseMessage.success = false;
            vm.responseMessage.error = false;
            vm.reset();
        }

        function create() {
            vm.dataLoading = true;

            $scope.$broadcast('show-errors-check-validity');

            if ($scope.formCreateJob.$invalid) {
                vm.dataLoading = false;
                return;
            }

            var obj = {
                "companyId": $rootScope.globals.currentUser.username,
                "token": $rootScope.globals.currentUser.authdata,
                "jobOffer": {
                    "offerId":"",
                    "companyId": $rootScope.globals.currentUser.username,
                    "name": vm.jobProfile.jobName,
                    "function": "",
                    "description": vm.jobProfile.jobDescription,
                    "task": vm.jobProfile.jobTask,
                    "posted": "",
                    "duration": "",
                    "validUntil": Date.parse(vm.jobProfile.durationTo),
                    "startDate": Date.parse(vm.jobProfile.durationFrom),
                    "location": vm.jobProfile.location,
                    "website": "",
                    "type": vm.jobProfile.type
                }
            };

            JobService.createJob(obj)
                .then(function (response) {
                    vm.responseMessage.showForm = false;
                    if (response.success) {
                        console.info("Job offer created!");
                        vm.responseMessage.success = true;
                        vm.responseMessage.text = "Your job offer has been successfully created.";
                        vm.dataLoading = false;
                    } else {
                        // backend service is not reachable (e.g. database down)
                        console.error(response.message);
                        vm.responseMessage.error = true;
                        vm.responseMessage.text = "An error occurred while creating your job offer.";
                        vm.dataLoading = false;
                    }
                });
        }

        function reset() {
            vm.jobProfile = {
                "jobName": "",
                "type": "",
                "jobDescription": "",
                "jobTask": "",
                "validTilldate": "",
                "durationFrom": "",
                "durationTo": "",
                "location": "",
                "address": "",
                "contactPerson": "",
                "email": ""
            };
            $scope.$broadcast('show-errors-reset');
        }
    }
})();
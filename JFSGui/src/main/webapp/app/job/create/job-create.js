(function () {
    'use strict';

    angular
        .module('app')
        .controller('JobCreateController', JobCreateController);

    JobCreateController.$inject = ['JobService', '$scope', '$rootScope'];
    function JobCreateController(JobService, $scope, $rootScope) {
        $scope.jobProfileParams = {
            "type": [
                { "value": "master_thesis", "label": "Master thesis" },
                { "value": "bachelor_thesis", "label": "Bachelor thesis" },
                { "value": "part_time", "label": "Part time" },
                { "value": "full_time", "label": "Full time" },
                { "value": "internship", "label": "Internship" },
                { "value": "contract", "label": "Contract" }
            ],
            "duration": [
                { "value": (1 * 30 * 24 * 3600 * 1000), "label": "1 month" },
                { "value": (2 * 30 * 24 * 3600 * 1000), "label": "2 months" },
                { "value": (3 * 30 * 24 * 3600 * 1000), "label": "3 months" },
                { "value": (4 * 30 * 24 * 3600 * 1000), "label": "4 months" },
                { "value": (5 * 30 * 24 * 3600 * 1000), "label": "5 months" },
                { "value": (6 * 30 * 24 * 3600 * 1000), "label": "6 months" },
                { "value": (7 * 30 * 24 * 3600 * 1000), "label": "7 months" },
                { "value": (8 * 30 * 24 * 3600 * 1000), "label": "8 months" },
                { "value": (9 * 30 * 24 * 3600 * 1000), "label": "9 months" },
                { "value": (10 * 30 * 24 * 3600 * 1000), "label": "10 months" },
                { "value": (11 * 30 * 24 * 3600 * 1000), "label": "11 months" },
                { "value": (12 * 30 * 24 * 3600 * 1000), "label": "12 months" },
                { "value": 0, "label": "infinite" }
            ]
        };

        $scope.initializeNewJobOffer = initializeNewJobOffer;
        $scope.create = create;
        $scope.reset = reset;

        // gets executed on initial load
        (function initController() {
            // reset job create view
            initializeNewJobOffer();
        })();

        function initializeNewJobOffer() {
            $scope.responseMessage = {};
            $scope.responseMessage.showForm = true;
            $scope.responseMessage.success = false;
            $scope.responseMessage.error = false;
            $scope.reset();
        }

        function create() {
            $scope.dataLoading = true;

            $scope.$broadcast('show-errors-check-validity');

            if ($scope.formCreateJob.$invalid) {
                $scope.dataLoading = false;
                return;
            }

            var obj = {
                "companyId": $rootScope.globals.currentUser.username,
                "token": $rootScope.globals.currentUser.authdata,
                "jobOffer": {
                    "companyId": $rootScope.globals.currentUser.username,
                    "name": $scope.jobProfile.jobName,
                    "function": "",
                    "description": $scope.jobProfile.jobDescription,
                    "task": $scope.jobProfile.jobTask,
                    "duration": $scope.jobProfile.duration,
                    "validUntil": Date.parse($scope.jobProfile.validTilldate),
                    "startDate": Date.parse($scope.jobProfile.startDate),
                    "address": $scope.jobProfile.address,
                    "website": $scope.jobProfile.jobWebsite,
                    "contactEmail": $scope.jobProfile.jobContactEmail,
                    "type": $scope.jobProfile.type
                }
            };

            JobService.createJob(obj)
                .then(function (response) {
                    $scope.responseMessage.showForm = false;
                    if (response.success) {
                        console.info("Job offer created!");
                        $scope.responseMessage.success = true;
                        $scope.responseMessage.text = "Your job offer has been successfully created.";
                        $scope.dataLoading = false;
                    } else {
                        // backend service is not reachable (e.g. database down)
                        console.error(response.message);
                        $scope.responseMessage.error = true;
                        $scope.responseMessage.text = "An error occurred while creating your job offer.";
                        $scope.dataLoading = false;
                    }
                });
        }

        function reset() {
            $scope.jobProfile = {
                "jobName": "",
                "type": "",
                "jobDescription": "",
                "jobTask": "",
                "validTilldate": "",
                "startDate": "",
                "duration": "",
                "location": "",
                "jobWebsite": "",
                "jobContactEmail": ""
            };
            $scope.$broadcast('show-errors-reset');
        }
    }
})();
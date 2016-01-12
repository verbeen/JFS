(function () {
    'use strict';

    angular
        .module('app')
        .controller('AddNotificationController', AddNotificationController);

    AddNotificationController.$inject = ['JobService', '$scope', '$http', '$rootScope'];
    function AddNotificationController(JobService, $scope, $http, $rootScope) {
        //Declaring functions for the page
        $scope.checkSub = checkSub;
        $scope.createSub = createSub;
        $scope.subsUpdate = subsUpdate;
        $scope.skills = '';
        $scope.flag = 0;
        $scope.responseMessage = "";
        $scope.responseStatus = true;
        $scope.responseMessageShow = false;
        $scope.saveSub = saveSub;
        $scope.jobSearch = {
            "type": [
                {"value": "all", "label": "All"},
                {"value": "master_thesis", "label": "Master thesis"},
                {"value": "bachelor_thesis", "label": "Bachelor thesis"},
                {"value": "part_time", "label": "Part time"},
                {"value": "full_time", "label": "Full time"},
                {"value": "internship", "label": "Internship"},
                {"value": "contract", "label": "Contract"},
                {"value": "others", "label": "Others"}
            ]
        };
        $scope.type = "all";
        $scope.checkSub();
        //Setting redirection navigation for main page
        $scope.sample = "sample";
        $scope.redirect = function () {
            window.location = "#/student";
        }

        function saveSub() {
            $scope.$watch('$scope.flag', function () {
                if ($scope.flag > 0) {
                    subsUpdate();
                } else {
                    createSub();
                }
            });
        }

        function createSub() {
            //Getting current time and converting it to milliseconds
            var t = new Date();
            var time = new Date(t).getTime();
            $scope.time = time;

            //Creating DTO
            var obj = {
                "userId": $rootScope.globals.currentUser.username,
                "type": $scope.type,
                "location": $scope.location,
                "skills": $scope.skills,
                "lastView": $scope.time


            };
            //Adding a new subscription
            JobService.addSubscription(obj)
                .then(function (response) {
                    if (response.success) {
                        $scope.responseMessage = "Subscription Added Successfully !";
                        $scope.responseMessageShow = true;
                    } else {
                        // backend service is not reachable (e.g. database down)
                        $scope.responseMessage.error = true;
                        $scope.responseMessage.text = "An error occurred while creating your subscription.";
                        $scope.dataLoading = false;
                        console.info("failed!");
                    }
                });
        }

        //updating existing subscriptions
        function subsUpdate() {
            //Getting current time and converting it to milliseconds
            var t = new Date();
            var time = new Date(t).getTime();
            $scope.time = time;
            //Creating DTO
            var obj = {
                "userId": $rootScope.globals.currentUser.username,
                "type": $scope.type,
                "location": $scope.location,
                "skills": $scope.skills,
                "lastView": $scope.time
            };
            //Adding a new subscription
            JobService.subsUpdate(obj)
                .then(function (response) {
                    if (response.success) {
                        $scope.responseMessage = "Subscription Updated Successfully!";
                        $scope.responseMessageShow = true;
                    } else {
                        // backend service is not reachable (e.g. database down)
                        console.error(response.message);
                    }
                });
        }

        //retrieving current subscription details and settings default values
        function checkSub() {
            var user = $rootScope.globals.currentUser.username;
            JobService.checkSubscription($rootScope.globals.currentUser.username)
                .then(function (response) {
                    if (response.success) {
                        $scope.subDetails = response.data;
                        $scope.flag = 1;
                        $scope.location = response.data.location;
                        $scope.skills = response.data.skills;
                        //Setting default values for the dropdown values
                        if (response.data.type) {
                            $scope.type = response.data.type;
                        }

                       /* if (response.data.type == "master_thesis") {
                            $scope.type = "master_thesis";
                        } else if (response.data.type == "bachelor_thesis") {
                            $scope.type = "bachelor_thesis";
                        } else if (response.data.type == "part_time") {
                            $scope.type = "part_time";
                        } else if (response.data.type == "full_time") {
                            $scope.type = "full_time";
                        } else if (response.data.type == "internship") {
                            $scope.type = "internship";
                        } else if (response.data.type == "contract") {
                            $scope.type = "contract";
                        }*/
                    } else {
                        // backend service is not reachable (e.g. database down)

                        console.error(response.message);
                        console.info("failed!No data retrieved");
                    }
                });
        };

    }


})();


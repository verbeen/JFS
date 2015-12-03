(function () {
    'use strict';

    angular
        .module('app')
        .controller('AddNotificationController', AddNotificationController);

    AddNotificationController.$inject = ['JobService','$scope','$http','$rootScope'];
    function AddNotificationController(JobService,$scope,$http,$rootScope) {
        //Declaring functions for the page
        $scope.checkSub = checkSub;
        $scope.createSub = createSub;
        $scope.skills = '';
        $scope.jobSearch = {
            "type": [
                {"value": "", "label": "All"},
                {"value": "master_thesis", "label": "Master thesis"},
                {"value": "bachelor_thesis", "label": "Bachelor thesis"},
                {"value": "part_time", "label": "Part time"},
                {"value": "full_time", "label": "Full time"},
                {"value": "internship", "label": "Internship"},
                {"value": "contract", "label": "Contract"}
            ]
        };
        $scope.checkSub();
        //Setting redirection navigation for main page
        $scope.sample = "sample";
        $scope.redirect = function () {
            window.location = "#/student";
        }
        function createSub()
        {
            //Getting current time and converting it to milliseconds
            var t = new Date();
            var time = new Date(t).getTime();
            $scope.time = time;

            //Creating DTO
            var obj = {
                "userId": $rootScope.globals.currentUser.username,
                "types": $scope.types,
                "location": $scope.location,
                "skills": $scope.skills,
                "lastView": $scope.time


            };
            //Adding a new subscription
            JobService.addSubscription(obj)
                .then(function (response) {
                    console.info(obj);
                    if (response.success) {
                        console.info("subscription added sucessfully!");
                    } else {
                        // backend service is not reachable (e.g. database down)
                        console.error(response.message);
                        $scope.responseMessage.error = true;
                        $scope.responseMessage.text = "An error occurred while creating your subscription.";
                        $scope.dataLoading = false;
                        console.info("failed!");
                    }
                });
        }
        //retrieving current subscription details and settings default values
        function checkSub() {
            var user = $rootScope.globals.currentUser.username;
            JobService.checkSubscription($rootScope.globals.currentUser.username)
                .then(function(response) {
                    console.info("inside checkSub function!");
                    console.info(response.data);
                    if (response.success) {
                        $scope.subDetails = response.data;
                        console.info("got data sucessfully!");
                        $scope.location = response.data.location;
                        $scope.skills = response.data.skills;
                        //Setting default values for the dropdown values
                        if(response.data.types == "master_thesis"){
                            $scope.types = "master_thesis";
                        }else if(response.data.types == "bachelor_thesis"){
                            onsole.log("bachelor");
                            $scope.types = "bachelor_thesis";
                        }else if(response.data.types == "part_time"){
                            onsole.log("part_time");
                            $scope.types = "part_time";
                        }else if(response.data.types == "full_time"){
                            onsole.log("full_time");
                            $scope.types = "full_time";
                        }else if(response.data.types == "internship"){
                            onsole.log("internship");
                            $scope.types = "internship";
                        }else if(response.data.types == "contract"){
                            onsole.log("contract");
                            $scope.types = "contract";
                        }
                    } else {
                        // backend service is not reachable (e.g. database down)
                        console.error(response.message);
                        console.info("failed!No data retrieved");
                    }
                });
        };

    }


})();


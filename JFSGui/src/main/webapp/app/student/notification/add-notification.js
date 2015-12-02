(function () {
    'use strict';

    angular
        .module('app')
        .controller('AddNotificationController', AddNotificationController);

    AddNotificationController.$inject = ['JobService','$scope','$http','$rootScope'];
    function AddNotificationController(JobService,$scope,$http,$rootScope) {
        $scope.create = createSub;
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



        $scope.sample = "sample";
        $scope.redirect = function () {
            window.location = "#/student";
        }
        function createSub()
        {
            var t = new Date();
            var time=new Date(t).getTime();
            $scope.time=time;
            console.log($scope.time);

            var obj = {
                "userId": $rootScope.globals.currentUser.username,
                "types": $scope.types,
                "location": $scope.location,
                "skills": $scope.skills,
                "lastView": $scope.time


            };
            JobService.addSubscription(obj)
                .then(function (response) {
                    console.info("inside subscriptionfunction!");
                    console.info(obj);

                 //   $scope.responseMessage.showForm = false;
                    if (response.success) {
                        console.info("subscription added sucessfully!");
                       /* $scope.responseMessage.success = true;
                        $scope.responseMessage.text = "subscription added sucessfully!";
                        $scope.dataLoading = false;
                        */
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
    }


})();


/**
 * Created by Kernal on 11/27/2015.
 */
(function () {
    'use strict';

angular
        .module('app')
        .controller('StudentLandingController',StudentLandingController);

        StudentLandingController.$inject = ['JobService','$scope','$http','$rootScope'];

        function StudentLandingController(JobService,$scope,$http,$rootScope) {
            // method declarations
            $scope.checkSub = checkSub;
            $scope.getJobsSubs = getJobsSubs;
            // gets executed on initial load
            (function initController() {
                $scope.dataLoading = false;
             //   getRecentJobs();
            })();

            function getRecentJobs() {
                $scope.dataLoading = true;

                JobService.getRecentJobs(2)
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
            //Function for getting the newly added jobs
            function getJobsSubs() {
                $scope.dataLoading = true;
                var t = new Date();
                var time = new Date(t).getTime();
                $scope.time = time;

                //Creating DTO
                var obj = {
                    "userId": $rootScope.globals.currentUser.username,
                    /*
                        "types": $scope.types,
                    "location": $scope.location,
                    "skills": $scope.skills,

                    "types": "master_thesis",
                    "location": "Trivandrum",
                    "skills": "Android",
                    "lastView": $scope.time*/


                };

                JobService.getJobsSubs(obj)
                    .then(function(response) {
                        console.log("Printing object-");
                        console.log(obj);
                        $scope.noResults = {};
                        $scope.noResults.info = false;
                        $scope.noResults.error = false;
                        if (response.success) {
                            if (response.data.offers.length > 0) {
                                $scope.offers = response.data.offers;
                                console.log(response.data);
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



            function checkSub() {
                var user = $rootScope.globals.currentUser.username;
                console.log(user);
                JobService.checkSubscription($rootScope.globals.currentUser.username)
                    .then(function(response) {
                        console.info("inside checkSub function!");
                        console.info(response.data);
                        if (response.success) {
                            $scope.subDetails=response.data;
                            console.info("got data sucessfully!");
                            /* $scope.responseMessage.success = true;
                             $scope.responseMessage.text = "subscription added sucessfully!";
                             $scope.dataLoading = false;
                             */
                        } else {
                            // backend service is not reachable (e.g. database down)
                            console.error(response.message);
                            console.info("failed!No data retrieved");
                        }
                    });
            };


        }


})();

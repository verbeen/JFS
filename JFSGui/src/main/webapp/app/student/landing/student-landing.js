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

            (function initController() {
                $scope.dataLoading = false;
            })();
            checkSub();

            //Function for getting the newly added jobs
            function getJobsSubs() {
                $scope.dataLoading = true;
                var t = new Date();
                var time = new Date(t).getTime();
                $scope.time = time;
                JobService.getJobsSubs($rootScope.globals.currentUser.authdata)
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
                                $scope.noResults.title = "No new Offers!";
                                $scope.noResults.text = "No job offers according to your notification settings.";
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
                JobService.checkSubscription($rootScope.globals.currentUser.authdata)
                    .then(function(response) {
                        $scope.noResults = {};
                        $scope.noResults.info = false;
                        $scope.noResults.error = false;
                        if (response.success && response.data != null && response.data != "") {
                            $scope.subDetails=response.data;
                            getJobsSubs();
                        } else {
                            // backend service is not reachable (e.g. database down)
                            console.info("failed!No data retrieved");
                            //$scope.noResults.error = true;
                            $scope.noResults.info = true;
                            $scope.noResults.title = "Please setup the notification!";
                            $scope.noResults.text = "Please setup the notification settings for getting recent " +
                                "job updates.";
                            $scope.dataLoading = false;
                        }
                    });
            };
        }
})();

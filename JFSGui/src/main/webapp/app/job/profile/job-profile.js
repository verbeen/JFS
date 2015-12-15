(function () {
    'use strict';

    angular
        .module('app')
        .controller('JobProfileController', JobProfileController);

    JobProfileController.$inject = ['$scope', '$rootScope', '$routeParams', 'JobService'];
    function JobProfileController($scope, $rootScope, $routeParams, JobService) {
        $scope.offerId = $routeParams.offerId;
        $scope.userType = $rootScope.globals.currentUser.userType;
        if ($scope.userType == 'STUDENT') {
            $scope.showApplyBtn = true;
        } else {
            $scope.showApplyBtn = false;
        }

        JobService.getJobProfile($routeParams.offerId)
            .then(function(response) {
                $scope.noResults = {};
                $scope.noResults.info = false;
                $scope.noResults.error = false;

                if (response.success) {
                    if (response.data) {
                        $scope.jobOffer = response.data;
                    } else {
                        $scope.jobOffer = {};
                        $scope.noResults.info = true;
                        $scope.noResults.title = "No results!";
                        $scope.noResults.text = "No job offer found.";
                    }
                    $scope.dataLoading = false;
                } else {
                    console.error(response);
                    $scope.jobOffer = {};
                    $scope.noResults.error = true;
                    $scope.noResults.title = "An error occurred!";
                    $scope.noResults.text = "Please try again later.";
                    $scope.dataLoading = false;
                }
            });
    }
})();
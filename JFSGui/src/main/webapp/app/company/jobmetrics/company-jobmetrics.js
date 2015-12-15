(function () {
    'use strict';

    angular
        .module('app')
        .controller('CompanyJobMetricsController', CompanyJobMetricsController);

    CompanyJobMetricsController.$inject = ['JobService', '$scope', '$rootScope'];
    function CompanyJobMetricsController(JobService, $scope, $rootScope) {
        // method declarations
        $scope.getAllMetrics = getAllMetrics;
        $scope.metrics = [];

        // gets executed on initial load
        (function initController() {
            $scope.dataLoading = false;
            getAllMetrics();
        })();

        function getAllMetrics() {
            $scope.dataLoading = true;

            JobService.getAllJobMetricsByCompany($rootScope.globals.currentUser.authdata)
                .then(function(response) {
                    $scope.noResults = {};
                    $scope.noResults.info = false;
                    $scope.noResults.error = false;
                    if (response.success) {
                        if (response.data.length > 0) {
                            $scope.metrics = response.data;
                        } else {
                            $scope.metrics = [];
                            $scope.noResults.info = true;
                            $scope.noResults.title = "No results!";
                            $scope.noResults.text = "No job offers found. Please change your search parameters.";
                        }
                        $scope.dataLoading = false;
                    } else {
                        console.error(response);
                        $scope.metrics = [];
                        $scope.noResults.error = true;
                        $scope.noResults.title = "An error occurred!";
                        $scope.noResults.text = "Please try again later.";
                        $scope.dataLoading = false;
                    }
                });
        };
    }
})();
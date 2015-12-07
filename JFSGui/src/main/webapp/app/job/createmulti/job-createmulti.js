(function () {
    'use strict';

    angular
        .module('app')
        .controller('JobCreateMultiController', JobCreateMultiController);

    JobCreateMultiController.$inject = ['JobService', '$scope', '$rootScope'];
    function JobCreateMultiController(JobService, $scope, $rootScope) {
        $scope.initializeNewJobOffers = initializeNewJobOffers;
        $scope.create = create;
        $scope.jobOffers = [];

        // gets executed on initial load
        (function initController() {
            // reset job create view
            initializeNewJobOffers();
        })();

        function initializeNewJobOffers() {
            $scope.responseMessage = {};
            $scope.responseMessage.showForm = true;
            $scope.responseMessage.success = false;
            $scope.responseMessage.error = false;
            $scope.jobOffers = [];
        }

        function create() {
            $scope.dataLoading = true;

            $scope.$broadcast('show-errors-check-validity');

            if ($scope.formCreateJobMulti.$invalid) {
                $scope.responseMessage.error = true;
                $scope.responseMessage.text = "An error occurred while creating your job offer.";
                $scope.dataLoading = false;
                return;
            }

            var obj = {
                "companyId": $rootScope.globals.currentUser.username,
                "token": $rootScope.globals.currentUser.authdata,
                "jobOffers": $scope.jobOffers
            };

            JobService.createJobMulti(obj)
                .then(function (response) {
                    $scope.responseMessage = {};
                    $scope.responseMessage.showForm = false;
                    if (response.success) {
                        console.info("Job offer created!");
                        $scope.responseMessage.success = true;
                        $scope.responseMessage.text = "Your job offers have been successfully added.";
                        $scope.dataLoading = false;
                    } else {
                        // backend service is not reachable (e.g. database down)
                        console.error(response.message);
                        $scope.responseMessage.error = true;
                        $scope.responseMessage.text = "An error occurred while adding your job offers.";
                        $scope.dataLoading = false;
                    }
                });
        }

        function handleFileSelect(evt) {
            var file = evt.target.files[0];

            Papa.parse(file, {
                header: true,
                dynamicTyping: true,
                delimiter:",",
                skipEmptyLines: true,
                complete: function(results) {
                    $scope.jobOffers = results.data;
                }
            });
        }

        $(document).ready(function(){
            $("#jobOffers").change(handleFileSelect);
        });
    }
})();
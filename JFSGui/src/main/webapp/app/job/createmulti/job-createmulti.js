(function () {
    'use strict';

    angular
        .module('app')
        .controller('JobCreateMultiController', JobCreateMultiController);

    JobCreateMultiController.$inject = ['JobService', '$scope', '$rootScope'];
    function JobCreateMultiController(JobService, $scope, $rootScope) {
        $scope.initializeNewJobOffers = initializeNewJobOffers;
        $scope.parseFiles = parseFiles;
        $scope.jobOfferChooser = "";
        $scope.create = create;
        $scope.jobOffers = [];
        $scope.files = [];

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
            $scope.files = [];
            $scope.jobOffers = [];
        }

        function create() {
            $scope.dataLoading = true;

            $scope.$broadcast('show-errors-check-validity');

            if ($scope.jobOffers.length == 0) {
                $scope.responseMessage.error = true;
                $scope.responseMessage.text = "No job offers loaded. Please select files and click parse.";
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
                        console.info("Job offers uploaded!");
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

        function fileSelected(evt){
            $scope.files = evt.target.files;
        }
        $(document).ready(function(){
            $("#jobOfferChooser").change(fileSelected);
        });

        function parseFiles() {
            $scope.jobOffers = [];

            for (var i = 0; i < $scope.files.length; i++){
                var file = $scope.files[i];
                Papa.parse(file, {
                    header: true,
                    dynamicTyping: true,
                    delimiter:",",
                    skipEmptyLines: true,
                    complete: function(results) {
                        if(results.errors.length == 0) {
                            $scope.$apply(function () {
                                $scope.jobOffers = $scope.jobOffers.concat(results.data);
                            });
                        }
                    }
                });
            }
        }
    }
})();
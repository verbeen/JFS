(function () {
    'use strict';

    angular
        .module('app')
        .controller('JobProfileController', JobProfileController);

    JobProfileController.$inject = ['$scope', '$rootScope', '$routeParams', 'JobService', 'GeoService'];
    function JobProfileController($scope, $rootScope, $routeParams, JobService, GeoService) {
        $scope.offerId = $routeParams.offerId;
        $scope.userType = $rootScope.globals.currentUser.userType;

        if ($scope.userType == 'STUDENT') {
            $scope.showApplyBtn = true;
        } else {
            $scope.showApplyBtn = false;
        }

        $scope.setMap = function(){
            $scope.map = GeoService.createMap('jobProfileMap', null);
            var icon = GeoService.createGreenIcon();
            var marker = L.marker([$scope.jobOffer.lat, $scope.jobOffer.lng], {icon: icon});
            marker.bindPopup($scope.jobOffer.address);
            $scope.map.addLayer(marker);
            $scope.map.panTo(marker.getLatLng(), {animate: true});
            marker.openPopup();
        };

        JobService.getJobProfile($routeParams.offerId).then(function(response) {
                $scope.noResults = {};
                $scope.noResults.info = false;
                $scope.noResults.error = false;

                if (response.success) {
                    if (response.data) {
                        $scope.jobOffer = response.data;
                        $scope.setMap();
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
(function () {
    'use strict';

    angular
        .module('app')
        .controller('JobListController', JobListController);

    JobListController.$inject = ['JobService', 'GeoService', '$scope'];
    function JobListController(JobService, GeoService, $scope) {
        // method declarations
        $scope.search = search;
        $scope.getRecentJobs = getRecentJobs;
        $scope.mapClicked = mapClicked;
        $scope.radiusChanged = radiusChanged;

        // gets executed on initial load
        (function initController() {
            $scope.dataLoading = false;
            $scope.errorSetMarker = false;

            $scope.jobSearch = {
                "type": [
                    { "value": "all", "label": "All" },
                    { "value": "master_thesis", "label": "Master thesis" },
                    { "value": "bachelor_thesis", "label": "Bachelor thesis" },
                    { "value": "part_time", "label": "Part time" },
                    { "value": "full_time", "label": "Full time" },
                    { "value": "internship", "label": "Internship" },
                    { "value": "contract", "label": "Contract" },
                    { "value": "others", "label": "Others" }
                ],
                "radius": [
                    { "value": 0, "label": "All" },
                    { "value": 1, "label": "1km" },
                    { "value": 2, "label": "2km" },
                    { "value": 3, "label": "3km" },
                    { "value": 5, "label": "5km" },
                    { "value": 10, "label": "10km" },
                    { "value": 20, "label": "20km" },
                    { "value": 30, "label": "30km" },
                    { "value": 50, "label": "50km" },
                    { "value": 100, "label": "100km" },
                    { "value": 200, "label": "200km" }
                ]
            };

            $scope.selectedJobSearch = {};

            $scope.map = GeoService.createMap('jobSearchMap', $scope.mapClicked);

            var clearLocationButton = L.easyButton('<span class="glyphicon glyphicon-remove" />', function(btn, map){
                clearMapMarker();
            });
            clearLocationButton.button.title = 'Clear selected coordinates.';
            $scope.map.addControl(clearLocationButton);

            $scope.mapMarkerLayer = new L.FeatureGroup();
            $scope.mapMarker = null;
            $scope.map.addLayer($scope.mapMarkerLayer);
            $scope.mapMarkerCircleLayer = new L.FeatureGroup();
            $scope.mapMarkerCircle = null;
            $scope.map.addLayer($scope.mapMarkerCircleLayer);

            $scope.mapOfferLayer = L.markerClusterGroup();
            $scope.map.addLayer($scope.mapOfferLayer);

            getRecentJobs();
        })();

        $scope.dropdownLocationSelected = function(location){
            $scope.searchAddress = location.formatted_address;
            setMapMarker(location.geometry.location, location.formatted_address);
        };

        function mapClicked(e){
            var lat = parseFloat(e.latlng.lat.toFixed(6));
            var lng = parseFloat(e.latlng.lng.toFixed(6));
            setMapMarker(e.latlng, "[" + lat + "," + lng + "]");
        };

        function setMapMarker(latlng, content){
            clearMapMarker();

            $scope.selectedJobSearch.coordinates = [latlng.lat, latlng.lng];

            var icon = GeoService.createGreenIcon();
            $scope.mapMarker = L.marker(latlng, {icon: icon});
            if(content != null){
                $scope.mapMarker.bindPopup(content);
            }

            $scope.mapMarkerLayer.addLayer($scope.mapMarker);

            var radius = $scope.selectedJobSearch.radius;
            if(radius != null && radius != 0){
                setMapMarkerCircle(latlng, radius);
            }

            $scope.map.panTo($scope.mapMarker.getLatLng(), {animate: true});
        }

        function setMapMarkerCircle(latlng, radius){
            $scope.mapMarkerCircle = L.circle(latlng, radius * 1000);
            $scope.mapMarkerCircle.options.stroke = false;
            $scope.mapMarkerCircle.options.clickable = false;
            $scope.mapMarkerCircleLayer.addLayer($scope.mapMarkerCircle);
        }

        function clearMapMarker(){
            $scope.mapMarkerLayer.clearLayers();
            $scope.mapMarker = null;
            $scope.mapMarkerCircleLayer.clearLayers();
            $scope.mapMarkerCircle = null;
            $scope.selectedJobSearch.coordinates = null;
        }

        $scope.$watch('searchAddress', function(){
            var address = $scope.searchAddress;
            if(address != null && address != ""){
                GeoService.getLocations(address).then(function(response){
                    var results = response.data.results;
                    setSuggestedDropdownLocations(response.data.results);
                });
            }else{
                setSuggestedDropdownLocations(null);
            }
        });

        function radiusChanged(){
            if($scope.mapMarker != null){
                var latlng = $scope.mapMarker.getLatLng();
                $scope.mapMarkerCircleLayer.clearLayers();
                $scope.mapMarkerCircle = null;

                setMapMarkerCircle(latlng, $scope.selectedJobSearch.radius);
            }
        }

        function setSuggestedDropdownLocations(locations){
            if(locations != null && locations.length > 0){
                $scope.dropdownSearchLocations = locations;
            }else{
                $scope.dropdownSearchLocations = "";
            }
        }

        function offersUpdated(){
            clearMapOfferLayer();

            for(var index = 0; index < $scope.offers.length; index++){
                var offer = $scope.offers[index];
                var icon = GeoService.createVioletIcon();
                var marker = L.marker([offer.lat, offer.lng], { icon: icon });

                var html = "<a href='#/job/profile/" + offer.offerId + "'>";
                html += offer.name;
                html += "</a>";
                marker.bindPopup(html);

                $scope.mapOfferLayer.addLayer(marker);
            }
        }

        function clearMapOfferLayer(){
            $scope.mapOfferLayer.clearLayers();
        }

        function getRecentJobs() {
            $scope.dataLoading = true;

            JobService.getRecentJobs(30)
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
                    offersUpdated();
                });
        };

        function search() {
            $scope.dataLoading = true;

            if (!$scope.selectedJobSearch) {
                $scope.selectedJobSearch = {};
            }

            if ($scope.selectedJobSearch.radius > 0 && $scope.selectedJobSearch.coordinates == null) {
                $scope.errorSetMarker = true;
                $scope.dataLoading = false;
                return;
            } else {
                $scope.errorSetMarker = false;
            }

            JobService.getJobsBySearch($scope.selectedJobSearch)
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
                    offersUpdated();
                });
        };
    }
})();
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

        // gets executed on initial load
        (function initController() {
            $scope.dataLoading = false;

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

            $scope.selectedJobSearch = JobService.createEmptyJobObject();

            $scope.map = GeoService.createMap('jobSearchMap', $scope.mapClicked);
            $scope.mapMarkerLayer = new L.FeatureGroup();
            $scope.map.addLayer($scope.mapMarkerLayer);

            getRecentJobs();
        })();

        $scope.dropdownLocationSelected = function(location){
            $scope.selectedJobSearch.location.address = location.formatted_address;
            $scope.selectedJobSearch.location.coordinates = location.geometry.location;
            setMapMarker(location.geometry.location, location.formatted_address);
        };

        function mapClicked(e){
            $scope.selectedJobSearch.location.coordinates = e.latlng;
            var lat = parseFloat(e.latlng.lat.toFixed(6));
            var lng = parseFloat(e.latlng.lng.toFixed(6));
            setMapMarker(e.latlng, "[" + lat + "," + lng + "]");
        };

        function setMapMarker(latlng, content){
            $scope.mapMarkerLayer.clearLayers();

            var icon = GeoService.createGreenIcon();
            var marker = L.marker(latlng, {icon: icon});
            if(content != null){
                marker.bindPopup(content);
            }

            $scope.mapMarkerLayer.addLayer(marker);

            var radius = $scope.selectedJobSearch.radius;
            if(radius != null && radius != 0){
                var circle = L.circle(latlng, radius * 1000);
                circle.options.stroke = false;
                circle.options.clickable = false;
                $scope.mapMarkerLayer.addLayer(circle);
            }

            $scope.map.panTo(marker.getLatLng(), {animate: true});
        }

        $scope.$watch('selectedJobSearch.location.address', function(){
            var address = $scope.selectedJobSearch.location.address;
            if(address != null && address != ""){
                GeoService.getLocations(address).then(function(response){
                    var results = response.data.results;
                    setSuggestedDropdownLocations(response.data.results);
                });
            }else{
                setSuggestedDropdownLocations(null);
            }
        });

        function setSuggestedDropdownLocations(locations){
            if(locations != null && locations.length > 0){
                $scope.dropdownSearchLocations = locations;
            }else{
                $scope.dropdownSearchLocations = "";
            }
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
                });
        };

        function search() {
            $scope.dataLoading = true;

            if (!$scope.selectedJobSearch) {
                $scope.selectedJobSearch = {};
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
                });
        };

        /*
        $scope.jobFilter = {
            "type": [
                { "value": "1", "label": "Part Time" },
                { "value": "2", "label": "Full Time" },
                { "value": "3", "label": "Master Thesis" }
            ],
            "salary": [
                { "value": "s1", "label": "Less than 20" },
                { "value": "s2", "label": "Between 20 and 40" },
                { "value": "s3", "label": "More than 40" }
            ],
            "duration": [
                { "value": "d1", "label": "Less than 6 month" },
                { "value": "d2", "label": "Between 6 and 11 months" },
                { "value": "d3", "label": "More than one year" }
            ],
            "company": ["Accenture","IBM","Google"],
            "skill": ["CSS","Java","Angular JS"]
        };
        */
    }
})();
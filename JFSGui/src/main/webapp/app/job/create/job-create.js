(function () {
    'use strict';

    angular
        .module('app')
        .controller('JobCreateController', JobCreateController);

    JobCreateController.$inject = ['JobService', 'GeoService', '$scope', '$rootScope', '$compile'];
    function JobCreateController(JobService, GeoService, $scope, $rootScope, $compile) {
        $scope.jobProfileParams = {
            "type": [
                { "value": "master_thesis", "label": "Master thesis" },
                { "value": "bachelor_thesis", "label": "Bachelor thesis" },
                { "value": "part_time", "label": "Part time" },
                { "value": "full_time", "label": "Full time" },
                { "value": "internship", "label": "Internship" },
                { "value": "contract", "label": "Contract" },
                { "value": "others", "label": "Others" }
            ],
            "duration": [
                { "value": (1 * 30 * 24 * 3600 * 1000), "label": "1 month" },
                { "value": (2 * 30 * 24 * 3600 * 1000), "label": "2 months" },
                { "value": (3 * 30 * 24 * 3600 * 1000), "label": "3 months" },
                { "value": (4 * 30 * 24 * 3600 * 1000), "label": "4 months" },
                { "value": (5 * 30 * 24 * 3600 * 1000), "label": "5 months" },
                { "value": (6 * 30 * 24 * 3600 * 1000), "label": "6 months" },
                { "value": (7 * 30 * 24 * 3600 * 1000), "label": "7 months" },
                { "value": (8 * 30 * 24 * 3600 * 1000), "label": "8 months" },
                { "value": (9 * 30 * 24 * 3600 * 1000), "label": "9 months" },
                { "value": (10 * 30 * 24 * 3600 * 1000), "label": "10 months" },
                { "value": (11 * 30 * 24 * 3600 * 1000), "label": "11 months" },
                { "value": (12 * 30 * 24 * 3600 * 1000), "label": "12 months" },
                { "value": 0, "label": "infinite" }
            ]
        };

        $scope.initializeNewJobOffer = initializeNewJobOffer;
        $scope.create = create;
        $scope.reset = reset;

        $scope.setSuggestedMarkers = setSuggestedMarkers;
        $scope.addSuggestedMarker = addSuggestedMarker;
        $scope.clearSuggestedMarkers = clearSuggestedMarkers;
        $scope.addLocationMarker = addLocationMarker;
        $scope.clearLocationMarker = clearLocationMarker;
        $scope.useAddress = useAddress;
        $scope.useCoordinates = useCoordinates;
        $scope.locationSelected = locationSelected;

        $scope.dropdownSearchLocations = "";

        $scope.iconGreen = GeoService.createGreenIcon();
        $scope.iconViolet = GeoService.createVioletIcon();
        $scope.mapAnimatePanning = false;

        // gets executed on initial load
        (function initController() {
            //recreate leaflet map
            initializeMap();
            // reset job create view
            initializeNewJobOffer();
        })();

        function initializeNewJobOffer() {
            $scope.responseMessage = {};
            $scope.responseMessage.showForm = true;
            $scope.responseMessage.success = false;
            $scope.responseMessage.error = false;
            $scope.reset();
        }

        function initializeMap() {
            $scope.map = GeoService.createMap('jobCreateLocationMap', mapClick);

            $scope.locationMarkerLayer = new L.FeatureGroup();
            $scope.locationMarker = "";
            $scope.map.addLayer($scope.locationMarkerLayer);

            $scope.suggestedMarkerLayer = new L.FeatureGroup();
            $scope.suggestedMarkerTable = {};
            $scope.map.addLayer($scope.suggestedMarkerLayer);

            var clearLocationButton = L.easyButton('<span class="glyphicon glyphicon-remove" />', function(btn, map){
                $scope.clearLocationMarker();
            });
            clearLocationButton.button.title = 'Clear selected coordinates.';
            $scope.map.addControl(clearLocationButton);

            var clearSuggestionsButton = L.easyButton('&curvearrowleft;', function(btn, map){
                $scope.clearSuggestedMarkers();
            });
            clearSuggestionsButton.button.title = 'Clear suggestion markers.';
            $scope.map.addControl(clearSuggestionsButton);
        }

        $scope.$watch('jobProfile.location.address', function(){
            var address = $scope.jobProfile.location.address;
            if(address != null && address != ""){
                GeoService.getLocations(address).then(function(response){
                   var results = response.data.results;
                    setSuggestedLocations(response.data.results);
                });
            }else{
                setSuggestedLocations(null);
            }
        });

        function locationSelected(location){
            $scope.jobProfile.location.address = location.formatted_address;
            setCoordinates(location.geometry.location);
        }

        function setSuggestedLocations(locations){
            if(locations != null && locations.length > 0){
                $scope.dropdownSearchLocations = locations;
            }else{
                $scope.dropdownSearchLocations = "";
            }
        }

        function mapClick(e){
            $scope.clearLocationMarker();
            $scope.clearSuggestedMarkers();

            $scope.addLocationMarker(e.latlng, 'Coordinates selected.<br>Loading address suggestions...');

            GeoService.getAddresses(e.latlng.lat, e.latlng.lng).then(function (response) {
                var data = response.data;
                var popupText = "";
                if(response.data != null){
                    popupText = 'Coordinates selected.';
                    popupText += "<br>";
                    popupText += response.data.results.length + ' addresses found.';
                    $scope.setSuggestedMarkers(response.data.results);
                }else{
                    popupText = 'Something went wrong when searching for addresses, please try again.';
                }
                $scope.locationMarker.setPopupContent(popupText);
            });
        }

        function addLocationMarker(latlng, text){
            $scope.jobProfile.location.coordinates = latlng;

            $scope.locationMarker = L.marker(latlng, {icon: $scope.iconGreen});
            $scope.locationMarker .bindPopup(text);
            $scope.locationMarkerLayer.addLayer($scope.locationMarker);
            $scope.locationMarker.setZIndexOffset(-1000);

            $scope.map.panTo(latlng, {animate: $scope.mapAnimatePanning});

            if($scope.mapAnimatePanning == false){
                $scope.mapAnimatePanning = true;
            }
        }

        function setSuggestedMarkers(googleAddressList){
            for(var index = 0; index < googleAddressList.length; ++index){
                var location = googleAddressList[index];
                var coords = location.geometry.location;
                var marker = L.marker([coords.lat, coords.lng], {icon: $scope.iconViolet});

                var level = index / googleAddressList.length;
                if(level == 0){
                    marker.setOpacity(0.6);
                }
                else if(0 < level && level < 0.33){
                    marker.setOpacity(0.5);
                }
                else if(0.33 <= level && level < 0.66){
                    marker.setOpacity(0.4);
                }
                else if(0.66 <= level){
                    marker.setOpacity(0.3);
                }

                $scope.addSuggestedMarker(marker, location);
            }
        }

        function addSuggestedMarker(marker, location){
            $scope.suggestedMarkerLayer.addLayer(marker);
            $scope.suggestedMarkerTable[marker._leaflet_id] = { marker: marker, location: location };

            var html = "<div style='text-align: center;'>";
            html += "<div style='display: inline-block;'>" + location.formatted_address + "</div>";
            html += "<br><br>";
            html += '<button type="button" class="btn btn-default" style="font: inherit;" ng-click="useAddress(' + marker._leaflet_id + ')">Use address</button>';
            html += '<button type="button" class="btn btn-default" style="font: inherit;" ng-click="useCoordinates(' + marker._leaflet_id + ' )">Use coordinates</button>';
            html += "</div>"
            var markerContent = $compile(html)($scope);

            marker.bindPopup(markerContent[0]);
        }

        function useAddress(id){
            var entry = $scope.suggestedMarkerTable[id];

            if(entry != null){
                $scope.jobProfile.location.address = entry.location.formatted_address;
            }
        }

        function useCoordinates(id){
            var entry = $scope.suggestedMarkerTable[id];

            if(entry != null){
                setCoordinates(entry.marker.getLatLng());
            }
        }

        function setCoordinates(latlng){
            if($scope.locationMarker == null || $scope.locationMarker == "") {
                $scope.addLocationMarker(latlng, 'Coordinates selected.');
            }
            else{
                $scope.locationMarker.setLatLng(latlng);
            }

            $scope.jobProfile.location.coordinates = latlng;
            $scope.map.panTo(latlng, {animate: true});
        }

        function clearSuggestedMarkers(){
            $scope.suggestedMarkerLayer.clearLayers();
            $scope.suggestedMarkerTable = {};
        }

        function clearLocationMarker(){
            $scope.locationMarkerLayer.clearLayers();
            $scope.locationMarker = "";
            $scope.jobProfile.location.coordinates = "";
        }

        function create() {
            $scope.dataLoading = true;

            $scope.$broadcast('show-errors-check-validity');

            if ($scope.formCreateJob.$invalid) {
                $scope.responseMessage.error = true;
                $scope.responseMessage.text = "An error occurred while creating your job offer.";
                $scope.dataLoading = false;
                return;
            }

            var obj = {
                "companyId": $rootScope.globals.currentUser.username,
                "token": $rootScope.globals.currentUser.authdata,
                "jobOffer": {
                    "companyId": $rootScope.globals.currentUser.username,
                    "name": $scope.jobProfile.jobName,
                    "function": "",
                    "description": $scope.jobProfile.jobDescription,
                    "task": $scope.jobProfile.jobTask,
                    "skills": $scope.jobProfile.jobSkill,
                    "duration": $scope.jobProfile.duration,
                    "validUntil": Date.parse($scope.jobProfile.validTilldate),
                    "startDate": Date.parse($scope.jobProfile.startDate),
                    "address": $scope.jobProfile.location.address,
                    "lat": $scope.jobProfile.location.coordinates.lat,
                    "lng": $scope.jobProfile.location.coordinates.lng,
                    "website": $scope.jobProfile.jobWebsite,
                    "contactEmail": $scope.jobProfile.jobContactEmail,
                    "type": $scope.jobProfile.type
                }
            };

            JobService.createJob(obj)
                .then(function (response) {
                    $scope.responseMessage = {};
                    $scope.responseMessage.showForm = false;
                    if (response.success && response.data != null && response.data.hasSucceeded) {
                        console.info("Job offer created.");
                        $scope.responseMessage.success = true;
                        $scope.responseMessage.text = "Your job offer has been successfully created.";
                        $scope.dataLoading = false;
                    } else {
                        // backend service is not reachable (e.g. database down)
                        console.error(response.message);
                        $scope.responseMessage.error = true;
                        $scope.responseMessage.text = "An error occurred while creating your job offer.";
                        $scope.dataLoading = false;
                    }
                });
        }

        function reset() {
            $scope.jobProfile = JobService.createEmptyJobObject();

            $scope.clearLocationMarker();
            $scope.clearSuggestedMarkers();

            $scope.$broadcast('show-errors-reset');
        }
    }
})();
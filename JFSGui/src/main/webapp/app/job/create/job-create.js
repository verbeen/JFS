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
                { "value": "contract", "label": "Contract" }
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
        $scope.useAddress = useAddress;
        $scope.useCoordinates = useCoordinates;

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
            $scope.map = L.map('jobCreateLocationMap').setView([48.7758459,9.1829321], 13);
            L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
                attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
            }).addTo($scope.map);
            $scope.map.on('click', mapClick);


            $scope.markers = new L.FeatureGroup();
            $scope.suggestedMarkers = new L.FeatureGroup();

            $scope.map.addLayer($scope.markers);
            $scope.map.addLayer($scope.suggestedMarkers);
        }

        function mapClick(e){
            $scope.markers.clearLayers();
            $scope.suggestedMarkers.clearLayers();

            var iconOrange = new L.Icon.Default({
                iconUrl: 'vendor/leaflet-0.7.7/images/marker-icon-orange.png',
                iconRetinaUrl: 'vendor/leaflet-0.7.7/images/marker-icon-orange-2x.png'
            });

            var clickedMarker = L.marker(e.latlng, {icon: iconOrange});
            clickedMarker.bindPopup('Searching for closest address...');
            clickedMarker.setOpacity(0.5);
            $scope.markers.addLayer(clickedMarker);

            GeoService.getAddresses(e.latlng.lat, e.latlng.lng).then(function (response) {
                var data = response.data;
                var popupText = "";
                if(response.data != null){
                    popupText = 'Search complete, ' + response.data.results.length + ' compatible locations found.';
                    setSuggestionMarkers(response.data.results);
                }else{
                    popupText = 'Something went wrong when searching for addresses, please try again.';
                }
                clickedMarker.setPopupContent(popupText);
            });
        }

        function useAddress(id){
            $scope.suggestedMarkers.eachLayer(function(marker){
                if(marker._leaflet_id == id){
                    marker.setPopupContent("Using address");
                }
            });
        }

        function useCoordinates(id){
            $scope.suggestedMarkers.eachLayer(function(marker){
               if(marker._leaflet_id == id){
                   marker.setPopupContent("Using coordinates");
               }
            });
        }

        function setSuggestionMarkers(googleAddressList){
            var iconPurple = new L.Icon.Default({
                iconUrl: 'vendor/leaflet-0.7.7/images/marker-icon-purple.png',
                iconRetinaUrl: 'vendor/leaflet-0.7.7/images/marker-icon-purple-2x.png'
            });

            for(var index = 0; index < googleAddressList.length; ++index){
                var address = googleAddressList[index];
                var location = address.geometry.location;
                var marker = L.marker([location.lat, location.lng], {icon: iconPurple});
                $scope.suggestedMarkers.addLayer(marker)

                var html = "<div style='text-align: center;'>";
                html += "<div style='display: inline-block;'>" + address.formatted_address + "</div>";
                html += "<br><br>";
                html += '<button type="button" class="btn btn-default" style="font: inherit;" ng-click="useAddress(' + marker._leaflet_id + ')">Use address</button>';
                html += '<button type="button" class="btn btn-default" style="font: inherit;" ng-click="useCoordinates(' + marker._leaflet_id + ' )">Use coordinates</button>';
                html += "</div>"
                var markerContent = $compile(html)($scope);

                marker.bindPopup(markerContent[0]);
                marker.setOpacity(0.5);
            }
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
                    "location": $scope.jobProfile.location,
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
                        console.info("Job offer created!");
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
            $scope.jobProfile = {
                "jobName": "",
                "type": "",
                "jobDescription": "",
                "jobTask": "",
                "jobSkill": "",
                "validTilldate": "",
                "startDate": "",
                "duration": "",
                "location": "",
                "jobWebsite": "",
                "jobContactEmail": ""
            };
            $scope.$broadcast('show-errors-reset');
        }
    }
})();
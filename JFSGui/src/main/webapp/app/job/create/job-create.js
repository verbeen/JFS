(function () {
    'use strict';

    angular
        .module('app')
        .controller('JobCreateController', JobCreateController);


    JobCreateController.$inject = ['JobService', '$scope', '$rootScope'];
    function JobCreateController(JobService, $scope, $rootScope) {
        var vm = this;



        vm.create = create;

        function create() {
            console.log(vm.jobProfile);

            var a = Date.parse(vm.jobProfile.durationFrom);
            console.log(a);

            var obj = {
                "companyId": $rootScope.globals.currentUser.username,
                "token": $rootScope.globals.currentUser.authdata,
                "jobOffer": {
                    "offerId":"",
                    "companyId": $rootScope.globals.currentUser.username,
                    "name": vm.jobProfile.jobName,
                    "function": "",
                    "description": vm.jobProfile.jobDescription,
                    "task": vm.jobProfile.jobTask,
                    "posted": "",
                    "duration": "",
                    "validUntil": Date.parse(vm.jobProfile.durationTo),
                    "startDate": Date.parse(vm.jobProfile.durationFrom),
                    "location": vm.jobProfile.location,
                    "website": "",
                    "type": vm.jobProfile.type
                }
            };

            JobService.createJob(obj)
                .then(function (response) {
                    if (response.success) {
                        console.log(response);
                    }
                });
        }
        vm.jobProfileParams = {
            "type": [
                { "value": "master_thesis", "label": "Master thesis" },
                { "value": "bachelor_thesis", "label": "Bachelor thesis" },
                { "value": "part_time", "label": "Part time" },
                { "value": "full_time", "label": "Full time" },
                { "value": "internship", "label": "Internship" },
                { "value": "contract", "label": "Contract" }
            ]
        };
    }
})();
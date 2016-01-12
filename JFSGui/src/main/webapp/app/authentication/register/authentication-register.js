(function () {
    'use strict';

    angular
        .module('app')
        .controller('RegisterController', RegisterController);

    RegisterController.$inject = ['UserService', '$location', '$rootScope'];
    function RegisterController(UserService, $location, $rootScope) {
        var vm = this;

        vm.register = register;

        function register() {
            vm.dataLoading = true;

            UserService.Create(vm.user)
                .then(function (response) {
                    if (response.success) {
                        if (response.data) {
                            // user is created
                            console.info("Registration successful.");
                            $location.path('/authentication/login');
                        } else {
                            // user is not created and backend returns false (e.g. already registered)
                            console.info("Registration not successful!");
                            vm.error = "Registration not successful!";
                            vm.dataLoading = false;
                        }
                    } else {
                        // backend service is not reachable (e.g. database down)
                        console.error(response.message);
                        vm.error = response.message;
                        vm.dataLoading = false;
                    }
                });
        }
    }
})();
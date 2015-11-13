(function () {
    'use strict';

    angular
        .module('app')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$location', 'AuthenticationService'];
    function LoginController($location, AuthenticationService) {
        var vm = this;

        vm.login = login;

        (function initController() {
            // reset login status
            AuthenticationService.ClearCredentials();
        })();

        function login() {
            vm.dataLoading = true;
            AuthenticationService.Login(vm.email, vm.password)
                .then(function (response) {
                    if (response.success) {
                        if (response.data.isLoggedIn) {
                            // user is logged in
                            console.info("Login successful!");
                            AuthenticationService.SetCredentials(vm.email, vm.password, response.data.token);
                            $location.path('/');
                        } else {
                            // user is not logged in and backend returns false (e.g. user not found)
                            console.info("Login not successful!");
                            vm.error = "Login not successful!";
                            vm.dataLoading = false;
                        }
                    } else {
                        // backend service is not reachable (e.g. database down)
                        console.error(response.message);
                        vm.error = response.message;
                        vm.dataLoading = false;
                    }
                });
        };
    }
})();
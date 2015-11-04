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
            AuthenticationService.Login(vm.email, vm.password, function (response) {
                if (response) {
                    console.debug("User logged in!");
                    AuthenticationService.SetCredentials(vm.email, vm.password);
                    $location.path('/');
                } else {
                    console.error("Error logging in!");
                    vm.dataLoading = false;
                }
            });
        };
    }
})();
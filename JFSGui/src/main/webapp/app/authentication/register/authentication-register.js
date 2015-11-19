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
            console.debug(vm.user);
            UserService.Create(vm.user)
                .then(function (response) {
                    if (response) {
                        console.debug("Registration successful!");
                        $location.path('/authentication/login');
                    } else {
                        vm.error = "Registration not successful!";
                        console.info("Registration not successful!");
                        vm.dataLoading = false;
                    }
                });
        }
    }
})();
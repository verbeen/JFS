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
                    console.debug(response);
                    if (response.success) {
                        console.log("FlashService.Success('Registration successful', true)");
                        $location.path('/authentication/login');
                    } else {
                        console.log("FlashService.Error(response.message)");
                        vm.dataLoading = false;
                    }
                });
        }
    }

})();
(function () {
    'use strict';

    angular
        .module('app')
        .controller('LogoutController', LogoutController);

    LogoutController.$inject = ['UserService'];
    function LogoutController(UserService) {
        var vm = this;

        (function logout() {
            console.log("remove cookie");
            UserService.logOut();
        })();
    }
})();
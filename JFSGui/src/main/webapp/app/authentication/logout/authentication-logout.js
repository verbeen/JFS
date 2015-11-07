(function () {
    'use strict';

    angular
        .module('app')
        .controller('LogoutController', LogoutController);

    LogoutController.$inject = ['$cookieStore', '$rootScope', '$location'];
    function LogoutController($cookieStore, $rootScope, $location) {
        var vm = this;

        (function logout() {
            console.log("remove cookie");
            $cookieStore.remove('globals');
            $rootScope.globals = {};
            $location.path('/authentication/logout');
        })();
    }
})();
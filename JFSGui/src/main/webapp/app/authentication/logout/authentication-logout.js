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
            $rootScope.globals = {
                currentUser: {
                    username: "",
                    userType: "",
                    authdata: "",
                    loggedIn: false
                }
            };
            $cookieStore.remove('globals');
            $location.path('/authentication/logout');
        })();
    }
})();
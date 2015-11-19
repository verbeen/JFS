(function () {
    'use strict';

    angular
        .module('app')
        .factory('AuthenticationService', AuthenticationService);

    AuthenticationService.$inject = ['$http', '$cookieStore', '$rootScope', '$timeout', 'UserService'];
    function AuthenticationService($http, $cookieStore, $rootScope, $timeout, UserService) {
        var service = {};

        service.Login = Login;
        service.SetCredentials = SetCredentials;
        service.ClearCredentials = ClearCredentials;

        return service;

        function Login(email, password, callback) {
            return $http.post('/service/users/login', { email: email, password: password }).then(handleSuccess, handleError('Login not successful!'));
        }

        function SetCredentials(email, password, userType, token) {
            $rootScope.globals = {
                currentUser: {
                    username: email,
                    userType: userType,
                    authdata: token,
                    loggedIn: true
                }
            };

            $cookieStore.put('globals', $rootScope.globals);
        }

        function ClearCredentials() {
            console.log("ClearCredentials");
            $rootScope.globals = {
                currentUser: {
                    username: "",
                    userType: "",
                    authdata: "",
                    loggedIn: false
                }
            };
            $cookieStore.remove('globals');
        }

        // private functions

        function handleSuccess(res) {
            return { success: true, data: res.data };
        }

        function handleError(error) {
            return function () {
                return { success: false, message: error };
            };
        }
    }
})();
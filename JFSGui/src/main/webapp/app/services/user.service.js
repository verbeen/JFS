(function () {
    'use strict';

    angular
        .module('app')
        .factory('UserService', UserService);

    UserService.$inject = ['$http'];
    function UserService($http) {
        var service = {};

        service.Create = Create;
        service.getAllUsers = getAllUsers;
        service.deleteUser = deleteUser;

        return service;

        function Create(user) {
            return $http.post('/service/users/register/' + user.type, user.authentication)
                .then(handleSuccess, handleError('Registration not successful!'));
        }
        function getAllUsers(token) {
            return $http.post('/service/users/all/', token)
                .then(handleSuccess, handleError('Got all jobs "' + token + '"!'));
        }

        function deleteUser(userId){
            return $http.delete('/service/users/delete/' + userId)
                .then(handleSuccess,handleError('Deleting user failed!'));
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
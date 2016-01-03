(function () {
    'use strict';

    angular
        .module('app')
        .controller('AdministrationUserListController', AdministrationUserListController);

    AdministrationUserListController.$inject = ['UserService', '$scope', '$rootScope'];
    function AdministrationUserListController(UserService, $scope, $rootScope) {
        var token = $rootScope.globals.currentUser.authdata;

        $scope.remove = remove;

        // gets executed on initial load
        (function initController() {
            $scope.dataLoading = false;
            getAllUsers();
        })();

        function getAllUsers() {
            UserService.getAllUsers(token)
                .then(function (response) {
                    $scope.noResults = {};
                    $scope.noResults.info = false;
                    $scope.noResults.error = false;
                    if (response.success) {
                        if (response.data.length > 0) {
                            $scope.users = response.data;
                        } else {
                            $scope.users = [];
                            $scope.noResults.info = true;
                            $scope.noResults.title = "No results!";
                            $scope.noResults.text = "No users found.";
                        }
                        $scope.dataLoading = false;
                    } else {
                        console.error(response);
                        $scope.users = [];
                        $scope.noResults.error = true;
                        $scope.noResults.title = "An error occurred!";
                        $scope.noResults.text = "Please try again later.";
                        $scope.dataLoading = false;
                    }
                });
        };

        function remove(userId){
            if (confirm("Are you sure you want to delete the user?")) {
                $scope.userId = userId;
                UserService.deleteUser(userId)
                    .then(function (response) {
                        $scope.noResults = {};
                        $scope.noResults.info = false;
                        $scope.noResults.error = false;
                        if (response.success) {
                            getAllUsers();
                        } else {
                            console.error(response);
                            $scope.users = [];
                            $scope.noResults.error = true;
                            $scope.noResults.title = "An error occurred!";
                            $scope.noResults.text = "Please try again later.";
                            $scope.dataLoading = false;
                        }
                    });
            }
        };
    }
})();
(function () {
    'use strict';

    angular
        .module('app')
        .controller('AdministrationUserListController', AdministrationUserListController);

    AdministrationUserListController.$inject = ['UserService', '$scope', '$rootScope'];
    function AdministrationUserListController(UserService, $scope, $rootScope) {
        var vm = this;

        $scope.getTimes = function(n) {
            return new Array(n);
        };

        vm.remove = function() {
            console.log("remove()");
        };

        UserService.getAllUsers($rootScope.globals.currentUser.authdata)
            .then(function(response) {
                if (response.success) {
                    $scope.users = response.data;
                    console.log("inside if block");
                    console.log($scope.users);
                } else {
                    console.error(response);
                }
            });

/*
        function getAllUsers(UserService, $Scope, $rootScope){

            UserService.getAllUsers($rootScope.globals.currentUser.authdata)
                .then(function (response) {
                    if (response.success) {
                        console.log("inside function");
                        console.log(response);

                    }
                });
        }*/

    }
})();
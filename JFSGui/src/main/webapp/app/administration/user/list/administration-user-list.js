(function () {
    'use strict';

    angular
        .module('app')
        .controller('AdministrationUserListController', AdministrationUserListController);

    AdministrationUserListController.$inject = ['$scope'];
    function AdministrationUserListController($scope) {
        var vm = this;

        $scope.getTimes = function(n) {
            return new Array(n);
        };

        vm.remove = function() {
            console.log("remove()");
        };
    }
})();
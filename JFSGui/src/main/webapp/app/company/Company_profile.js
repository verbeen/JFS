(function () {
    'use strict';

    angular
        .module('app')
        .controller('CompanyProfileController', CompanyProfileController);

    CompanyProfileController.$inject = ['$scope'];
    function CompanyProfileController($scope) {
        $scope.sample = "sample";
    }

})();

(function () {
    'use strict';

    angular
        .module('app')
        .controller('SampleController', SampleController);

    SampleController.$inject = ['$scope'];
    function SampleController($scope) {
        var that = this;
        that.testObject = "bla";

        $scope.sample = "sample";
    }

})();
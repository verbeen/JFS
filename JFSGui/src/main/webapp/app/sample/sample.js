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

        $scope.sampleObjectForSelect = [
            {"value":"Gear","label":"Gear"},
            {"value":"Globe","label":"Globe"},
            {"value":"Heart","label":"Heart"}
        ];
    }

})();
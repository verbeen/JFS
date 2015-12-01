 document.write('<base href="' + document.location + '" />');

(function () {
    'use strict';

    angular
        .module('app')
        .controller('AddNotificationController', AddNotificationController);

    AddNotificationController.$inject = ['$scope','$http'];
    function AddNotificationController($scope) {

        $scope.movies = [
            'The Dark Knight',
            'Heat',
            'Inception',

        ];

        $scope.loadMovies = function(query) {
            return $http.get('movies.json');
        };




        $scope.sample = "sample";
        $scope.redirect = function(){
            window.location = "#/student";
        }
    }

})();


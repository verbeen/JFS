(function () {
    'use strict';

    angular.module('app')
        .directive('navigation', NavigationDirective);

    NavigationDirective.$inject = ['$rootScope'];
    function NavigationDirective($rootScope) {
        return {
            restrict: 'E',
            templateUrl: '/app/directive/navigation/directive-navigation.html',
            link: function ($scope, $el, $attrs) {
                 $rootScope.$watch('globals.currentUser.userType', function(newVal, oldVal) {
                     if ($rootScope.globals.currentUser.loggedIn) {
                         $scope.loggedIn = true;
                     } else {
                         $scope.loggedIn = false;
                     }

                     if ($rootScope.globals.currentUser.userType == 'ADMIN') {
                         $scope.isAdmin = true;
                         $scope.isStudent = false;
                         $scope.isCompany = false;
                     } else if ($rootScope.globals.currentUser.userType == 'STUDENT') {
                         $scope.isAdmin = false;
                         $scope.isStudent = true;
                         $scope.isCompany = false;
                     } else if ($rootScope.globals.currentUser.userType == 'COMPANY') {
                         $scope.isAdmin = false;
                         $scope.isStudent = false;
                         $scope.isCompany = true;
                     } else {
                         $scope.isAdmin = false;
                         $scope.isStudent = false;
                         $scope.isCompany = false;
                     }
                }, true);
            }
        };
    }
})();
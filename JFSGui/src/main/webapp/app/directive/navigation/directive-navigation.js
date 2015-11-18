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
                     } else {
                         $scope.isAdmin = false;
                     }
                }, true);
            }
        };
    }
})();
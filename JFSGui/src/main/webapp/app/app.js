(function () {
    'use strict';

    angular.module('app', ["ngRoute", "ngCookies", "ngSanitize", "mgcrea.ngStrap", "wu.masonry"])
        .config(config)
        .run(run)
        .controller('HomeController', HomeController)
        .filter('duration', DurationFilter)
        .filter('htmlToPlainText', HtmlToPlainTextFilter);

    config.$inject = ['$routeProvider'];
    function config($routeProvider, $rootScope) {
        $routeProvider
            .when('/', {
                templateUrl: 'app/home.html',
                controller: 'HomeController as vm'
            })
            .when('/job/list', {
                templateUrl: 'app/job/list/job-list.html',
                controller: 'JobListController as vm'
            })
            .when('/job/profile/:id', {
                templateUrl: 'app/job/profile/job-profile.html',
                controller: 'JobProfileController as vm'
            })
            .when('/authentication/login', {
                templateUrl: 'app/authentication/login/authentication-login.html',
                controller: 'LoginController as vm'
            })
            .when('/authentication/logout', {
                templateUrl: 'app/authentication/logout/authentication-logout.html',
                controller: 'LogoutController as vm'
            })
            .when('/authentication/register', {
                templateUrl: 'app/authentication/register/authentication-register.html',
                controller: 'RegisterController as vm'
            })
            .when('/administration/job/list', {
                templateUrl: 'app/administration/job/list/administration-job-list.html',
                controller: 'AdministrationJobListController as vm'
            })
            .when('/administration/user/list', {
                templateUrl: 'app/administration/user/list/administration-user-list.html',
                controller: 'AdministrationUserListController as vm'
            })
            .when('/sample', {
                templateUrl: 'app/sample/sample.html',
                controller: 'SampleController as vm'
            })
            .when('/job/create', {
                templateUrl: 'app/job/create/job-create.html',
                controller: 'JobCreateController as vm'
            })
            .otherwise({redirectTo: '/'});
    }

    run.$inject = ['$rootScope', '$location', '$cookieStore', '$http'];
    function run($rootScope, $location, $cookieStore, $http) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookieStore.get('globals') || {};

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in and trying to access a restricted page
            var loggedIn = $rootScope.globals.currentUser;

            if ($location.path() == '/authentication/register' && !loggedIn) {
                $location.path('/authentication/register');
            } else if ($location.path() != '/authentication/login' && !loggedIn) {
                $location.path('/authentication/login');
            }
        });
    }

    HomeController.$inject = ['$rootScope', '$scope'];
    function HomeController($rootScope, $scope) {
        $scope.username = $rootScope.globals.currentUser.username;
        $scope.userType = $rootScope.globals.currentUser.userType;
        $scope.token = $rootScope.globals.currentUser.authdata;
    }

    DurationFilter.$inject = [];
    function DurationFilter() {
        return function(millseconds) {
            var days = Math.floor(millseconds / 1000 / 3600 / 24);

            if (days < 31) {
                return days + " days";
            } else {
                var months = days / 30;
                return months + " months";
            }
        }
    }

    HtmlToPlainTextFilter.$inject = [];
    function HtmlToPlainTextFilter() {
        return function(text) {
            return  text ? String(text).replace(/<[^>]+>/gm, '') : '';
        };
    }
})();
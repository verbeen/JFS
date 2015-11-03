angular.module('app', ["ngRoute", "ngCookies", "mgcrea.ngStrap", "jobofferList", "jobofferDetails"])
    .config(['$routeProvider', function($routeProvider, $rootScope) {
		$routeProvider.when('/', {
            templateUrl: 'app/home.html',
            controller: 'MainController as MainCtrl'
        })
		.when('/joboffer/list', {
			templateUrl: 'app/modules/joboffer/list/joboffer-list.html',
			controller: 'JobOfferListController as JobOfferListCtrl'
		})
		.when('/joboffer/details/:id', {
			templateUrl: 'app/modules/joboffer/details/joboffer-details.html',
			controller: 'JobOfferDetailsController as JobOfferDetailsCtrl'
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
            templateUrl: 'app/authentication/register/authentication-register-organization.html',
            controller: 'RegisterController as vm'
        })
		.otherwise({redirectTo: '/'});
	}])
    .controller('MainController', function($scope, $rootScope) {
        $scope.username = $rootScope.globals.currentUser.username;
    })
    .run(['$rootScope', '$location', '$cookieStore', '$http', function($rootScope, $location, $cookieStore, $http) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookieStore.get('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
        }

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in and trying to access a restricted page
            var loggedIn = $rootScope.globals.currentUser;

            if ($location.path() == '/authentication/register' && !loggedIn) {
                $location.path('/authentication/register');
            } else if ($location.path() != '/authentication/login' && !loggedIn) {
                $location.path('/authentication/login');
            }
        });
    }]);
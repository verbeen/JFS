angular.module('jfsApp', ["ngRoute", "mgcrea.ngStrap", "jobofferList", "jobofferDetails"])
	.config(['$routeProvider', function($routeProvider) {
		$routeProvider.when('/', {
			template: '<h1>home</h1>'
		})
		.when('/joboffer/list', {
			templateUrl: 'app/modules/joboffer/list/joboffer-list.html',
			controller: 'JobOfferListController as JobOfferListCtrl'
		})
		.when('/joboffer/details/:id', {
			templateUrl: 'app/modules/joboffer/details/joboffer-details.html',
			controller: 'JobOfferDetailsController as JobOfferDetailsCtrl'
		})
		.otherwise({redirectTo: '/'});
	}]);
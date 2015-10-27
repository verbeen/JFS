angular.module("jobofferList")
	.directive("jobofferCreate", [function() {
		return {
			templateUrl: 'app/modules/joboffer/list/create/joboffer-create.html',
			restrict: 'A',
			link: function($scope, $element, $attrs) {
				console.log('blub');
			}
		};
	}]);
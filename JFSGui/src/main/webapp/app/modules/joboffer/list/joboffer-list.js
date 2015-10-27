angular.module("jobofferList", [])
	.controller('JobOfferListController', function($scope, JobOfferList) {
		JobOfferList.getAllJoboffers()
		.success(function(data) {
			$scope.joboffers = data;
		});
	});
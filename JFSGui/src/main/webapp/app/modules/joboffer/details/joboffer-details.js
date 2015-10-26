angular.module("jobofferDetails", [])
	.controller("JobOfferDetailsController", function($scope, JobOfferDetails) {
		JobOfferDetails.getJobofferDetails()
		.success(function(data) {
			$scope.jobofferDetails = data;
		});
	});
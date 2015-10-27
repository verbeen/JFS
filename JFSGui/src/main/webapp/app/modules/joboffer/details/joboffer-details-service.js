angular.module("jobofferDetails")
	.factory('JobOfferDetails', function JobOfferDetailsFactory($http) {
		return {
			getJobofferDetails: function() {
				return $http({
					method: "GET",
					url: "data/jobofferDetails.json"
				});
			}
		};
	});
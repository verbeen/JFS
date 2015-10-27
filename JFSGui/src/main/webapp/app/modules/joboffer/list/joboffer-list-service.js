angular.module("jobofferList")
	.factory('JobOfferList', function JobOfferListFactory($http) {
		return {
			getAllJoboffers: function() {
				return $http({
					method: "GET",
					url: "data/joboffers.json"
				});
			}
		};
	});
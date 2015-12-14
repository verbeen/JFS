describe('Controller: JobProfileController', function() {
    beforeEach(module('app'));

    var ctrl, rootScope, scope, jobService, routeParams;

    beforeEach(inject(function($controller, JobService, $rootScope, $routeParams) {
        spyOn(JobService, 'getJobProfile').and.callThrough();
        routeParams = $routeParams;
        jobService = JobService;
        routeParams.offerId = 3;
        scope = $rootScope.$new();
        ctrl = $controller('JobProfileController', { $scope: scope });
    }));

    it('tracks that the spy on JobService.getJobProfile() was called', function() {
        expect(jobService.getJobProfile).toHaveBeenCalled();
    });

    it('tracks number of arguments of the spys\' calls on JobService.getJobProfile()', function() {
        expect(jobService.getJobProfile.calls.allArgs()[0].length).toEqual(1);
    });

    it('tracks number of the spys\' calls on JobService.getJobProfile()', function() {
        expect(jobService.getJobProfile.calls.count()).toEqual(1);
    });

    it('tracks all the arguments the spys\' calls on JobService.getJobProfile()', function() {
        expect(jobService.getJobProfile).toHaveBeenCalledWith(3);
        expect(jobService.getJobProfile).toHaveBeenCalledWith(jasmine.any(Number));
    });
});
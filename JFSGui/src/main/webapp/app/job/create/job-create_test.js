describe('Controller: JobCreateController', function() {
    beforeEach(module('app'));

    var rootScope, scope, ctrl, jobService;

    beforeEach(inject(function($rootScope, $controller, JobService) {
        spyOn(JobService, 'createJob').and.callThrough();
        jobService = JobService;
        rootScope = $rootScope;
        rootScope.globals = {
            currentUser: {
                username: "company@company"
            }
        };
        scope = $rootScope.$new();
        scope.formCreateJob = {};
        scope.jobProfile = {};
        ctrl = $controller('JobCreateController', { $scope: scope });
    }));

    it('initialization of job profile parameters on initial controller load', function() {
        expect(scope.jobProfileParams.type).toBeTruthy();
        expect(scope.jobProfileParams.duration).toBeTruthy();
    });

    it('tracks that the spy on JobService.createJob() was called', function() {
        scope.formCreateJob.$invalid = false;
        scope.create();
        expect(jobService.createJob).toHaveBeenCalled();
    });

    it('tracks number of arguments of the spys\' calls on JobService.createJob()', function() {
        scope.formCreateJob.$invalid = false;
        scope.create();
        expect(jobService.createJob.calls.allArgs()[0].length).toEqual(1);
    });

    it('tracks number of the spys\' calls on JobService.createJob()', function() {
        scope.formCreateJob.$invalid = false;
        scope.create();
        expect(jobService.createJob.calls.count()).toEqual(1);
    });

    it('tracks all the arguments the spys\' calls on JobService.getJobsBySearch()', function() {
        scope.formCreateJob.$invalid = false;
        scope.create();
        expect(jobService.createJob).toHaveBeenCalledWith(jasmine.any(Object));
    });

    it('tracks that the spy on JobService.createJob() was not called when form is invalid', function() {
        scope.formCreateJob.$invalid = true;
        scope.create();
        expect(jobService.createJob).not.toHaveBeenCalled();
    });
});
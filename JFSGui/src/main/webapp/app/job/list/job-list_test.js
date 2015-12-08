describe('Controller: JobListController', function() {
    beforeEach(module('app'));

    var scope, ctrl, jobService;

    beforeEach(inject(function($rootScope, $controller, JobService) {
        spyOn(JobService, 'getRecentJobs').and.callThrough();
        spyOn(JobService, 'getJobsBySearch').and.callThrough();
        jobService = JobService;
        scope = $rootScope.$new();
        ctrl = $controller('JobListController', { $scope: scope });
    }));

    it('initialization of job types on initial controller load', function() {
        expect(scope.jobSearch).toBeTruthy();
    });

    it('tracks that the spy on JobService.getRecentJobs() was called', function() {
        expect(jobService.getRecentJobs).toHaveBeenCalled();
    });

    it('tracks number of arguments of the spys\' calls on JobService.getRecentJobs()', function() {
        expect(jobService.getRecentJobs.calls.allArgs()[0].length).toEqual(1);
    });

    it('tracks number of the spys\' calls on JobService.getRecentJobs()', function() {
        expect(jobService.getRecentJobs.calls.count()).toEqual(1);
    });

    it('tracks all the arguments the spys\' calls on JobService.getRecentJobs()', function() {
        expect(jobService.getRecentJobs).toHaveBeenCalledWith(jasmine.any(Number));
    });

    it('when search function is called, job search argument has to be defined', function() {
        scope.search();
        expect(scope.selectedJobSearch).toBeDefined();
    });

    it('tracks that the spy on JobService.getJobsBySearch() was called', function() {
        scope.search();
        expect(jobService.getJobsBySearch).toHaveBeenCalled();
    });

    it('tracks number of arguments of the spys\' calls on JobService.getJobsBySearch()', function() {
        scope.search();
        expect(jobService.getJobsBySearch.calls.allArgs()[0].length).toEqual(1);
    });

    it('tracks number of the spys\' calls on JobService.getJobsBySearch()', function() {
        scope.search();
        expect(jobService.getJobsBySearch.calls.count()).toEqual(1);
    });

    it('tracks all the arguments the spys\' calls on JobService.getJobsBySearch()', function() {
        scope.search();
        expect(jobService.getJobsBySearch).toHaveBeenCalledWith(scope.selectedJobSearch);
        expect(jobService.getJobsBySearch).toHaveBeenCalledWith(jasmine.any(Object));
    });
});
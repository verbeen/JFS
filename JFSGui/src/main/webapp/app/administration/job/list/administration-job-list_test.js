describe('Controller: AdministrationJobListController', function() {
    beforeEach(module('app'));

    var rootScope, scope, ctrl, jobService;

    beforeEach(inject(function($rootScope, $controller, JobService) {
        spyOn(JobService, 'getAllJobs').and.callThrough();
        spyOn(JobService, 'getJobsBySearch').and.callThrough();
        jobService = JobService;
        rootScope = $rootScope;
        rootScope.globals = {
            currentUser: {
                authdata: 'token-123-456-789'
            }
        };
        scope = $rootScope.$new();
        ctrl = $controller('AdministrationJobListController', { $scope: scope });
    }));

    it('initialization of job types on initial controller load', function() {
        expect(scope.jobSearch).toBeTruthy();
    });

    it('tracks that the spy on JobService.getAllJobs() was called', function() {
        expect(jobService.getAllJobs).toHaveBeenCalled();
    });

    it('tracks number of arguments of the spys\' calls on JobService.getAllJobs()', function() {
        expect(jobService.getAllJobs.calls.allArgs()[0].length).toEqual(1);
    });

    it('tracks number of the spys\' calls on JobService.getAllJobs()', function() {
        expect(jobService.getAllJobs.calls.count()).toEqual(1);
    });

    it('tracks all the arguments the spys\' calls on JobService.getAllJobs()', function() {
        expect(jobService.getAllJobs).toHaveBeenCalledWith('token-123-456-789');
        expect(jobService.getAllJobs).toHaveBeenCalledWith(jasmine.any(String));
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
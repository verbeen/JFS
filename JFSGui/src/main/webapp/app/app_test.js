describe('Navigation: app run', function() {
    beforeEach(module('app'));

    var route, rootScope, location, httpBackend;

    beforeEach(inject(function($route, $rootScope, $location, $httpBackend) {
        route = $route;
        rootScope = $rootScope;
        location = $location;
        httpBackend = $httpBackend;

        httpBackend.when('GET', 'app/home.html').respond('home');
        httpBackend.when('GET', 'app/job/list/job-list.html').respond('jobList');
        httpBackend.when('GET', 'app/job/profile/job-profile.html').respond('jobProfile');
        httpBackend.when('GET', 'app/authentication/login/authentication-login.html').respond('login');
        httpBackend.when('GET', 'app/authentication/logout/authentication-logout.html').respond('logout');
        httpBackend.when('GET', 'app/authentication/register/authentication-register.html').respond('register');
        httpBackend.when('GET', 'app/administration/job/list/administration-job-list.html').respond('adminJobList');
        httpBackend.when('GET', 'app/administration/user/list/administration-user-list.html').respond('adminUserList');
        httpBackend.when('GET', 'app/job/create/job-create.html').respond('jobCreate');
    }));

    it('verify routing for home when user is logged in', function() {
        rootScope.globals = {
            currentUser: {
                loggedIn: true
            }
        };
        rootScope.$apply(function() {
            location.path('/');
        });
        expect(location.path()).toBe('/');
        expect(route.current.templateUrl).toBe('app/home.html');
        expect(route.current.controller).toBe('HomeController as vm');
    });

    it('grant access to /authentication/register when user is not logged in', function() {
        rootScope.globals = {
            currentUser: {
                loggedIn: false
            }
        };
        rootScope.$apply(function() {
            location.path('/authentication/register');
        });
        expect(location.path()).toBe('/authentication/register');
    });

    it('grant access to /authentication/login when user is not logged in', function() {
        rootScope.globals = {
            currentUser: {
                loggedIn: false
            }
        };
        rootScope.$apply(function() {
            location.path('/authentication/login');
        });
        expect(location.path()).toBe('/authentication/login');
    });

    it('grant access to /job/list when user is not logged in', function() {
        rootScope.globals = {
            currentUser: {
                loggedIn: false
            }
        };
        rootScope.$apply(function() {
            location.path('/job/list');
        });
        expect(location.path()).toBe('/job/list');
    });

    it('redirect user to login page when not logged in', function() {
        rootScope.globals = {
            currentUser: {
                loggedIn: false
            }
        };

        rootScope.$apply(function() {
            location.path('/');
        });
        expect(location.path()).toBe('/authentication/login');
        expect(route.current.templateUrl).toBe('app/authentication/login/authentication-login.html');
        expect(route.current.controller).toBe('LoginController as vm');

        rootScope.$apply(function() {
            location.path('/job/profile/10');
        });
        expect(location.path()).toBe('/authentication/login');
        expect(route.current.templateUrl).toBe('app/authentication/login/authentication-login.html');
        expect(route.current.controller).toBe('LoginController as vm');
    });

    it('redirect user from /job/create when user is not logged in as company', function() {
        rootScope.globals = {
            currentUser: {
                userType: "STUDENT",
                loggedIn: true
            }
        };
        rootScope.$apply(function() {
            location.path('/job/create');
        });
        expect(location.path()).toBe('/');

        rootScope.globals = {
            currentUser: {
                userType: "ADMIN",
                loggedIn: true
            }
        };
        rootScope.$apply(function() {
            location.path('/job/create');
        });
        expect(location.path()).toBe('/');
    });

    it('redirect user from /administration/user/list when user is not logged in as admin', function() {
        rootScope.globals = {
            currentUser: {
                userType: "STUDENT",
                loggedIn: true
            }
        };
        rootScope.$apply(function() {
            location.path('/administration/user/list');
        });
        expect(location.path()).toBe('/');

        rootScope.globals = {
            currentUser: {
                userType: "COMPANY",
                loggedIn: true
            }
        };
        rootScope.$apply(function() {
            location.path('/administration/user/list');
        });
        expect(location.path()).toBe('/');
    });
});
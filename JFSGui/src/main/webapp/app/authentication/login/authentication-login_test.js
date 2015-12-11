describe('Controller: LoginController', function() {
    beforeEach(module('app'));

    var scope, ctrl, authenticationService;

    beforeEach(inject(function($rootScope, $controller, AuthenticationService) {
        spyOn(AuthenticationService, 'Login').and.callThrough();
        authenticationService = AuthenticationService;
        scope = $rootScope.$new();
        ctrl = $controller('LoginController', { $scope: scope });
    }));

    it('tracks that the spy on AuthenticationService.Login() was called', function() {
        ctrl.email = 'email@email';
        ctrl.password = 'passw0rd';
        ctrl.login();
        expect(authenticationService.Login).toHaveBeenCalled();
    });

    it('tracks number of arguments of the spys\' calls on AuthenticationService.Login()', function() {
        ctrl.email = 'email@email';
        ctrl.password = 'passw0rd';
        ctrl.login();
        expect(authenticationService.Login.calls.allArgs()[0].length).toEqual(2);
    });

    it('tracks number of the spys\' calls on AuthenticationService.Login()', function() {
        ctrl.email = 'email@email';
        ctrl.password = 'passw0rd';
        ctrl.login();
        expect(authenticationService.Login.calls.count()).toEqual(1);
    });

    it('tracks all the arguments the spys\' calls on AuthenticationService.Login()', function() {
        ctrl.email = 'email@email';
        ctrl.password = 'passw0rd';
        ctrl.login();
        expect(authenticationService.Login).toHaveBeenCalledWith('email@email', 'passw0rd');
        expect(authenticationService.Login).toHaveBeenCalledWith(jasmine.any(String), jasmine.any(String));
    });
});
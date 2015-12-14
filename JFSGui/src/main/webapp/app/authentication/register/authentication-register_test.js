describe('Controller: RegisterController', function() {
    beforeEach(module('app'));

    var scope, ctrl, userService;

    beforeEach(inject(function($rootScope, $controller, UserService) {
        spyOn(UserService, 'Create').and.callThrough();
        userService = UserService;
        scope = $rootScope.$new();
        ctrl = $controller('RegisterController', { $scope: scope });
        ctrl.user = {
            authentication: {
                username: 'email@email',
                password: 'passw0rd'
            },
            type: 'STUDENT'
        };
    }));

    it('tracks that the spy on UserService.Register() was called', function() {
        ctrl.register();
        expect(userService.Create).toHaveBeenCalled();
    });

    it('tracks number of arguments of the spys\' calls on UserService.Register()', function() {
        ctrl.register();
        expect(userService.Create.calls.allArgs()[0].length).toEqual(1);
    });

    it('tracks number of the spys\' calls on UserService.Register()', function() {
        ctrl.register();
        expect(userService.Create.calls.count()).toEqual(1);
    });

    it('tracks all the arguments the spys\' calls on UserService.Register()', function() {
        ctrl.register();
        var result = {
            authentication: {
                username: 'email@email',
                password: 'passw0rd'
            },
            type: 'STUDENT'
        };
        expect(userService.Create).toHaveBeenCalledWith(result);
        expect(userService.Create).toHaveBeenCalledWith(jasmine.any(Object));
    });
});
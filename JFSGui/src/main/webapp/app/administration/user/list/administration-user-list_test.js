describe('Controller: AdministrationUserListController', function() {
    beforeEach(module('app'));

    var rootScope, scope, ctrl, userService;

    beforeEach(inject(function($rootScope, $controller, UserService) {
        spyOn(UserService, 'getAllUsers').and.callThrough();
        userService = UserService;
        rootScope = $rootScope;
        rootScope.globals = {
            currentUser: {
                authdata: 'token-123-456-789'
            }
        };
        scope = $rootScope.$new();
        ctrl = $controller('AdministrationUserListController', { $scope: scope });
    }));

    it('tracks that the spy on UserService.getAllUsers() was called', function() {
        expect(userService.getAllUsers).toHaveBeenCalled();
    });

    it('tracks number of arguments of the spys\' calls on UserService.getAllUsers()', function() {
        expect(userService.getAllUsers.calls.allArgs()[0].length).toEqual(1);
    });

    it('tracks number of the spys\' calls on UserService.getAllUsers()', function() {
        expect(userService.getAllUsers.calls.count()).toEqual(1);
    });

    it('tracks all the arguments the spys\' calls on UserService.getAllUsers()', function() {
        expect(userService.getAllUsers).toHaveBeenCalledWith('token-123-456-789');
        expect(userService.getAllUsers).toHaveBeenCalledWith(jasmine.any(String));
    });
});
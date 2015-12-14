describe('Controller: LogoutController', function() {
    beforeEach(module('app'));

    var rootScope, scope, ctrl, location;

    beforeEach(inject(function($rootScope, $controller, $location) {
        scope = $rootScope.$new();
        location = $location;
        rootScope = $rootScope;
        rootScope.globals = {
            currentUser: {
                username: "email@email",
                userType: "STUDENT",
                authdata: "token-123-456-789",
                loggedIn: true
            }
        };
        ctrl = $controller('LogoutController', { $scope: scope });
    }));

    it('checks that rootScope gets reset', function() {
        expect(rootScope.globals.currentUser.loggedIn).toBeFalsy();
    });

    it('checks that user was forwarded to logout confirmation page', function() {
        expect(location.path()).toBe('/authentication/logout');
    });
});
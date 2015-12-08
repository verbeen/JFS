describe('Controller: SampleController', function() {
    beforeEach(module('app'));

    var scope, ctrl;

    beforeEach(inject(function($rootScope, $controller) {
        scope = $rootScope.$new();
        ctrl = $controller('SampleController', { $scope: scope });
    }));

    it('should have the sample string set on load', function() {
       expect(scope.sample).toEqual('sample');
    });

});
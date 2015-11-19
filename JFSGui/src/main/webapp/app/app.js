(function () {
    'use strict';

    angular.module('app', ["ngRoute", "ngCookies", "ngSanitize", "mgcrea.ngStrap", "wu.masonry"])
        .config(config)
        .run(run)
        .controller('HomeController', HomeController)
        .filter('duration', DurationFilter)
        .filter('htmlToPlainText', HtmlToPlainTextFilter)
        .directive('showErrors', ShowErrorsDirective)
        .directive('minLength', BsSelectValidationDirective);

    config.$inject = ['$routeProvider'];
    function config($routeProvider, $rootScope) {
        $routeProvider
            .when('/', {
                templateUrl: 'app/home.html',
                controller: 'HomeController as vm'
            })
            .when('/job/list', {
                templateUrl: 'app/job/list/job-list.html',
                controller: 'JobListController as vm'
            })
            .when('/job/profile', {
                templateUrl: 'app/job/profile/job-profile.html',
                controller: 'JobProfileController as vm'
            })
            .when('/authentication/login', {
                templateUrl: 'app/authentication/login/authentication-login.html',
                controller: 'LoginController as vm'
            })
            .when('/authentication/logout', {
                templateUrl: 'app/authentication/logout/authentication-logout.html',
                controller: 'LogoutController as vm'
            })
            .when('/authentication/register', {
                templateUrl: 'app/authentication/register/authentication-register.html',
                controller: 'RegisterController as vm'
            })
            .when('/administration/job/list', {
                templateUrl: 'app/administration/job/list/administration-job-list.html',
                controller: 'AdministrationJobListController as vm'
            })
            .when('/administration/user/list', {
                templateUrl: 'app/administration/user/list/administration-user-list.html',
                controller: 'AdministrationUserListController as vm'
            })
            .when('/sample', {
                templateUrl: 'app/sample/sample.html',
                controller: 'SampleController as vm'
            })
            .when('/job/create', {
                templateUrl: 'app/job/create/job-create.html',
                controller: 'JobCreateController as vm'
            })
            .when('/company', {
                templateUrl: 'app/company/company_profile.html',
                controller: 'CompanyProfileController as vm'
            })
            .otherwise({redirectTo: '/'});
    }

    run.$inject = ['$rootScope', '$location', '$cookieStore'];
    function run($rootScope, $location, $cookieStore) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookieStore.get('globals') || {};

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            if ($rootScope.globals.currentUser == undefined) {
                $rootScope.globals = {
                    currentUser: {
                        username: "",
                        userType: "",
                        authdata: "",
                        loggedIn: false
                    }
                };
            }

            // redirect to login page if not logged in and trying to access a restricted page
            var currentUser = $rootScope.globals.currentUser;
            var currentPath = $location.path();
            var loggedIn = $rootScope.globals.currentUser.loggedIn;

            if (currentPath == '/authentication/register' && !loggedIn) {
                $location.path('/authentication/register');
            } else if (currentPath != '/authentication/login' && !loggedIn) {
                $location.path('/authentication/login');
            }

            if (currentPath.indexOf('/administration/') > -1 && currentUser.userType != 'ADMIN') {
                console.error("No access!");
                $location.path('/');
            }
        });
    }

    HomeController.$inject = ['$rootScope', '$scope'];
    function HomeController($rootScope, $scope) {
        $scope.username = $rootScope.globals.currentUser.username;
        $scope.userType = $rootScope.globals.currentUser.userType;
        $scope.token = $rootScope.globals.currentUser.authdata;
    }

    DurationFilter.$inject = [];
    function DurationFilter() {
        return function(millseconds) {
            var days = Math.floor(millseconds / 1000 / 3600 / 24);

            if (days < 31) {
                return days + " days";
            } else {
                var months = days / 30;
                return months + " months";
            }
        }
    }

    HtmlToPlainTextFilter.$inject = [];
    function HtmlToPlainTextFilter() {
        return function(text) {
            return  text ? String(text).replace(/<[^>]+>/gm, '') : '';
        };
    }

    ShowErrorsDirective.$inject = ['$timeout'];
    function ShowErrorsDirective($timeout) {
        return {
            restrict: 'A',
            require: '^form',
            link: function (scope, el, attrs, formCtrl) {
                // find the text box element, which has the 'name' attribute
                var inputEl = el[0].querySelector("[name]");
                // convert the native text box element to an angular element
                var inputNgEl = angular.element(inputEl);
                // get the name on the text box so we know the property to check
                // on the form controller
                var inputName = inputNgEl.attr('name');

                // only apply the has-error class after the user leaves the text box
                inputNgEl.bind('blur', function () {
                    el.toggleClass('has-error', formCtrl[inputName].$invalid);
                })

                scope.$on('show-errors-check-validity', function() {
                    el.toggleClass('has-error', formCtrl[inputName].$invalid);
                });

                scope.$on('show-errors-reset', function() {
                    $timeout(function() {
                        el.removeClass('has-error');
                    }, 0, false);
                });
            }
        };
    }

    BsSelectValidationDirective.$inject = [];
    function BsSelectValidationDirective() {
        return {
            require: 'ngModel',
            link: function (scope, elem, attr, ngModel) {
                var minLength = attr.minLength;

                //For model -> DOM validation
                ngModel.$formatters.unshift(function (value) {
                    if (ngModel.$viewValue !== undefined) {
                        var valid = (ngModel.$viewValue.length >= minLength);
                        ngModel.$setValidity('minLength', valid);
                        return value;
                    }
                });

                scope.$watch(attr.ngModel, function (newVal) {
                    if (angular.isDefined(newVal)) {
                        var valid = (newVal.length >= minLength);
                        ngModel.$setValidity('minLength', valid);
                        return valid ? ngModel.$viewValue : undefined;
                    }
                }, true);
            }
        };
    }
})();
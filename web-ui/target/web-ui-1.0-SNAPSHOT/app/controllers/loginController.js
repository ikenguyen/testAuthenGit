'use strict';

define(['order-tracker'], function (orderTracker) {

    var injectParams = ['$scope', '$route', 'auth'];

    var LoginController = function ($scope, $route, auth) {
        $scope.credentials = {};

        $scope.tab = function(route) {
            return $route.current && route === $route.current.controller;
        };

        $scope.authenticated = function() {
            return auth.authenticated;
        }

        $scope.login = function() {
              auth.authenticate($scope.credentials, function(authenticated) {
                  if (authenticated) {
                      console.log("Login succeeded")
                      $scope.error = false;
                  } else {
                      console.log("Login failed")
                      $scope.error = true;
                  }
              })
        };
        $scope.logout = auth.clear;
    };

    LoginController.$inject = injectParams;

    orderTracker.register.controller('LoginController', LoginController);

});
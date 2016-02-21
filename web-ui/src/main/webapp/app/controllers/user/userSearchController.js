'use strict';

define(['order-tracker'], function (orderTracker) {

    var injectParams = ['$scope','$controller','userService'];

    var UserSearchController = function ($scope, $controller, userService) {

        var args = {
                $scope: $scope,
                title : "User.Search",
                lang : "User"
        };
        angular.extend(this, $controller('simpleController', args));
        var self = this;
        $scope.customers = [];
        $scope.displayed

        $scope.getData = function(tblState) {
            $scope.isLoading = true;

            userService.getUsers(function(data){
                $scope.customers = data;
                tblState.pagination.numberOfPages = 1;
                $scope.isLoading = false;
            },function(errors){
                this.msg(errors, 'alert-danger');
            })

        }


    };

    UserSearchController.$inject = injectParams;

    orderTracker.register.controller('UserSearchController', UserSearchController);

});
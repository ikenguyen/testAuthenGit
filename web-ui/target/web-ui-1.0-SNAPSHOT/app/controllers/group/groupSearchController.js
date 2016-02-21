'use strict';

define(['order-tracker'], function (orderTracker) {

    var injectParams = ['$scope','$controller','groupService'];

    var GroupSearchController = function ($scope, $controller, groupService) {

        var args = {
                $scope: $scope,
                title : "Group.Search",
                lang : "Group"
        };
        angular.extend(this, $controller('simpleController', args));
        var self = this;
        $scope.groups = [];

        $scope.getData = function(tblState) {
            $scope.isLoading = true;

            groupService.getGroups(function(data){
                $scope.groups = data;
                tblState.pagination.numberOfPages = 1;
                $scope.isLoading = false;
            },function(errors){
                this.msg(errors, 'alert-danger');
            })

        }


    };

    GroupSearchController.$inject = injectParams;

    orderTracker.register.controller('GroupSearchController', GroupSearchController);

});
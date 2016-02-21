'use strict';

define(['order-tracker'], function (orderTracker) {

    var injectParams = ['$scope','$controller','customerService'];

    var CustomerSearchController = function ($scope, $controller, customerService) {

        var args = {
                $scope: $scope,
                title : "Customer.Search",
                lang : "Customer"
        };
        angular.extend(this, $controller('simpleController', args));
        var self = this;
        $scope.customers = [];
        $scope.displayed

        $scope.getData = function(tblState) {
            $scope.isLoading = true;

            customerService.getCustomers(function(data){
                $scope.customers = data;
                tblState.pagination.numberOfPages = 1;
                $scope.isLoading = false;
            },function(errors){
                this.msg(errors, 'alert-danger');
            })

        }


    };

    CustomerSearchController.$inject = injectParams;

    orderTracker.register.controller('CustomerSearchController', CustomerSearchController);

});
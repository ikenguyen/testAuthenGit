'use strict';

define(['order-tracker'], function (orderTracker) {

    var injectParams = ['$scope','$controller', '$location', '$route','modalService', 'groupService'];

    var InventoryItemSearchController = function ($scope, $controller, $location, $route, modalService, groupService) {


        var args = {
                    $scope: $scope,
                    title : "Customer.Search",
                    lang : "Customer"
            };
            angular.extend(this, $controller('simpleController', args));
            var self = this;
            $scope.inventoryItems = [];
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

    InventoryItemSearchController.$inject = injectParams;
    orderTracker.register.controller('InventoryItemSearchController', InventoryItemSearchController);
    console.log('using modalSerivce InventoryItemSearchController');

});
'use strict';

define(['order-tracker'], function (orderTracker) {

    var injectParams = ['$scope','$controller', '$location', '$route','modalService', 'InventoryItemService'];

    var InventoryItemController = function ($scope, $controller, $location, $route, modalService, InventoryItemService) {

            var args = {
                $scope: $scope,
                title : "Inventory Item",
                lang : "InventoryItem"
            };

            $scope.detailAddress = {
                title : 'Address',
                headerTpl : 'app/views/customer/tableHeader.html',
                bodyTpl : 'app/views/customer/tableBody.html',
                defaultIndex : 1
            };

            angular.extend(this, $controller('simpleController', args));
            angular.extend(this, $controller('DetailController', {$scope : $scope, container : $scope.detailAddress }));


            var cusCtrl = this;

            var validationMsgTable = {
                "cusForm.name.$error.required" : "Customer.Errors.RequiredName",
                "cusForm.phone.$error.required" : "Customer.Errors.RequiredPhone",
                "cusForm.phone.$error.pattern" : "Customer.Errors.InvalidPattern",
                "cusForm.city.$error.required" : "Customer.Errors.RequiredCity",
                "cusForm.country.$error.required" : "Customer.Errors.RequiredCountry"
            };

            var validationDetailTable = {
                "detailForm.party.$error.required" : "Address.Errors.RequiredParty",
            }

            $scope.customer = {};
            $scope.addrs = [];
            $scope.countries = ['Viet nam', 'Han quoc'];
            $scope.cities = ['Ho chi minh', 'Ha noi', 'Da nang'];
            $scope.phonePattern = /^\(?(\d{3})\)?[ .-]?(\d{3})[ .-]?(\d{4})$/;


            cusCtrl.loadData = function(code) {
                if(code){
                    customerService.getCustomer(code, function(data){
                        $scope.customer = data;

                        $scope.detailAddress.itemList = data.addressList;

                        angular.forEach($scope.detailAddress.itemList, function(value, key){
                            value.readOnly = true;
                        });
                    }, function(error){
                        cusCtrl.msg(error, 'alert-danger');
                    });

                }

            };

            cusCtrl.createNewItem = function() {
                var index = 0;
                angular.forEach($scope.detailAddress.itemList, function(value, key) {
                    if(index < value.index) {
                        index = value.index;
                    }
                });

                return {
                    "index"  : index + 1,
                    "partyName"  : "",
                    "address": "",
                    "city"   : "",
                    "phone"  : "",
                    "email"  : "",
                    "defaultAddr" : true,
                    "status" : true
                };
            };

            cusCtrl.doValidate = function() {

                var errors = cusCtrl.validate(validationMsgTable, function(errors){
                    if(!$scope.customer.city) {
                        errors.push('Customer.Errors.RequiredCity');
                    }

                    if(!$scope.customer.country) {
                        errors.push('Customer.Errors.RequiredCountry');
                    }
                });

                if(errors.length) {
                    cusCtrl.msg(errors, "alert-danger");
                    return false;
                }

                return true;
            };

            cusCtrl.doValidateDetail = function() {
                var errors = [];
                var validateItem = $scope.detailAddress.sltItem;

                if(!validateItem.partyName) {
                    errors.push("Address.Errors.RequiredParty");
                }

                if(!validateItem.address) {
                    errors.push("Address.Errors.RequiredAddress");
                }

                if(!validateItem.city) {
                    errors.push("Address.Errors.RequiredCity");
                }

                if(!validateItem.phone) {
                    errors.push("Address.Errors.RequiredPhone");
                } else {
                    if(!$scope.phonePattern.test(validateItem.phone)){
                        errors.push("Address.Errors.MismatchPhonePattern");
                    }
                }

                if(errors.length) {
                    cusCtrl.msgDetail(errors, "alert-danger");
                    return false;
                } else {
                    cusCtrl.msgDetail();
                    return true;
                }

            }

            cusCtrl.doCreate = function(){
                $scope.customer.addressList = [];
                angular.forEach($scope.detailAddress.itemList, function(value, key){
                    $scope.customer.addressList.push(value);
                });

                customerService.createCustomer($scope.customer,
                    function (data){
                         if(data.code){
                            cusCtrl.go('/customer/' + data.code);
                         }
                    },
                    function (data){
                        cusCtrl.msg(data.errorCode, "alert-danger");
                        return;
                    });

            };

            cusCtrl.doUpdate = function(){
                $scope.customer.addressList = [];
                angular.forEach($scope.detailAddress.itemList, function(value, key){
                    $scope.customer.addressList.push(value);
                });

                customerService.updateCustomer($scope.customer,
                    function (data){
                         if(data.code){
                            cusCtrl.go('/customer/' + data.code);
                         }
                    },
                    function (errorCode){
                        cusCtrl.msg(data.errorCode, "alert-danger");
                        return;
                });

            };

            cusCtrl.doDelete = function(customer){
                modalService.showModal().then(function (result) {
                    if (result === 'ok') {
                        customerService.deleteCustomer(customer.code, function(data){
                            if(data) {
                                cusCtrl.go('/customer/search');
                            }
                        }, function(errorCode){
                            cusCtrl.msg(data.errorCode, "alert-danger");
                            return;
                        });
                    }
                });
            };

            cusCtrl.defaultAddrUpdated = function(selectedIdx) {
                if(selectedIdx) {
                    angular.forEach($scope.detailAddress.itemList, function(value, key){
                        if(value.index == selectedIdx) {
                            value.defaultAddr = true;
                        } else {
                            value.defaultAddr = false;
                        }
                    });
                }
            }

            $scope.$broadcast('controller.ready', this);
            this.isCreate() && ($scope.title = "Inventory.ItemCreate");
            this.isUpdate() && ($scope.title = "Inventory.ItemSearch");

        };

    };

    InventoryItemController.$inject = injectParams;
    orderTracker.register.controller('InventoryItemController', InventoryItemController);

});
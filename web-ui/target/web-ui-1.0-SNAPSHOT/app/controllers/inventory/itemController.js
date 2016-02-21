'use strict';

define(['order-tracker'], function (orderTracker) {

    var injectParams = ['$scope','$controller', '$location', '$route','modalService', 'groupService'];

    var GroupController = function ($scope, $controller, $location, $route, modalService, groupService) {
        var args = {
            $scope: $scope,
            title : "Group",
            lang : "Group"
        };

        $scope.detailRoles = {
            title : 'Group',
            headerTpl : 'app/views/group/tableHeader.html',
            bodyTpl : 'app/views/group/tableBody.html'
        };

        angular.extend(this, $controller('simpleController', args));
        angular.extend(this, $controller('DetailController', {$scope : $scope, container : $scope.detailRoles}));


        var grpCtrl = this;

        var validationTable = {
            "groupForm.name.$error.required" : "Group.Errors.RequiredName"
        }

        $scope.group = {};
        $scope.roles = [];

        var loadedRoles = [];


        grpCtrl.loadData = function(id) {
            if(id){
                groupService.getGroup(id, function(data){
                    $scope.group = data;
                }, function(error){
                    grpCtrl.msg(error, 'alert-danger');
                    return;
                });

                groupService.getRolesForGroup(id, function(data){
                    $scope.detailRoles.itemList = data;

                    angular.forEach($scope.detailRoles.itemList, function(value, key){
                        value.readOnly = true;
                    });
                }, function(error){
                    grpCtrl.msg(error, 'alert-danger');
                    return
                });
            }
        };

        grpCtrl.doValidate = function() {

            var errors = grpCtrl.validate(validationTable);

            if(errors.length) {
                grpCtrl.msg(errors, "alert-danger");
                return false;
            }

            return true;
        };

        grpCtrl.doCreate = function(){
            $scope.group.roles = [];
            angular.forEach($scope.detailRoles.itemList, function(value, key){
                $scope.group.roles.push(value.id);
            });

            groupService.createGroup($scope.group,
                function (data){
                     if(data.id){
                        grpCtrl.go('/group/' + data.id);
                     }
                },
                function (data){
                    grpCtrl.msg(data.errorCode, "alert-danger");
                    return;
                });

        };

        grpCtrl.doUpdate = function(){
            $scope.group.roles = [];
            angular.forEach($scope.detailRoles.itemList, function(value, key){
                $scope.group.roles.push(value.id);
            });

            groupService.updateGroup($scope.group,
                function (data){
                     if(data.id){
                        grpCtrl.go('/group/' + data.id);
                     }
                },
                function (data){
                    grpCtrl.msg(data.errorCode, "alert-danger");
                    return;
            });

        };

        grpCtrl.doDelete = function(group){

            modalService.showModal().then(function (result) {
                if (result === 'ok') {
                    groupService.deleteGroup(group.id, function(data){
                        if(data) {
                            grpCtrl.go('/group/search');
                        }
                    }, function(errorCode){
                        grpCtrl.msg(data.errorCode, "alert-danger");
                        return;
                    });
                }
            });
        };

        grpCtrl.createNewItem = function(){
            return {
                "id"  : "",
                "name": "",
                "desc": "",
                "readOnly":false
            };
        };

        groupService.getRoles(function(roles){
            var filteredData = roles.filter(function(role){
                var notExisted = true;

                angular.forEach($scope.detailRoles.itemList, function(key, value){
                    if(key.id == role.id){
                        notExisted = false;
                    }
                });
                return notExisted;
            })
            grpCtrl.loadedRoles = filteredData;

        }, function(error){

            grpCtrl.msg(error, 'alert-danger');

        })




        $scope.$broadcast('controller.ready', this);

        this.isCreate() && ($scope.title = "Group.Create");
        this.isUpdate() && ($scope.title = "Group.Update");

    };

    GroupController.$inject = injectParams;
    orderTracker.register.controller('GroupController', GroupController);
    console.log('using modalSerivce');

});
'use strict';

define(['order-tracker'], function (orderTracker) {

    var injectParams = ['$scope','$controller', '$location', '$route', 'modalService', 'userService', 'groupService'];

    var UserController = function ($scope, $controller, $location, $route, modalService, userService, groupService) {
        var args = {
            $scope: $scope,
            title : "User",
            lang : "User"
        };

        $scope.detailGroups = {
            title : 'Group',
            headerTpl : 'app/views/user/tableHeader.html',
            bodyTpl : 'app/views/user/tableBody.html'
        };

        angular.extend(this, $controller('simpleController', args));
        angular.extend(this, $controller('DetailController', {$scope : $scope, container : $scope.detailGroups}));


        var usrCtrl = this;

        var validationMsgTable = {
            "userForm.userName.$error.required" : "User.Errors.RequiredUserName",
            "userForm.fullName.$error.required" : "User.Errors.RequiredFullName",
            // TODO : need to verify if password is at least 6 char, contains 1 number and 1 uppercase
            //"userForm.password.$error.pattern" : "User.Errors.PasswordComplexity",
            // TODO : won't alter error when email leave empty, need to fix later
            "userForm.email.$error.email"       : "User.Errors.email"
        };

        var validationCreateMsgTable = {
            "userForm.password.$error.required" : "User.Errors.RequiredPassword"
        }

        $scope.user = {};
        $scope.groups = [];

        var loadedGroups = [];


        usrCtrl.loadData = function(id) {
            if(id){
                userService.getUser(id, function(data){
                    $scope.user = data;
                }, function(error){
                    usrCtrl.msg(error, 'alert-danger');
                });

                userService.getGroupsForUser(id, function(data){
                    $scope.detailGroups.itemList = data;

                    angular.forEach($scope.detailGroups.itemList, function(value, key){
                        value.readOnly = true;
                    });
                }, function(error){
                    usrCtrl.msg(error, 'alert-danger');
                    return;
                });
            }

        };

        usrCtrl.createNewItem = function() {
            return {
                "id"  : "",
                "name": "",
                "desc": ""
            };
        };

        groupService.getGroups(function(data){
            usrCtrl.loadedGroups = data;
        }, function(errorCode){
            usrCtrl.msg(error, 'alert-danger');
            return;
        })



        usrCtrl.doValidate = function() {

            var validationTable = usrCtrl.isCreate() ? angular.merge({}, validationMsgTable, validationCreateMsgTable)
                                        : validationMsgTable;

            var errors = usrCtrl.validate(validationTable, function(errors){
                if ($scope.user.password != $scope.user.passwordRetype) {
                    errors.push('User.Errors.MismatchPassword');
                }
            });

            if(errors.length) {
                usrCtrl.msg(errors, "alert-danger");
                return false;
            }

            return true;
        };

        usrCtrl.doCreate = function(){

            $scope.user.groups = [];
            angular.forEach($scope.detailGroups.itemList, function(value, key){
                $scope.user.groups.push(value.id);
            });

            userService.createUser($scope.user,
                function (data){
                     if(data.id){
                        usrCtrl.go('/user/' + data.id);
                     }
                },
                function (data){
                    usrCtrl.msg(data.errorCode, "alert-danger");
                    return;
                });

        };

        usrCtrl.doUpdate = function(){

            $scope.user.groups = [];
            angular.forEach($scope.detailGroups.itemList, function(value, key){
                $scope.user.groups.push(value.id);
            });
            userService.updateUser($scope.user,
                function (data){
                     if(data.id){
                        usrCtrl.go('/user/' + data.id);
                     }
                },
                function (data){
                    usrCtrl.msg(data.errorCode, "alert-danger");
                    return;
            });

        };

        usrCtrl.doDelete = function(user){
            modalService.showModal().then(function (result) {
                if (result === 'ok') {
                    userService.deleteUser(user.id, function(data){
                        if(data) {
                            usrCtrl.go('/user/search');
                        }
                    }, function(errorCode){
                        usrCtrl.msg(data.errorCode, "alert-danger");
                        return;
                    });
                }
            });
        };

        $scope.$broadcast('controller.ready', this);

        this.isCreate() && ($scope.title = "User.Create");
        this.isUpdate() && ($scope.title = "User.Update");

    };

    UserController.$inject = injectParams;

    orderTracker.register.controller('UserController', UserController);

});
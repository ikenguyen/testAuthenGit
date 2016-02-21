'use strict';

define(['order-tracker'], function (orderTracker) {

    var injectParams = ['$scope'];

    var HomeController = function ($scope) {
        $scope.title = 'this is home controller';

        $scope.message = {
            "type" : "",
            "content" : ""
        };

        this.alert = function(msg, type){
            $scope.message.type = type | "alert-info";
            $scope.message.content = msg;
        }
    };

    HomeController.$inject = injectParams;

    orderTracker.register.controller('homeController', HomeController);

});
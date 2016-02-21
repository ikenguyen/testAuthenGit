'use strict';

define(['order-tracker'], function (orderTracker) {

    var injectParams = ['$scope'];

    var AboutController = function ($scope) {
        $scope.title = 'this is all about the bass';
    };

    AboutController.$inject = injectParams;

    orderTracker.register.controller('AboutController', AboutController);

});
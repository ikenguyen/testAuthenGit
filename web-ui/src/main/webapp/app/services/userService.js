'use strict';

define(['order-tracker'], function (orderTracker) {

    var injectParams = ['$http', '$q'];

    var userFactory = function ($http, $q) {
        var serviceBase = '/user/',
            factory = {};

        factory.getUsers = function(successCallback, errorCallback){
            return $http.get(serviceBase + 'all/',{
                transformResponse: function(data, headersGetter, status) {
                    if (status != '200') {
                        return {errorCode : data};
                    }else{
                        var parsed = JSON.parse(data);
                        return parsed;
                    }
                }
            })
            .then(function (response) {
                successCallback && successCallback(response.data);
            }, function(response){
                errorCallback && errorCallback(response);
            });
        }

        factory.createUser = function (user,successCallback,errorCallback) {
            return $http.post(serviceBase, user,{
                transformResponse: function(data, headersGetter, status) {
                    if (status == '200') {
                        return {id : data}
                    } else {
                        return {errorCode : data};
                    }
                }
            })
            .then(function (results) {
                successCallback && successCallback(results.data);
            }, function(response){
                errorCallback && errorCallback(response.data);
            });
        };

        factory.updateUser = function (user, successCallback, errorCallback) {
             return $http.post(serviceBase + user.id, user,{
                transformResponse: function(data, headersGetter, status) {
                    if (status != '200') {
                        return {errorCode : data};
                    }else {
                        return JSON.parse(data);
                    }
                }
            })
            .then(function (results) {
                successCallback && successCallback(results.data);
            }, function(response){
                errorCallback && errorCallback(response.data);
            });
        };

        factory.deleteUser = function (id, successCallback, errorCallback) {
             return $http.delete(serviceBase + '/' + id,{
                transformResponse: function(data, headersGetter, status) {
                    if (status == '200') {
                        return {id : data}
                    } else {
                        return {errorCode : data};
                    }
                }
            })
            .then(function (results) {
                successCallback && successCallback(results.data);
            }, function(response){
                errorCallback && errorCallback(response.data);
            });
        };

        factory.getUser = function (id, successCallback, errorCallback) {
            return $http.get(serviceBase + id,{
                transformResponse: function(data, headersGetter, status) {
                    if (status != '200') {
                        return {errorCode : data};
                    }else{
                        var parsed = JSON.parse(data);
                        return parsed;
                    }
                }
            })
            .then(function (results) {
                successCallback && successCallback(results.data);
            }, function(response){
                errorCallback && errorCallback(response.data);
            });
        };

        factory.getGroupsForUser = function (id, successCallback, errorCallback) {
                    return $http.get(serviceBase + id + '/groups',{
                        transformResponse: function(data, headersGetter, status) {
                            if (status != '200') {
                                return {errorCode : data};
                            }else{
                                var parsed = JSON.parse(data);
                                return parsed;
                            }
                        }
                    })
                    .then(function (results) {
                        successCallback && successCallback(results.data);
                    }, function(response){
                        errorCallback && errorCallback(response.data);
                    });
                };

        return factory;
    };

    userFactory.$inject = injectParams;

    orderTracker.factory('userService', userFactory);

});
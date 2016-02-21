'use strict';

define(['order-tracker'], function (orderTracker) {

    var injectParams = ['$http', '$q'];

    var groupFactory = function ($http, $q) {
        var serviceBase = '/group/',
            factory = {};

        factory.getGroups = function(successCallback, errorCallback){
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
        };

        factory.getRoles = function(successCallback, errorCallback){
                    return $http.get(serviceBase + 'roles/',{
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

        factory.createGroup = function (group, successCallback, errorCallback) {
            return $http.post(serviceBase, group,{
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

        factory.updateGroup = function (group, successCallback, errorCallback) {
             return $http.post(serviceBase + group.id, group,{
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

        factory.deleteGroup = function (id, successCallback, errorCallback) {
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

        factory.getGroup = function (id, successCallback, errorCallback) {
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

        factory.getRolesForGroup = function (id, successCallback, errorCallback) {
            return $http.get(serviceBase + id +'/roles',{
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

    groupFactory.$inject = injectParams;

    orderTracker.factory('groupService', groupFactory);

});
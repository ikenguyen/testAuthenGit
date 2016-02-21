'use strict';

define(['order-tracker'], function (orderTracker) {

    var injectParams = ['$http', '$q'];

    var inventoryItemFactory = function ($http, $q) {
        var serviceBase = '/inventory/',
            factory = {};

        factory.getInventoryItems = function(successCallback, errorCallback){
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

        factory.createItem = function (customer,successCallback,errorCallback) {
            return $http.post(serviceBase, customer,{
                transformResponse: function(data, headersGetter, status) {
                    if (status == '200') {
                        return {code : data}
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

        factory.updateItem = function (customer, successCallback, errorCallback) {
             return $http.post(serviceBase + customer.code, customer,{
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

        factory.deleteItem = function (id, successCallback, errorCallback) {
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

        factory.getItem = function (code, successCallback, errorCallback) {
            return $http.get(serviceBase + code,{
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

    inventoryItemFactory.$inject = injectParams;

    orderTracker.factory('inventoryItemService', inventoryItemFactory);

});
'use strict';

define(['order-tracker'], function (orderTracker) {

    var injectParams = ['$scope','$parse', 'container'];

    var DetailController = function ($scope, $parse, container) {

    container.itemList = [];
    container.sltItem = {};

    container.msg = {
        type : "",
        contents : []
    };

    var detailCtrl = this;

    this.msgDetail = function(msg, type){
        container.msg.type = type || "alert-info";
              container.msg.contents = [];

              if (msg) {
                  if (angular.isArray(msg)){
                      container.msg.contents = msg;
                  } else {
                      container.msg.contents[0] = msg;
                  }
              }

          };

    this.createNewItem = function() {
        console.log('createNewItem has not been implemented yet');
        return {};
    }

    this.validate = function (validationMsgTable, moreValidation) {
        var errors = [];
        angular.forEach(validationMsgTable, function (value, key) {
            var exp = $parse(key);
            if(exp($scope)) {
                errors.push(value);
            }
        });

        (!errors.length) && moreValidation && moreValidation(errors);

        return errors;
    };

    this.doValidate = function(){
        // should implemented on the child controller
        return true;
    };

    this.doDetailOk = function(item, callback){

        var valid = this.doValidate && this.doValidate();

        if(valid) {
            item.readOnly = true;
            callback && callback(item);
        }
    };


    this.addDetail = function() {
       var item = this.createNewItem();
       item.readOnly = false;
       container.itemList.push(item);
    };

    this.editDetail = function(item){
        var idx = container.itemList.indexOf(item);
        var valid = true;
        angular.forEach(container.itemList, function(value, key){
            if(!value.readOnly){
                detailCtrl.msgDetail('Errors.UnfinishedDetail', 'alert-danger');
                valid = false;
            }
        });
        if(valid){
            container.itemList[idx].readOnly = false;
        }

    }

    this.removeDetail = function(item) {
        var idx = container.itemList.indexOf(item);
        container.itemList.splice(idx, 1);
    }

    this.onSelect = function(item){
        container.sltItem = item;
        this.doDetailOk(container.sltItem, function(item){
            container.sltItem.readOnly = true;
            container.itemList.splice(-1, 1, container.sltItem);
            $scope.$broadcast('detail.selected',item);
        });

    }


    };

    DetailController.$inject = injectParams;

    orderTracker.controller('DetailController', DetailController);

});
'use strict';

define(['order-tracker'], function (orderTracker) {

    var injectParams = ['$scope','$injector','$parse','title','lang'];

    var SimpleController = function ($scope, $injector, $parse, title, lang) {
        $scope.title = title;
        // status of the document : create, update, view
        $scope.currentState = "view";

        $scope.message = {
            type : "",
            contents : []
        };

        if (lang) {
            var translatePartialLoader = $injector.get('$translatePartialLoader');
            var translate = $injector.get('$translate');
            if (!translatePartialLoader.isPartAvailable(lang)) {
                translatePartialLoader.addPart(lang);
                translate.refresh();
            }

        };

        this.state = function(state){
             if (state == 'view' || state == 'create' || state == 'update'){
                 $scope.currentState = state;
             }
             return $scope.currentState;
        };

        this.isCreate = function(){
            return $scope.currentState == 'create';
        };

        this.isUpdate = function(){
            return $scope.currentState == 'update';
        };

        this.isView = function(){
            return $scope.currentState == 'view';
        };

        this.msg = function(msg, type){
            $scope.message.type = type || "alert-info";
            $scope.message.contents = [];

            if (msg) {
                if (angular.isArray(msg)){
                    $scope.message.contents = msg;
                } else {
                    $scope.message.contents[0] = msg;
                }
            }

        };

        this.loadData = function (id){
            console.log('loadData is not implemented yet');
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

        this.doOk = function(){

            var valid = ($scope.currentState != 'view') && this.doValidate && this.doValidate();

            if(valid) {

                this.isCreate() && this.doCreate && this.doCreate();

                this.isUpdate() && this.doUpdate && this.doUpdate();

            }
        };

        this.doCancel = function() {
            $injector.get('$window').history.back();
        };

        // TODO : if we use go inside html like [go("/path/{{data}}")] we got error because {{data}} won't be resolved in time
        this.go = function(path) {
            $injector.get('$location').path(path);
        }

        $scope.$on('controller.ready', function(event, sub){
            var routeParams = $injector.get('$routeParams');
            var paths = $injector.get('$location').path().split('/');
            sub.state(paths[2]);
            (sub.isCreate) && sub.loadData && sub.loadData(routeParams.id);
        })

    };

    SimpleController.$inject = injectParams;

    orderTracker.controller('simpleController', SimpleController);

});
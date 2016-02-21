'use strict';

define(['services/routeResolver'], function(){

    var orderTrackerApp = angular.module('orderTracker', ['ngRoute','routeResolverServices', 'ui.bootstrap',
                                            'pascalprecht.translate','smart-table','ngSanitize', 'ui.select']);

        orderTrackerApp.config(['$locationProvider','$routeProvider', 'routeResolverProvider', '$controllerProvider',
                        '$compileProvider', '$filterProvider', '$provide', '$httpProvider', '$translateProvider',
                        'uiSelectConfig',

                function ($locationProvider, $routeProvider, routeResolverProvider, $controllerProvider,
                          $compileProvider, $filterProvider, $provide, $httpProvider,$translateProvider,uiSelectConfig){

                    $locationProvider.html5Mode(false);

                    orderTrackerApp.routeProviderRef = $routeProvider;

                    //Change default views and controllers directory using the following:
                    //routeResolverProvider.routeConfig.setBaseDirectories('/app/views', '/app/controllers');

                    orderTrackerApp.register =
                    {
                        controller: $controllerProvider.register,
                        directive: $compileProvider.directive,
                        filter: $filterProvider.register,
                        factory: $provide.factory,
                        service: $provide.service
                    };

                    //Define routes - controllers will be loaded dynamically
                    orderTrackerApp.route = routeResolverProvider.route;
                    //route.resolve() now accepts the convention to use (name of controller & view) as well as the
                    //path where the controller or view lives in the controllers or views folder if it's in a sub folder.
                    //For example, the controllers for customers live in controllers/customers and the views are in views/customers.
                    //The controllers for orders live in controllers/orders and the views are in views/orders
                    //The second parameter allows for putting related controllers/views into subfolders to better organize large projects


                    $translateProvider.useLoader('$translatePartialLoader', {
                      urlTemplate: 'resources/public/lang/{part}/{lang}.json'
                    });
                    $translateProvider.preferredLanguage('vi');
                    uiSelectConfig.theme = 'bootstrap';

             $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

        }]);

        orderTrackerApp.run(function(auth, $http, $translatePartialLoader, $translate) {

            $http.get("resources/public/json/navigation.json")
                .success(function(data)
                {
                      // contains value menu+ subs from navigation.js
                      orderTrackerApp.sidebar = [];
                      // initialize menu object
                      var menu={};
                      // build menuItem and subsItem from navigation.json base on keys[menu, subs]
                      angular.forEach(data, function (value, key)
                      {
                             if(value.menu){
                                menu = value.menu;
                                if(value.subs){
                                menu.subs = [];
                                    angular.forEach(value.subs,function(subMenu){
                                        if(subMenu.menu){
                                            menu.subs.push(subMenu.menu);
                                        }
                                    });
                                }
                                orderTrackerApp.sidebar.push(menu);
                             }

                            var resolvedParent = orderTrackerApp.route.resolve(value.name,value.path,value.alias);
                            orderTrackerApp.routeProviderRef.when(value.url, resolvedParent);
                            angular.forEach(value.subs, function (sub)
                            {
                                var subName = value.name + sub.name;
                                var subUrl  = value.url + sub.url;
                                var resolvedChild = (sub.name == '^') ? resolvedParent
                                            : orderTrackerApp.route.resolve(subName,sub.path || value.path,sub.alias);
                                orderTrackerApp.routeProviderRef.when(subUrl,resolvedChild);
                            });
                      });

                });
            $translatePartialLoader.addPart('Common');
            $translate.refresh();
            // Initialize auth module with the home page and login/logout path
            // respectively
            auth.init('/home', '/login', '/logout');
        });

        orderTrackerApp.controller('orderTrackerController',
            function($scope, $rootScope, auth){
                var self = this;

                $scope.layout = {
                    "header" : "app/views/header.html",
                    "sidebar" : "",
                    "footer" : "app/views/footer.html",
                    "theme"  : "login-layout light-login"
                };

                $rootScope.$on('user.authenticated', function(){
                    $scope.layout.sidebar = "app/views/sidebar.html";
                    $scope.layout.theme = "no-skin";
                });

            }
        );

        orderTrackerApp.filter('capitalize', function() {
            return function(token) {
                return token.charAt(0).toUpperCase() + token.slice(1);
            }
        });

      return orderTrackerApp;
});



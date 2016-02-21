'use strict';

define(['order-tracker'], function (orderTracker) {

    var injectParams = ['$scope','auth'];

    var SidebarController = function ($scope,auth) {
            // TODO : update when demo done
            // check authenticated from auth service
            // build sidebar from json
            if(auth.authenticated){
                     $scope.menus = orderTracker.sidebar;
            }

            // initialize selectedItem to open class default
            // TODO : will bind with constant to easy to configure
            $scope.selectedItem = 0;
            $scope.selectedSubItem = 0;
            /*
            set selectedItem index when action clicked on menu item.
            Param : index from $index.
             */
            $scope.selectedMenu = function (index){
                $scope.selectedItem = index;
            }

            $scope.selectedSubMenu = function(index){
                $scope.selectedSubItem = index;
            }
    };

    SidebarController.$inject = injectParams;

    orderTracker.controller('sidebarController', SidebarController);

});
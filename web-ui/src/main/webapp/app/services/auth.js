'use strict';

define(['order-tracker'], function(orderTracker){

    var injectParams = ['$rootScope', '$http', '$location','$route'];

    var authFactory = function($rootScope, $http, $location,$route) {

          var auth = {

              authenticated : false,

              loginPath : '/login',
              logoutPath : '/logout',
              homePath : '/',
              path : $location.path(),

              authenticate : function(credentials, callback) {

                  var headers = credentials && credentials.username ? {
                      authorization : "Basic "
                              + btoa(credentials.username + ":"
                                      + credentials.password)
                  } : {};

                  $http.get('user', {
                      headers : headers
                  }).success(function(data) {
                      if (data.userName) {
                          auth.authenticated = true;
                          $rootScope.$broadcast('user.authenticated')
                      } else {
                          auth.authenticated = false;
                      }
                      callback && callback(auth.authenticated);
                      $location.path(auth.path==auth.loginPath ? auth.homePath : auth.path);
                  }).error(function() {
                      auth.authenticated = false;
                      callback && callback(false);
                  });

              },

              enter : function() {
                    if ($location.path() != auth.loginPath) {
                        auth.path = $location.path();
                        if (!auth.authenticated) {
                            $location.path(auth.loginPath);
                        }
                    }
              },

              clear : function() {
                  $location.path(auth.loginPath);
                  auth.authenticated = false;
                  $http.post(auth.logoutPath, {}).success(function() {
                      console.log("Logout succeeded");
                  }).error(function(data) {
                      console.log("Logout failed");
                  });
              },

              init : function(home, login, logout) {
                  auth.homePath = home;
                  auth.loginPath = login;
                  auth.logoutPath = logout;

                  auth.authenticate({}, function(authenticated) {
                      if (authenticated) {
                          $location.path(auth.path);
                      } else {
                          $location.path(auth.loginPath);
                      }
                      $route.reload();

                  })

                  // Guard route changes and switch to login page if unauthenticated
                    $rootScope.$on('$routeChangeStart', function() {
                        auth.enter();
                    });

              }

          };

          return auth;

      };

    authFactory.$inject = injectParams;

    orderTracker.factory('auth', authFactory);

});



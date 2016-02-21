require.config({
    baseUrl: 'app',
    urlArgs: 'v=1.0'
});

require(
    [
        'order-tracker',
        'SidebarController',
        'SimpleController',
        'DetailController',
        'services/routeResolver',
        'services/auth',
        'services/httpInterceptors',
        'services/modalService',
        'services/userService',
        'services/groupService',
        'services/customerService'
    ],
    function () {
        angular.bootstrap(document, ['orderTracker']);
    });

app.config([ '$routeProvider', function($routeProvider) {
	$routeProvider
	.when('/login', {
		templateUrl : '/todolist/views/login.html',
		controller : 'loginController'
	})
	.when('/user', {
		templateUrl : '/todolist/views/user/user-zaklad.html',
		controller : 'userController'
	})
	.when('/admin', {
		templateUrl : '/todolist/views/admin/admin-zaklad.html',
		controller : 'adminController'
	})
	.when('/notAllowed', {
		templateUrl : '/todolist/views/notAllowed.html',
		controller : 'notAllowedController'
	})
	.otherwise({
		redirectTo : '/login'
	});
} ]);
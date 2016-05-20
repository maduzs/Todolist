app.controller('loginController', [
		'$scope',
		'$http',
		'$location',
		'$window',
		'USER_ROLES',
		'AuthService',
		'urls',
		'errorMessages',
		function($scope, $http, $location, $window, USER_ROLES, AuthService, urls, errorMessages) {
			
			$scope.currentUser = null;
			$scope.userRoles = USER_ROLES;
			$scope.isAuthorized = AuthService.isAuthorized;
			 
			$scope.setCurrentUser = function (user) {
			  $scope.currentUser = user;
			};
			  
			$scope.credentials = {
				    username: '',
				    password: ''
			};
			
			$scope.login = function (credentials) {
				$scope.dataLoading = true;
				/*AuthService.login(credentials).then(function (user) {
				      //$rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
				      $scope.setCurrentUser(user);

						$scope.dataLoading = false;
				    }, function () {
				      //$rootScope.$broadcast(AUTH_EVENTS.loginFailed);

						$scope.dataLoading = false;
				    });*/
				var authorize = {
						method : 'POST',
						url : urls.urlAuthorize,
						headers : {
							'Content-Type': 'application/json'
						},
						data : {
							user : credentials
						} 
					}
					
					$http(authorize).then(function successCallback(response) {
						
					}, function errorCallback(response) {
						$scope.incorrectCredentials = true;
						$scope.dataLoading = false;
					});
			
			};
				  
			$scope.incorrectLogin = errorMessages.incorrectLogin;
			
			$scope.submit = function() {
				$scope.incorrectCredentials = false;
				$scope.dataLoading = true;
				var authorize = {
					method : 'POST',
					url : urls.urlAuthorize,
					headers : {
						'Content-Type': 'application/json'
					},
					data : {
						user : $scope.user
					} 
				}
				
				$http(authorize).then(function successCallback(response) {
					
					var params = angular.fromJson(response.data);
					
					var xmlResp = x2js.xml_str2json(response.data);
					uidService.setUid(xmlResp["xml-uid"].uid);
					
					$window.sessionStorage.token = xmlResp["xml-uid"].uid;
					
					$http.defaults.headers.common.Authorization = 'Bearer ' + response.token;
					
					$location.path("/privilege");
					
				}, function errorCallback(response) {
					$scope.incorrectCredentials = true;
					$scope.dataLoading = false;
				});

			};

		}]);

/* alebo

function login() {
vm.loading = true;
AuthenticationService.Login(vm.username, vm.password, function (result) {
    if (result === true) {
        $location.path('/');
    } else {
        vm.error = 'Username or password is incorrect';
        vm.loading = false;
    }
});
};

*/
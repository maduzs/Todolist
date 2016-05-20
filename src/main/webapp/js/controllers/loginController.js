app.controller('loginController', [
		'$scope',
		'$http',
		'$location',
		'$window',
		'uidService',
		'privilegeService',
		'urls',
		'errorMessages',
		function($scope, $http, $location, $window, uidService, privilegeService, urls, errorMessages) {

			$scope.incorrectLogin = errorMessages.incorrectLogin;
			
			$scope.credentials = {
				    login: '',
				    password: ''
			};
			
			$scope.login = function (credentials) {
				$scope.dataLoading = true;

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
						$scope.credentials.password = "";
						
						var uuid = angular.fromJson(response.data.uuid);
						uidService.setUid(uuid);
						$window.sessionStorage["uuid"] = uuid;
						
						$scope.dataLoading = false;

						privilegeService.setPrivilege("user", true);
						
						$location.path("/user");
						
					}, function errorCallback(response) {
						$scope.incorrectCredentials = true;
						$scope.dataLoading = false;
					});
			
			};

		}]);
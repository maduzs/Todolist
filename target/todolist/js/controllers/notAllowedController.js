app.controller('notAllowedController', [ '$scope', 'errorMessages', function ($scope, errorMessages) {
	
	$scope.notAllowed = errorMessages.notAllowed;
	
}]);
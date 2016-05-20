var app = angular.module('todolist', ['ngRoute', 'ui.bootstrap']);

// localhost
app.constant("urls", {
	urlAuthorize : '/todolist/rest/users/register',
	urlSaveItem : 'http://localhost:8080/todolist/rest/items/save',
	urlDeleteItem : 'http://localhost:8080/todolist/rest/items/delete'
});

app.constant("errorMessages", {
	incorrectLogin : 'Nespávne údaje',
	notAllowed : 'Nemáte oprávnenia na vstup'
});

app.constant('USER_ROLES', {
	  admin: 'admin',
	  user: 'user'
});


app.run(['$rootScope', '$location', 'uidService', 'privilegeService', '$routeParams', function ($rootScope, $location, uidService, privilegeService, $routeParams) {
    $rootScope.$on('$routeChangeStart', function (event, next, current) {
        if (!uidService.isLoggedIn()) {
            console.log('DENY');
            //event.preventDefault();
            
            $location.path('/login');     
        }
        else {
        	if (next.$$route == undefined)
        		return;
        	switch (next.$$route.originalPath){
        	case ("/" + privilegeService.getPrivilegeNames().privName1) : {
        		if (!privilegeService.getPrivileges().priv1)
                    $location.path('/notAllowed');
        		break;
        	}
        	case ("/" + privilegeService.getPrivilegeNames().privName2) : {
        		if (!privilegeService.getPrivileges().priv2)
                    $location.path('/notAllowed');
        		break;
        	}
        	default : {
                console.log('ALLOW');
        	}
        	}
        }
    });
}]);

app.factory('uidService', function() {
	var uid;
	return {
		setUid : function(aUid) {
			uid = aUid;
		},
		isLoggedIn : function() {
			return (!angular.isUndefined(uid)) ? true : false;
		},
		getUid : function(){
			return uid;
		}
	}
});

app.factory('privilegeService', function() {
	var privileges= {
			priv1 : false,
			priv2 : false
	};
	var privilegeNames = {
			privName1 : "pleb",
			privName2 : "admin"
	}
	return {
		setPrivilege : function(priv, val) {
			switch(priv) {
	    	case privilegeNames.privName1:
	    		privileges.priv1 = val;
	    		break;
	    	case privilegeNames.privName2:
	    		privileges.priv2 = val;
	    		break;
	    	case privilegeNames.privName3:
	    		privileges.priv3 = val;
	    		break;
			}
		},
		getPrivileges : function(){
			return privileges;
		},
		getPrivilegeNames : function(){
			return privilegeNames;
		}
	}
});

app.factory('AuthenticationService', function() {
        var service = {};
 
        service.Login = Login;
        service.Logout = Logout;
 
        return service;
 
        function Login(username, password, callback) {
            $http.post('/api/authenticate', { username: username, password: password })
                .success(function (response) {
                    // login successful if there's a token in the response
                    if (response.token) {
                        // store username and token in local storage to keep user logged in between page refreshes
                        $localStorage.currentUser = { username: username, token: response.token };
 
                        // add jwt token to auth header for all requests made by the $http service
                        $http.defaults.headers.common.Authorization = 'Bearer ' + response.token;
 
                        // execute callback with true to indicate successful login
                        callback(true);
                    } else {
                        // execute callback with false to indicate failed login
                        callback(false);
                    }
                });
        }
 
        function Logout() {
            // remove user from local storage and clear http auth header
            delete $localStorage.currentUser;
            $http.defaults.headers.common.Authorization = '';
        }
 });

app.factory('AuthService', function ($http, Session) {
	  var authService = {};
	 
	  authService.login = function (credentials) {
	    return $http
	      .post('/todolist/rest/users/register', credentials)
	      .then(function (res) {
	        Session.create(res.data.id, res.data.user.id,
	                       res.data.user.role);
	        return res.data.user;
	      });
	  };
	 
	  authService.isAuthenticated = function () {
	    return !!Session.userId;
	  };
	 
	  authService.isAuthorized = function (authorizedRoles) {
	    if (!angular.isArray(authorizedRoles)) {
	      authorizedRoles = [authorizedRoles];
	    }
	    return (authService.isAuthenticated() &&
	      authorizedRoles.indexOf(Session.userRole) !== -1);
	  };
	 
	  return authService;
})

app.service('Session', function () {
  this.create = function (sessionId, userId, userRole) {
    this.id = sessionId;
    this.userId = userId;
    this.userRole = userRole;
  };
  this.destroy = function () {
    this.id = null;
    this.userId = null;
    this.userRole = null;
  };
})



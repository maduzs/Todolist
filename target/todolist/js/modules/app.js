var app = angular.module('todolist', ['ngRoute', 'ui.bootstrap']);

// localhost
app.constant("urls", {
	urlAuthorize : '/todolist/rest/users/login',
	urlGetItems : '/todolist/rest/item/',
	urlSaveItem : '/todolist/rest/item/insert',
	urlUpdateItem : '/todolist/rest/item/update',
	urlDeleteItem : '/todolist/rest/item/delete'
});

app.constant("errorMessages", {
	incorrectLogin : 'Nespávne údaje',
	notAllowed : 'Nemáte oprávnenia na vstup'
});

app.run(['$rootScope', '$location', 'privilegeService', 'uidService', '$routeParams', function ($rootScope, $location, privilegeService , uidService, $routeParams) {
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
			priv2 : false,
	};
	var privilegeNames = {
			privName1 : "user",
			privName2 : "admin",
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


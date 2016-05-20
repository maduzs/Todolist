app.controller('userController', [ '$scope', '$http', '$filter', 'urls', 'uidService', 
                                   function ($scope, $http, $filter, urls, uidService) {
	
	$scope.itemsDataLoading = false;
	$scope.itemDeleteConfirm = false;
	$scope.newItemInsert = false; 
	
	$scope.items = [];
	$scope.sortedItems = []; 
	
	$scope.sortType     = 'createdOn'; 
	$scope.sortReverse  = false; 
	
	$scope.prehlad = {
		column1 : 'Por. číslo',
		column2 : 'Obsah',
		column3 : 'Vytvorený',
		column4 : 'Stav',
		
		button1 : "Editácia",
		button2 : "Zrušiť",
		button3 : "Uložiť",
		button4 : "Vymazať",
		button5 : "Nie",
		button6 : "Áno",
		button7 : "Pridať",
		button8 : "Potvrďiť",
	
	}
	
	$scope.setNewItem = function() {
		$scope.newItem = {
				id : 0,
				content : "",
				createdOn : new Date(),
				completed : false
		}
	}
	
	$scope.getItems = function(newLoad) {
		$scope.itemsDataLoading = true;
		$scope.items = [];
        
		var getItems = {
				method : 'GET',
				url : urls.urlGetItems,
				headers : {
					'Content-Type': 'application/json',
					'RestUid' : uidService.getUid()
				}
			}
			
			$http(getItems).then(function successCallback(response) {
				for (var i=0; i<response.data.length; i++){
					response.data[i].number = i;
					response.data[i].disabled = true;
					$scope.items.push(response.data[i]);
				}
				$scope.sort('refresh');
				
				$scope.itemsDataLoading = false;
				
			}, function errorCallback(response) {
				$scope.itemsDataLoading = false;
			});
	}
	
	$scope.sendItem = function(itemQuery) {
		$scope.itemsDataLoading = true;
		$http(itemQuery).then(function successCallback(response) {
			if (response.status == 200){
				$scope.getItems(false);
			}
			else{
				$scope.itemsDataLoading = false;
			}
		}, function errorCallback(response) { 

			$scope.itemsDataLoading = false;
		});	
	}
	
	$scope.insertNewItem = function() {
		delete $scope.newItem['disabled']; 
		delete $scope.newItem['number'];
		var insertItem = {
				method : 'POST',
				url : urls.urlSaveItem,	
				headers : {
					'Content-Type': 'application/json',
					'RestUid' : uidService.getUid()
				},
				data: {
					newItem : $scope.newItem
				}
		}
		$scope.sendItem(insertItem);
	}
	
	$scope.updateItem = function(item){
		$scope.prepareItemForSend(item)
		var updateItem = {
				method : 'POST',
				url : urls.urlUpdateItem,	
				headers : {
					'Content-Type': 'application/json',
					'RestUid' : uidService.getUid()
				},
				data: {
					newItem : $scope.newItem
				}
		}
		
		$scope.sendItem(updateItem);
	}
	
	$scope.deleteItem = function(item){
		$scope.prepareItemForSend(item)
		
		var deleteItem = {
				method : 'POST',
				url : urls.urlDeleteItem,	
				headers : {
					'Content-Type': 'application/json',
					'RestUid' : uidService.getUid()
				},
				data: {
					newItem : $scope.newItem
				}
		} 
		$scope.sendItem(deleteItem);
	}
	
	$scope.prepareItemForSend = function(item){
		if (item.content === undefined)
			item.content = "";
		item.disabled = true;
		$scope.newItem = JSON.parse(JSON.stringify(item));
		
		var date = new Date($scope.newItem.createdOn * 1000);
		$scope.newItem.createdOn = new Date(); 
		delete $scope.newItem['disabled']; 
		delete $scope.newItem['number'];
	}
	
	$scope.editing = function(id){
		$scope.newItemInsert = false; 
		var index = $scope.getLocalId(id);
		$scope.clear(index)
		$scope.newItem = JSON.parse(JSON.stringify($scope.filteredItems[index]));
	}
	
	$scope.deleting = function(){
		$scope.newItemInsert = false;
	}
	
	$scope.cancel = function(id){
		var index = $scope.getLocalId(id);
		$scope.filteredItems[index] = JSON.parse(JSON.stringify($scope.newItem));
	}
	
	$scope.sort = function(sortType){
		if (sortType !== 'refresh'){
			if ($scope.sortType === sortType)
				$scope.sortReverse = !$scope.sortReverse;
			else
				$scope.sortType = sortType; 
		}
		var copyArray = $filter('orderBy')($scope.items, $scope.sortType, $scope.sortReverse);
		$scope.sortedItems = JSON.parse(JSON.stringify(copyArray));
		$scope.updateFilteredItems();
	}
	
	$scope.getLocalId = function(id){
		for (var i=0; i<$scope.filteredItems.length;i++){
			if ($scope.filteredItems[i].id == id)
				return i;
		}
	}
	
	$scope.clear = function(index){
		for (var i=0; i<$scope.filteredItems.length; i++){
			if (!$scope.filteredItems[i].disabled  &&  i != index){
				$scope.filteredItems[i] = JSON.parse(JSON.stringify($scope.newItem));
				$scope.filteredItems[i].disabled = true;
			}
		}
	}
	
	$scope.$watch('currentPage + numPerPage', function() {
		$scope.updateFilteredItems();  
	 });
	
	$scope.filteredItems = []
	,$scope.currentPage = 1
	,$scope.numPerPage = 10
	,$scope.maxSize = 5;

	$scope.updateFilteredItems = function(){
		var begin = (($scope.currentPage - 1) * $scope.numPerPage),
		    end = begin + $scope.numPerPage;

		var copyArray = $scope.sortedItems.slice(begin, end);
		$scope.filteredItems = JSON.parse(JSON.stringify(copyArray));
	}			
	
}]);
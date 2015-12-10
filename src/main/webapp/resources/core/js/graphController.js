var app = angular.module('rdf', []);

function contains(val, arr) {
	for (var i = 0; i < arr.length; i++) {
		if (arr[i].id == val) {
			return true;
		}
	}
	
	return false;
}

app.controller('graphController', function($scope, $http) {
    var baseUrl = '/api/rest/latest/';
    $scope.graphContentTypes = ['RDF/XML', 'HTML', 'N-TRIPLE', 'TURTLE', 'N3'];
    $scope.graphUrl = 'http://www.w3schools.com/xml/rdf-schema.xml';
    $scope.graphLoading = false;
    $scope.graphPrefixes = null;
    $scope.graphError = '.';
    $scope.graphContentType = $scope.graphContentTypes[0];
    $scope.graphTypes = null;
    $scope.showOptions = false;

    $scope.showOptionsTrigger = function() {
    	$scope.showOptions = !$scope.showOptions;
    }
    
    $scope.color = function (type, types) {
    	var retVal = null;
    	types.forEach(function(iType) {
    		if (iType.value == type) {
    			retVal = iType.color;
    			return;
    		}
    	});
    	
    	return retVal;
    }
    
    $scope.graph = function() {
    	$scope.graphLoading = true;
    	$scope.graphError = null;
    	$http.get(baseUrl+'graph?url='+$scope.graphUrl+'&type='+$scope.graphContentType).then(function(response) {
    		var content = response.data.content;
    		var code = response.data.code;
    		
    		if (code == 'FAIL') {
    			$scope.graphError = content;
    			$scope.graphLoading = false;
    			return;
    		}
    		
    		$scope.prefixes = content.prefixes;
    		$scope.graphTypes = content.types;
    		
    		
    		var nodesArray = [];
    		var edgesArray = [];
    		var colors = ['#006699', '#0066cc', '#009933', '#009966', '#009999', '#0099cc', '#0099ff'];
    		var defColor = '#00ffff';
    		
    		
    		$scope.graphTypes.forEach(function(type) {
    			type.color = colors.shift();
    		});
    		
    		content.nodes.forEach(function(node) {
    			var size = node.connections*10;
    			var shape = node.resourceType != 'LITERAL' ? 'dot' : 'diamond';
    			var color = node.type ? $scope.color(node.type, $scope.graphTypes) : defColor;
    			
    			var graphNode = { id : node.value, label : node.value, cid : node.type, size : size, shape : shape, color : color };
    			nodesArray.push(graphNode);
    		});
    		
    		content.edges.forEach(function(edge) {
    			var graphEdge = { from : edge.from, to : edge.to, label : edge.label , arrows : "to", font : { size : 10 }, color : { inherit : 'from', opacity : 0.3} };
    			edgesArray.push(graphEdge);
    		});
    		
    		var nodes = new vis.DataSet(nodesArray);
    		var edges = new vis.DataSet(edgesArray);
    		var data = {
    				nodes: nodes,
    				edges: edges
    		};
    	
    		var options = {
    					"layout" : { 
    						"randomSeed" : 2 },
    				  "physics": {
    				    "barnesHut": {
    				      "springLength": 400
    				    }
    				  }
    				}
    		
    		createGraph('graph-container', data, options);
    		$scope.graphLoading = false;
    	});
    };
});
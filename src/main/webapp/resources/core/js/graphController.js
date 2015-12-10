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
    $scope.graphContentTypes = ['RDF/XML', 'HTML', 'XML'];
    $scope.graphUrl = 'http://www.w3schools.com/xml/rdf-schema.xml';
    $scope.graphLoading = false;
    $scope.graphPrefixes = null;
    $scope.graphError = null;
    $scope.graphContentType = $scope.graphContentTypes[0];
    $scope.networkCluster = null;

    
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
    		
    		var nodesArray = [];
    		var edgesArray = [];
    		
    		content.nodes.forEach(function(node) {
    			var graphNode = { id : node.value, label : node.value, cid : node.type };
    			console.log(graphNode);
    			nodesArray.push(graphNode);
    		});
    		
    		content.edges.forEach(function(edge) {
    			var graphEdge = { from : edge.from, to : edge.to, label : edge.label , arrows : "to" };
    			edgesArray.push(graphEdge);
    		});
    		
    		var nodes = new vis.DataSet(nodesArray);
    		var edges = new vis.DataSet(edgesArray);
    		var data = {
    				nodes: nodes,
    				edges: edges
    		};
    		console.log(data);
    	
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
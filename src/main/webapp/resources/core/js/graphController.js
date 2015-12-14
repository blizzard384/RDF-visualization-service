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
    var baseUrl = 'api/rest/latest/';
    $scope.graphContentTypes = ['XHTML', 'HTML','RDF/XML', 'N-TRIPLE', 'TURTLE', 'N3'];
    $scope.graphUrl = 'http://www.w3.org/2001/sw/grddl-wg/td/hCardFabien-RDFa.html';
    $scope.graphLoading = false;
    $scope.graphPrefixes = null;
    $scope.graphError = '.';
    $scope.graphContentType = $scope.graphContentTypes[0];
    $scope.graphTypes = null;
    $scope.showOptions = false;
    $scope.showHelp = false;

    $scope.showHelpTrigger = function() {
    	$scope.showOptions = false;
    	$scope.showHelp = !$scope.showHelp;
    }
    
    $scope.showOptionsTrigger = function() {
    	$scope.showHelp = false;
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
    			var size = node.connections >= 1 ? node.connections > 1 ? node.connections*10 : 15 : 5;
    			var shape =  'dot';
    			var title = node.value;
    			node.literals.forEach(function(lit) {
    				title = title + '<br>' + lit;
    			});
    			var color = node.type ? $scope.color(node.type, $scope.graphTypes) : defColor;
    			
    			var graphNode = { id : node.value, label : node.label, cid : node.type, size : size, shape : shape, color : color, title : title};
    			if (node.literals.length > 0) {
    				graphNode.label = graphNode.label + ' (' + node.literals.length + ')';
    			}
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
    						"randomSeed" : 2 
    						},
    						"physics": {
    						    "barnesHut": {
    						      "gravitationalConstant": -3000,
    						      "springLength": 300,
    						      "avoidOverlap": 0.1
    						    },
    						    "minVelocity": 0.9
    						  }
    				}
    		
    		createGraph('graph-container', data, options);
    		$scope.graphLoading = false;
    	});
    };
});
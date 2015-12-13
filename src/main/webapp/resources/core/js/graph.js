var data = null;
var graph = null;
var complexClustering = true;

function createGraph(element, datas, options) {
	data = datas;

	var container = document.getElementById(element);
	graph = new vis.Network(container, data, options);
	graph
			.on(
					'selectNode',
					function(params) {
						if (params.nodes.length == 1) {
							var clusterId = params.nodes[0];
							if (graph.isCluster(clusterId) == true) {
								graph.openCluster(clusterId);
								if (clusterId.startsWith('ID')
										&& complexClustering) {
									getConnected(clusterId.replace('ID', ''))
											.forEach(function(toCluster) {
												clusterByConnection(toCluster);
											});
								}
							} else {
								try {
									clusterByConnection(params.nodes[0]);
								} catch (error) {
									console.log(error);
									alert('Cannot perform operation, data set is too large. Disable complex clustering.');
								}
							}
						}
					});
	graph.on('dragEnd', function(params) {
		graph.selectNodes([]);
	});

}

function getConnected(id) {
	var result = [];
	graph.findNode(id)[0].edges.forEach(function(edge) {
		if (edge.fromId == id && edge.fromId != edge.toId) {
			result.push(edge.toId);
		}
	});
	return result;
}

function isConnected(from, to, complex) {
	if (from.id == to.id) {
		return true;
	}
	if (to.visited === true) {
		return false;
	}

	to.visited = true;

	var retVal = false;
	
	for (var i = 0; i < to.edges.length; i++) {
		var edge = to.edges[i];
		if (edge.from.id == from.id && edge.to.id == to.id) {
			retVal = true;
			break;
		}
		if (edge.to.id == to.id && complex) {
			retVal = isConnected(from, edge.from, complex);
			if (retVal === true) {
				break;
			}
		}
	}

	to.visited = false;

	return retVal;
}

function clusterByConnection(id) {
	var idNode = graph.findNode(id)[0];
	if (complexClustering) {
		data.nodes.forEach(function(node) {
			if (isConnected(idNode, graph.findNode(node.id)[0])) {
				var connectedNode = graph.clustering.findNode(node.id);
				if (connectedNode.length == 2) {
					graph.openCluster(connectedNode[1].id);
				}
			}
		});
	}
	var clusterOptionsByData = {
		joinCondition : function(childOptions) {
			var childNode = graph.findNode(childOptions.id)[0];
			childNode.visited = false;
			var con = isConnected(idNode, childNode,
					complexClustering);
			return con;
		},
		processProperties : function(clusterOptions, childNodes) {
			clusterOptions.label = idNode.options.label + " [" + childNodes.length + "]";
			clusterOptions.title = idNode.options.title;
			return clusterOptions;
		},
		clusterNodeProperties : {
			id : 'ID' + id,
			borderWidth : 3,
			shape : 'diamond'
		}
	}

	graph.cluster(clusterOptionsByData);
}

function clusterByCid(cid) {
	var clusteredNodes = graph.clustering.clusteredNodes;
	var openedClusters = [];

	for ( var nodeId in clusteredNodes) {
		var cluster = graph.clustering.findNode(nodeId);
		if (cluster.length == 2) {
			var clusterId = cluster[1].id;
			if (openedClusters.indexOf(clusterId) == -1) {
				openedClusters.push(clusterId);
				graph.openCluster(clusterId);
			}
		}
	}

	cid.forEach(function(type) {
		clusterType(type);
	});
}

function clusterType(type) {
	var clusterOptionsByData = {
		joinCondition : function(childOptions) {
			return childOptions.cid == type || childOptions.id == type;
		},
		processProperties : function(clusterOptions, childNodes) {
			clusterOptions.label = type + " [" + childNodes.length + "]";
			return clusterOptions;
		},
		clusterNodeProperties : {
			id : 'CID' + type,
			borderWidth : 3,
			shape : 'square'
		}
	}
	graph.cluster(clusterOptionsByData);
}

function complexCluster() {
	complexClustering = !complexClustering;
}

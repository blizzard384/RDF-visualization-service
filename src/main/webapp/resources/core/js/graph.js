var data = null;
var graph = null;
var complexClustering = true;

function createGraph(element, datas, options) {
	data = datas;

	var container = document.getElementById(element);
	graph = new vis.Network(container, data, options);
	graph
			.on(
					"selectNode",
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
									alert('Cannot perform operation, data set is too large. Disable complex clustering.');
								}
							}
						}
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
	if (from == to) {
		return true;
	}

	if (to.visited === true) {
		return false;
	}

	to.visited = true;

	var retVal = false;
	to.edges.forEach(function(edge) {
		if (edge.from == from && edge.to == to) {
			retVal = true;
			return;
		}
		if (edge.to == to && complex) {
			retVal = isConnected(from, edge.from, complex);
			if (retVal == true) {
				return;
			}
		}
	});

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

			return isConnected(idNode, graph.findNode(childOptions.id)[0],
					complexClustering);
		},
		processProperties : function(clusterOptions, childNodes) {
			clusterOptions.label = id + " [" + childNodes.length + "]";
			return clusterOptions;
		},
		clusterNodeProperties : {
			id : 'ID' + id,
			borderWidth : 3,
			shape : 'box'
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
			shape : 'box'
		}
	}
	graph.cluster(clusterOptionsByData);
}

function complexCluster() {
	complexClustering = !complexClustering;
}

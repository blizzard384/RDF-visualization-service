<%@page session="false"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
   <head>
      <title>RDF visualizer</title>
      <c:url var="home" value="/" scope="request" />
      <spring:url value="/resources/core/css/index.css" var="coreCss" />
      <spring:url value="/resources/core/css/bootstrap.min.css"
         var="bootstrapCss" />
      <link href="${bootstrapCss}" rel="stylesheet" />
      <link href="${coreCss}" rel="stylesheet" />
      <link href="https://cdnjs.cloudflare.com/ajax/libs/vis/4.10.0/vis.min.css" rel="stylesheet" />
      <spring:url value="/resources/core/js/jquery.1.10.2.min.js"
         var="jqueryJs" />
      <script src="${jqueryJs}"></script>
      <spring:url value="/resources/core/js/graph.js"
         var="graphJs" />
      <script src="${graphJs}"></script>
      <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.2/angular.min.js"></script>
      <spring:url value="/resources/core/js/graphController.js"
         var="controllerJs" />
      <script src="${controllerJs}"></script>
      <script src="http://visjs.org/dist/vis.js"></script>
   </head>
   <body ng-app="rdf">
      <nav class="navbar navbar-inverse">
         <div class="container">
            <div class="navbar-header">
               <a class="navbar-brand" href="#">Main</a>
            </div>
         </div>
      </nav>
      <div class="container" style="min-height: 500px" ng-controller="graphController">
         <div class="input-group">
            <input type="text" class="form-control form-control-md" ng-model="graphUrl">
            <select class="form-control selectpicker form-control-xs" ng-model="graphContentType">
               <option ng-repeat="type in graphContentTypes" value="{{type}}">{{type}}</option>
            </select>
            <span class="input-group-btn">
            <button class="btn btn-default" type="button" ng-click="graph()">Go!</button>
            </span>
         </div>
         <!-- /input-group -->
         <span ng-show="graphLoading">Loading...</span>
         {{graphError}}
         <div ng-hide="graphLoading || graphError">
         	<span><a href="#" ng-click="showOptionsTrigger()">Options</a></span>
         	<span><a href="#" ng-click="showHelpTrigger()">Help</a></span>
         	<table class="table borderless" ng-show="showHelp">
         		<tr><td>
Functionality:
<ul>
<li> Visualize RDF document from URL
	<ul>
		<li> enter URL and select content type of returned data</li>
	</ul>
</li>
<li> Each node has size based on outgoing connections</li>
<li> Each node has color based on RDF type</li>
<li> Literals are shown in tooltip (mouseover)</li>
<li> Full URIs are shown in tooltip (mouseover)</li>
<li> Number of literals are shown in ( ) brackets</li>
<li> Clustering:
	<ul>
		<li> Break cluster by clicking on it</li>
		<li> number in [ ] represents number of outgoing connections</li>
		<li> Clustering by type:
			<ul>
				<li> These clusters has square shape</li>
				<li> Select in options menu nodes of type (connected via rdf:Type) to cluster (basicaly cluster same colors)</li>
				<li> When u select something all other clusters will be broken</li>
				<li> You can select more types to cluster</li>
				<li> If only one node would belong to cluster nothing will happen</li>
			</ul>
		</li>
		<li>	Cluster by connections:
			<ul>
				<li> These clusters have diamond shape</li>
				<li> Click on node to cluster; All nodes connected to clicked node will be clustered. Connections are directed so only nodes connected FROM clicked node will be clustered.</li>
				<li> Trigger complex clustering in options</li>
				<li> In not complex clustering; only nodes directly connected to clicked node will be clustered</li>
				<li> In complex clustering mode:
					<ul>
						<li> If you click on node all nodes will be searched if they are connected to clicked node (again, connections are directed) and will be clustered if they are</li>
						<li> Calculations have exponential complexity (Is it possible to implement it better?); It works fine on hundreds of nodes and edges but won't work on large datasets</li>
						<li> If you click on cluster, nodes connected (again, directed) to cluster origin (node clicked on when creating cluster) with other connections (again, directed) will be clustered</li>
					</ul>
				</li>
				<li> There is small bug; when node is selected it cannot be unclustered; just deselect node and click again </li>
			</ul>
		</li>
	</ul>
</li>
</ul>
         		</td></tr>
         	</table>
            <table class="table borderless" ng-show="showOptions">
               <tr>
                  <th>Complex clustering:</th>
                  <th>Cluster by type:</th>
               <tr>
                  <td>
                     <input type="checkbox" class="form-control" checked onchange="complexCluster.call()" value="true">
                  </td>
                  <td>
                     <div class="input-group">
                        <select multiple class="form-control form-control-lg" ng-model="networkCluster" id="clusterInput" onchange="clusterByCid($('#clusterInput').val())">
                           <option value="">-none-</option>
                           <option ng-repeat="type in graphTypes" value="{{type.value}}">{{type.value}}</option>
                        </select>
                     </div>
                     <!-- /input-group -->
                  </td>
               </tr>
            </table>
            <div><b>Tips: Click on the node to cluster it by outcoming connections; Click on cluster to uncluster; Drag nodes to reorganize; Pinch or scroll to zoom;</b></div>
            <div>
               <div ng-repeat="(key, value) in prefixes">{{key}}:{{value}}</div>
            </div>
            <div id="graph-container"></div>
         </div>
      </div>
      <div class="container">
         <footer>
         </footer>
      </div>
   </body>
</html>
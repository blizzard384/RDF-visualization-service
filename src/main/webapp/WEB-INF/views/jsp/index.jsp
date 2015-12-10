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
            <table class="table borderless" ng-show="showOptions">
               <tr>
                  <th>Complex clustering:<br>Cluster not only neighbour nodes but all nodes connected to clicked one. But only if edges lead from clicked node.<br>Also when you uncluster by clicking node all connected nodes with other connections will be clustered.<br>(Only when you click on node)</th>
                  <th>Select classes which instances will be clustered; You can select multiple types</th>
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
            <div><b>Tips: Click on the node to cluster it by outcoming connections; Click on cluster to uncluster; Drag nodes to reorganize; Pinch or scroll to zoom;<b></div>
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
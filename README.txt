Sample data:
- http://www.w3.org/2001/sw/grddl-wg/td/hCardFabien-RDFa.html
- - Type: HTML
- - Try complex clustering on this
- - Type clustering won't work because there are not two nodes of same type (only one)
- http://www.w3schools.com/xml/rdf-schema.xml
- - Type: RDF/XML
- - Complex clustering works but is not really useful
- - Try cluster by all types

TO-DOs:
- Change physics or allow user control physics options for better readability and usability
- Let user upload RDF files
- Further improve working with clusters
- Somehow work with long URIs to be shorter

Functionality:
- Visualize RDF document from URL
- Each node has size based on outgoing connections
- Each node has color based on RDF type
- Clustering:
- - Clustering by type:
- - - Select in options menu nodes of type (connected via rdf:Type) to cluster (basicaly cluster same colors)
- - - When u select something all other clusters will be broken
- - - You can select more types to cluster
- - - If only one node would belong to cluster nothing will happen
- - Cluster by connections:
- - - Click on node to cluster; All nodes connected to clicked node will be clustered. Connections are directed so only nodes connected FROM clicked node will be clustered.
- - - Trigger complex clustering in options
- - - In not complex clustering; only nodes directly connected to clicked node will be clustered
- - - In complex clustering mode:
- - - - If you click on node all nodes will be searched if they are connected to clicked node (again, connections are directed) and will be clustered if they are
- - - - Calculations have exponential complexity (Is it possible to implement it better?); It works fine on hundreds of nodes and edges but won't work on large datasets
- - - - If you click on cluster, nodes connected (again, directed) to cluster origin (node clicked on when creating cluster) with other connections (again, directed) will be clustered
- - - There is small bug; when node is selected it cannot be unclustered; just deselect node and click again 


How to run project:
- Import into favorite IDE (or straight do Eclipse via existing maven project import)
- Run 'mvn update' (or in Eclipse right click on project->maven->update)
- Run 'mvn jetty:run' to start server (or in Eclipse create new maven build run configuration with base directory of project and goals: 'jetty:run')
- Type localhost:8080 in browser

You can also use REST API:
- domain/api/rest/latest/graph?url=<rdf url>&type=<rdf type>
- - returns edges, nodes, prefixes and types in Graph object

Noteworthy technologies:
- Apache Jena
- - RDF parsing
- java-rdfa
- - RDFa parsing
- Spring MVC
- - web service
- Jackson
- - JSON serialization
- Angular
- - Handles all AJAX calls
- JQuery
- vis.js
- - Graph API
- Boostrap
- - CSS
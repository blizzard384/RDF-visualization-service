package cz.vse.rdf.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import cz.vse.rdf.service.Graph;
import cz.vse.rdf.service.ModelSerializer;
import cz.vse.rdf.service.SerializableEdge;
import cz.vse.rdf.service.SerializableNode;
import cz.vse.rdf.service.SerializableNode.ResourceType;



@Component
public class SimpleJsonModelSerializerImpl implements ModelSerializer {
	
	private static final String RDF_TYPE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";

	@Override
	public Graph serialize(Model model) {
		StmtIterator stmIterator = model.listStatements();
		
		List<SerializableNode> nodes = new ArrayList<>();
		List<SerializableEdge> edges = new ArrayList<>();
		Map<String, String> prefixes = model.getNsPrefixMap();
		Collection<Serializable> types = new HashSet<>();
		
		while(stmIterator.hasNext()) {
			Statement stmt = stmIterator.next();
			Node subject = stmt.asTriple().getSubject();
			Node predicate = stmt.asTriple().getPredicate();
			Node object = stmt.asTriple().getObject();
			
			ResourceType objectType = object.isLiteral() ? ResourceType.LITERAL : ResourceType.RESOURCE;
			SerializableNode objectNode = new SerializableNode(object.toString(model), null, objectType);
			
			if (!nodes.contains(objectNode)) {
				nodes.add(objectNode);
			}
			
			ResourceType subjectType = subject.isLiteral() ? ResourceType.LITERAL : ResourceType.RESOURCE;
			SerializableNode subjectNode = new SerializableNode(subject.toString(model), null, subjectType);
			
			if (predicate.toString().equals(RDF_TYPE)) {
				subjectNode = new SerializableNode(subject.toString(model), object.toString(model), subjectType);
				
				int connections = 1;
				int index = nodes.indexOf(subjectNode);
				if (index != -1) {
					connections = nodes.get(index).getConnections();
				}
				
				subjectNode.setConnections(connections);
				nodes.remove(subjectNode);
				nodes.add(subjectNode);
				types.add(objectNode);
			} else if (!nodes.contains(subjectNode)) {
				nodes.add(subjectNode);
			} else {
				nodes.get(nodes.indexOf(subjectNode)).addConnection();
			}
			
			edges.add(new SerializableEdge(subject.toString(model), predicate.toString(model), object.toString(model)));
		}
		
		return new Graph(nodes, edges, prefixes, types);
	}

}

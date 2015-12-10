package cz.vse.rdf.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import cz.vse.rdf.service.GraphService;



@Component
public class GraphvizImageService implements GraphService {
	
	private static final String RDF_TYPE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";

	@Override
	public Graph serialize(Model model) {
		StmtIterator stmIterator = model.listStatements();
		
		List<Serializable> nodes = new ArrayList<>();
		List<Serializable> edges = new ArrayList<>();
		Map<String, String> prefixes = model.getNsPrefixMap();
		
		while(stmIterator.hasNext()) {
			Statement stmt = stmIterator.next();
			Node subject = stmt.asTriple().getSubject();
			Node predicate = stmt.asTriple().getPredicate();
			Node object = stmt.asTriple().getObject();
			
			Serializable objectNode = new SerializableNode(object.toString(model), null);
			
			if (!nodes.contains(objectNode)) {
				nodes.add(objectNode);
			}
			
			Serializable subjectNode = new SerializableNode(subject.toString(model), null);
			
			if (predicate.toString().equals(RDF_TYPE)) {
				subjectNode = new SerializableNode(subject.toString(model), object.toString(model));
				nodes.remove(subjectNode);
				nodes.add(subjectNode);
			} else if (!nodes.contains(subjectNode)) {
				nodes.add(subjectNode);
			}
			
			edges.add(new SerializableEdge(subject.toString(model), predicate.toString(model), object.toString(model)));
		}
		
		return new Graph(nodes, edges, prefixes);
	}

}

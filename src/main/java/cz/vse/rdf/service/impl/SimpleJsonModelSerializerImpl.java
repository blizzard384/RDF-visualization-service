package cz.vse.rdf.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
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



@Component
public class SimpleJsonModelSerializerImpl implements ModelSerializer {
	
	private static final String RDF_TYPE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	private static final String RDF_OUT_FORMAT = "TURTLE";

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
			
			SerializableNode objectNode = new SerializableNode(object.toString(model), null, 0);
			String literal = predicate.toString(model) + " > " + object.toString(model);
			SerializableNode subjectNode = new SerializableNode(subject.toString(model), null, object.isLiteral() ? 0 : 1);
			
			boolean isType = predicate.toString().equals(RDF_TYPE);
			
			if (object.isLiteral() || isType) {
				subjectNode.addFirstLiteral(literal);
			} else {
				edges.add(new SerializableEdge(subject.toString(model), predicate.toString(model), object.toString(model)));
				if (!nodes.contains(objectNode)) {
					nodes.add(objectNode);
				}
			}
			
			if (nodes.contains(subjectNode)) {
				SerializableNode node = nodes.get(nodes.indexOf(subjectNode));
				if (isType) {
					node.addFirstLiteral(literal);
					node.setType(object.toString(model));
					types.add(objectNode);
				}
				if (object.isLiteral()) {
					node.addLiteral(literal);
				} else {
					node.addConnection();
				}
			} else {
				nodes.add(subjectNode);
			}
		}
		
		OutputStream stream = new ByteArrayOutputStream();
		model.write(stream, RDF_OUT_FORMAT);
		
		return new Graph(nodes, edges, prefixes, types, stream.toString());
	}

}

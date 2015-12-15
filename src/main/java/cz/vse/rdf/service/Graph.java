package cz.vse.rdf.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@SuppressWarnings("serial")
public class Graph implements Serializable {
	
	public final Collection<SerializableNode> nodes;
	public final Collection<SerializableEdge> edges;
	public final Map<String, String> prefixes;
	public final Collection<Serializable> types;
	public final Serializable rawRdf;
	
	public Graph(Collection<SerializableNode> nodes, Collection<SerializableEdge> edges, Map<String, String> prefixes,
			Collection<Serializable> types, Serializable rawRdf) {
		super();
		this.nodes = nodes;
		this.edges = edges;
		this.prefixes = prefixes;
		this.types = types;
		this.rawRdf = rawRdf;
	}
}

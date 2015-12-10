package cz.vse.rdf.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@SuppressWarnings("serial")
public class Graph implements Serializable {
	
	public final Collection<Serializable> nodes;
	public final Collection<Serializable> edges;
	public final Map<String, String> prefixes;
	
	public Graph(Collection<Serializable> nodes, Collection<Serializable> edges, Map<String, String> prefixes) {
		super();
		this.nodes = nodes;
		this.edges = edges;
		this.prefixes = prefixes;
	}
}

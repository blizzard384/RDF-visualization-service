package cz.vse.rdf.service.impl;

import java.util.Date;

import com.hp.hpl.jena.graph.Node;

public class DistincEdge {
	
	public final Date timestamp;
	
	public final Node node;

	public DistincEdge(Node node) {
		super();
		this.timestamp = new Date();
		this.node = node;
	}

	@Override
	public String toString() {
		return node.toString();
	}
	
	

}

package cz.vse.rdf.service.impl;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SerializableEdge implements Serializable {
	
	public final String from;
	public final String label;
	public final String to;
	
	public SerializableEdge(String from, String label, String to) {
		super();
		this.from = from;
		this.label = label;
		this.to = to;
	}
}

package cz.vse.rdf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceManager {
	
	private final ModelSerializer graph;
	private final RdfService rdf;
	
	@Autowired
	public ServiceManager(ModelSerializer graph, RdfService rdf) {
		super();
		this.graph = graph;
		this.rdf = rdf;
	}
	
	public Graph getGraph(String url, String contentType) {
		return graph.serialize(rdf.parseRdf(url, contentType));
	}
}

package cz.vse.rdf.service;

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;

import cz.vse.rdf.service.impl.Graph;

@Component
public interface GraphService {
	
	Graph serialize(Model model);

}

package cz.vse.rdf.service;

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;

@Component
public interface RdfService {

	Model parseRdf(String url, String contentType);
}

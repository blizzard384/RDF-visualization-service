package cz.vse.rdf.service.impl;

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import cz.vse.rdf.service.RdfService;

@Component
public class JenaRdfService implements RdfService {
	
	static {
		try {
			Class.forName("net.rootdev.javardfa.jena.RDFaReader");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Missing dependency java-rdfa", e);
		}
	}

	@Override
	public Model parseRdf(String url, String contentType) {
		Model model = ModelFactory.createDefaultModel();

		try {
			return model.read(url, contentType);
		} catch (RuntimeException e) {
			throw new IllegalArgumentException("Cannot create model from given url:\n"+e.getMessage(), e);
		}
	}

}

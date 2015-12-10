package cz.vse.rdf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cz.vse.rdf.service.ServiceManager;

@Controller
@RequestMapping("/")
public class IndexController {
	
	private final ServiceManager serviceManager;
	
	@Autowired
	public IndexController(ServiceManager imageCreatorService) {
		this.serviceManager = imageCreatorService;
	}
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}

}

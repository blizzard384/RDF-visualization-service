package cz.vse.rdf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cz.vse.rdf.service.ServiceManager;
import cz.vse.rdf.service.impl.Graph;

@RestController
@RequestMapping("/api/rest/latest")
public class AjaxController {

	private final ServiceManager manager;
	
	@Autowired
	public AjaxController(ServiceManager manager) {
		this.manager = manager;
	}
	
	@RequestMapping(value = "/graph", method = RequestMethod.GET)
	public @ResponseBody AjaxResponse<Graph> getGraph(@RequestParam("url") String url, @RequestParam("type") String contentType) {
		return AjaxResponse.ok(manager.getGraph(url, contentType));
	}
	
	@ExceptionHandler(Throwable.class)
	public @ResponseBody AjaxResponse<String> handleException(Throwable e) {
		return AjaxResponse.fail(e.getMessage());
	}
	
}

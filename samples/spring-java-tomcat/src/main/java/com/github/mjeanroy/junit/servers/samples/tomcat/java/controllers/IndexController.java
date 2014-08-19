package com.github.mjeanroy.junit.servers.samples.tomcat.java.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

	@RequestMapping(value = {"/", "/index"}, method = GET)
	@ResponseBody
	public String index() {
		return "Hello World";
	}
}

package com.wangku.miaodan.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	@RequestMapping(value = "/home", produces = "text/html;charset=UTF-8")
	public String home(ModelMap model, SearchBean condition) {
		model.put("condition", condition);
		return "home";
	}
	
	@RequestMapping(value = "/jpdd", produces = "text/html;charset=UTF-8")
	public String jpdd(ModelMap model, SearchBean condition) {
		model.put("condition", condition);
		return "/order/jpdd";
	}
	
	@RequestMapping(value = "/td", produces = "text/html;charset=UTF-8")
	public String td(ModelMap model, SearchBean condition) {
		model.put("condition", condition);
		return "/order/td";
	}
	
	@RequestMapping("/mine")
	public String mine() {
		return "/order/mine";
	}
	
	@RequestMapping("/option/{path}")
	public String about(@PathVariable("path")String path) {
		return "/option/" + path;
	}

}

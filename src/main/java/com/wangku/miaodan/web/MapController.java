package com.wangku.miaodan.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/map")
public class MapController {
	
	@RequestMapping("")
	public String map(long type, ModelMap model) {
		model.put("type", type);
		return "/map/map3";
	}
	
	@RequestMapping("/search")
	public String search(long type, String city, ModelMap model) {
		model.put("city", city);
		model.put("type", type);
		return "/map/search";	
	}

}

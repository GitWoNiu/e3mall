package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.service.ItemService;

@Controller
public class PageController {
	
	@Autowired
	private ItemService itemService;

	@RequestMapping("/")
	public String showIndex(){
		return "index";
	}
	
	/*@RequestMapping("/item-list")
	public String itemList(){
		return "item-list";
	}*/
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page){
		return page;
	}
	
}

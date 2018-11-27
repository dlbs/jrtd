package com.wangku.miaodan.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangku.miaodan.core.interceptor.LoginInterceptor;
import com.wangku.miaodan.core.model.Order;
import com.wangku.miaodan.core.service.IOrderService;
import com.wangku.miaodan.core.service.IUserService;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private IUserService userService;
	
	@RequestMapping("/search")
	@ResponseBody
	public Map<String, Object> search(SearchBean condition, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (condition.getType() != null && condition.getType() == 3) {
			map.put("orders", orderService.getStoredByUser(LoginInterceptor.getMobile(request)));
		} else {
			map.put("orders", orderService.selectByCondition(condition));
		}
		return map;
	}
	
	@RequestMapping("/detail/{id}")
	public String detail(@PathVariable("id") String id, String from, ModelMap mp, HttpServletRequest request) {
		Order order = orderService.getOrderById(Long.parseLong(id));
		mp.put("isMine", orderService.checkIsMine(LoginInterceptor.getMobile(request), order.getId()));
		mp.put("from", from);
		mp.put("order", order);
		mp.put("isTD", (new Date().getTime() - order.getApplyTime().getTime())/(24 * 60 * 60 * 1000) >= 1? true:false);
		return "/order/order-detail";
	}
	
	@RequestMapping("/store")
	@ResponseBody
	public Map<String, Object> store(Long id, HttpServletRequest request) {
		
		String mobile = LoginInterceptor.getMobile(request);
		Map<String, Object> result = new HashMap<String, Object>();
		Order order = orderService.getOrderById(id);
		if (order == null) {
			result.put("code", 601);
		} else {
			boolean isTD = new Date().getTime() - order.getApplyTime().getTime() >= 24 * 60 * 60 * 1000;
			if (userService.checkCanStore(mobile, isTD)) {
				long storeOrder = userService.storeOrder(id, mobile, isTD);
				if (storeOrder == 1) {
					result.put("code", 200);
					result.put("mobile", order.getMobile());
				} else {
					result.put("code", 600);
				}
			} else {
				result.put("code", 300);
			}
		}
		return result;
	}

}

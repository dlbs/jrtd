package com.wangku.miaodan.web;

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
		if (condition.getType() != null && condition.getType() == 2) {
			map.put("orders", orderService.getStoredByUser(LoginInterceptor.getMobile(request)));
		} else {
			map.put("orders", orderService.selectByCondition(condition));
		}
		return map;
	}
	
	@RequestMapping("/detail/{id}")
	public String detail(@PathVariable("id") String sourceId, int type, String from, ModelMap mp, HttpServletRequest request) {
		Order order = orderService.getOrderBySourceIdAndType(sourceId, type);
		mp.put("isMine", orderService.checkIsMine(LoginInterceptor.getMobile(request), order.getId()));
		mp.put("from", from);
		mp.put("order", order);
		return "/order/order-detail";
	}
	
	@RequestMapping("/store")
	@ResponseBody
	public Map<String, Object> store(Long id, HttpServletRequest request) {
		
		String mobile = LoginInterceptor.getMobile(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if (userService.checkCanStore(mobile)) {
			try {
				long storeOrder = userService.storeOrder(id, mobile);
				if (storeOrder == 1) {
					result.put("code", 200);
				} else {
					result.put("code", 600);
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.put("code", 500);
			}
		} else {
			result.put("code", 300);
		}
		return result;
	}

}

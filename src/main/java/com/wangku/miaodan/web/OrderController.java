package com.wangku.miaodan.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangku.miaodan.constant.OrderSourceTypeEnum;
import com.wangku.miaodan.core.interceptor.LoginInterceptor;
import com.wangku.miaodan.core.model.Order;
import com.wangku.miaodan.core.model.Requit;
import com.wangku.miaodan.core.model.TempOrder;
import com.wangku.miaodan.core.model.User;
import com.wangku.miaodan.core.service.IOrderService;
import com.wangku.miaodan.core.service.IRequitService;
import com.wangku.miaodan.core.service.IUserService;
import com.wangku.miaodan.utils.HttpUtils;
import com.wangku.miaodan.utils.Strings;

@Controller
public class OrderController {
	
	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IRequitService resquitService;
	
	@RequestMapping("/order/search")
	@ResponseBody
	public Map<String, Object> search(SearchBean condition, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Order> orders = new ArrayList<Order>(0);
		if (condition.getType() != null && condition.getType() == 3) {
			int start = (condition.getPage() - 1) * condition.getSize();
			orders = orderService.getStoredByUser(LoginInterceptor.getMobile(request), condition.isTD(), start, condition.getSize());
		} else {
			orders = orderService.selectByCondition(condition);
		}
		Date now = new Date();
		for (Order order : orders) {
			order.setPlusTime(plusTime(now, order.getApplyTime()));
		}
		
		map.put("orders", orders);
		
		return map;
	}
	
	public String plusTime(Date now, Date target) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(now);
		cal2.setTime(target);
		long time = now.getTime() - target.getTime();
		if (time / (1000 * 60 * 60 * 24) > 364) {
			return (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) + "年前";
		} else if (time / (1000 * 60 * 60 * 24) > 30){
			return (cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH)) + "月前";
		} else if (time / (1000 * 60 * 60 * 24) > 1) {
			return (time / (1000 * 60 * 60 * 24)) + "天前";
		} else if (time / (1000 * 60 * 60) > 1) {
			return (time / (1000 * 60 * 60 )) + "小时前";
		} else if (time / (1000 * 60) >= 1) {
			return (time / (1000 * 60)) + "分钟前";
		} else if (time / 1000 > 10) {
			return (time / 1000) + "秒前";
		} else {
			return "刚刚";
		}
	}
	
	@RequestMapping("/order/detail/{id}")
	public String detail(@PathVariable("id") String id, String from, Boolean td, Integer status, ModelMap mp, HttpServletRequest request) {
		String mobile = LoginInterceptor.getMobile(request);
		Order order = orderService.getOrderById(Long.parseLong(id));
		mp.put("isMine", orderService.checkIsMine(mobile, order.getId()));
		mp.put("from", from);
		mp.put("status", status);
		mp.put("td", td == null? "false":td.toString());
		mp.put("order", order);
		mp.put("isTD", OrderSourceTypeEnum.isTDByDesc(order.getSource()));
		mp.put("requit", resquitService.detail(Long.parseLong(id), mobile));
		return "/order/order-detail";
	}
	
	@RequestMapping("/order/store")
	@ResponseBody
	public Map<String, Object> store(Long id, HttpServletRequest request) {
		String mobile = LoginInterceptor.getMobile(request);
		Map<String, Object> result = new HashMap<String, Object>();
		Order order = orderService.getOrderById(id);

		if (order == null) {
			result.put("code", 601);
		} else {
			boolean isTD = OrderSourceTypeEnum.isTDByDesc(order.getSource());
			//验证用户是否通过认证
			Integer status = userService.getDetailByMobile(mobile).getStatus();
			 
			if (status == 0) {
				result.put("code", 601);
				result.put("msg", "未进行认证");
			} else if (status == 1) {
				result.put("code", 602);
				result.put("msg", "认证中");
			} else if (status == 3){
				result.put("code", 603);
				result.put("msg", "认证未通过");
			} else if (!userService.checkCanStore(mobile, isTD)) {
				result.put("code", 300);
				result.put("msg", "抢单机会用尽，请尽快充值");
			} else {
				long storeOrder = 0;
				try {
					storeOrder = userService.storeOrder(order, mobile, isTD);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (storeOrder == 1) {
						result.put("code", 200);
						result.put("order", order);
				} else {
					result.put("code", 500);
				}
				
			}
		}
		return result;
	}
	
	@RequestMapping(value = "/order/requit/{id}", method = RequestMethod.GET)
	public String requit(@PathVariable("id")Long id, ModelMap model, String from, HttpServletRequest request) {
		model.put("orderId", id);
		model.put("from", from);
		model.put("isRequit", resquitService.isRequit(id, LoginInterceptor.getMobile(request)));
		return "order/requit";
	}
	
	@RequestMapping(value = "/order/requit/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> requitSave(@PathVariable("id")Long id, HttpServletRequest request, Requit requit) {
		String mobile = LoginInterceptor.getMobile(request);
		User user = userService.getDetailByMobile(mobile);
		requit.setMobile(mobile);
		requit.setUserId(user.getId());
		requit.setOrderId(id);
		String requitReason = requit.getRequitReason();
		if ("0".equals(requitReason)) {
			requit.setRequitReason("客户不需要");
		} else if ("1".equals(requitReason)) {
			requit.setRequitReason("联系方式异常");
		} else if ("2".equals(requitReason)) {
			requit.setRequitReason("异地");
		}
		resquitService.save(requit);
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("code", 200);
		result.put("msg", "退单操作完成");
		return result;
	}
	

	
	@RequestMapping(value = "/loan", method = RequestMethod.POST)
	@ResponseBody
	public int save(@RequestBody TempOrder order) {
		
		if (order == null) {
			return 0;
		}
		
		if (Strings.isBlank(order.getName())) {
			return 1;
		}
		
		if (Strings.isBlank(order.getMobile())) {
			return 2;
		}
		
		if (Strings.isBlank(order.getCity()) || order.getCity().contains("市")) {
			return 3;
		}
		
		if (order.getSum() == null || order.getSum() <= 0) {
			return 4;
		}
		
		if (Strings.isBlank(order.getIdentyNumber())) {
			return 5;
		}
		
		if (Objects.isNull(order.getApplyTime())) {
			return 6;
		}
		
		List<Order> list = new ArrayList<Order>();
		list.add(order.translateOrder());
		orderService.saveBatch(list);
		return 200;
	}
	
	@RequestMapping("/test")
	@ResponseBody
	public String test(long id) {
		Order orderById = orderService.getOrderById(id);
		String post = HttpUtils.post("http://hd.kapokmedia.com:60521/notify/buytheInformation", orderById.getMkj());
		return post;
	}
	
}

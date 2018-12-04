package com.wangku.miaodan.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangku.miaodan.constant.ProductTypeEnums;
import com.wangku.miaodan.core.interceptor.AdminLoginInterceptor;
import com.wangku.miaodan.core.model.AuthUser;
import com.wangku.miaodan.core.model.Order;
import com.wangku.miaodan.core.model.Recharge;
import com.wangku.miaodan.core.model.Requit;
import com.wangku.miaodan.core.model.User;
import com.wangku.miaodan.core.service.IAuthUserService;
import com.wangku.miaodan.core.service.IOrderService;
import com.wangku.miaodan.core.service.IRechargeService;
import com.wangku.miaodan.core.service.IRequitService;
import com.wangku.miaodan.core.service.IUserService;
import com.wangku.miaodan.utils.Strings;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IRechargeService rechargeService;
	
	@Autowired
	private IRequitService requitService;
	
	@Autowired
	private IAuthUserService authUserService;
	
	@RequestMapping("")
	public String adminLogin(String user, ModelMap model, HttpServletRequest request) {
		String userName = AdminLoginInterceptor.getUserName(request);
		String ticket = AdminLoginInterceptor.getTicket(request);
		if (Strings.isNullOrEmpty(userName)) {
			userName = authUserService.getDetailByTicket(ticket).getUsername();
		}
		model.put("user", AdminLoginInterceptor.getUserName(request));
		return "admin/index";
	}
	
	@RequestMapping("/login")
	public String adminHome(String username, String password, ModelMap model, HttpServletResponse response) {
		String viewName = "redirect:/admin";
		AuthUser user = authUserService.getDetailByUserAndPass(username, password);
		if (!Objects.isNull(user)) {
			String ticket = UUID.randomUUID().toString().replace("-", "");
			authUserService.addTicket(user.getId(), ticket);
			AdminLoginInterceptor.setUserName(ticket, username);
			Cookie cookie = new Cookie("adminticket", ticket);
			cookie.setPath("/");
			response.addCookie(new Cookie("adminticket", ticket));
			return viewName;
		}
		model.put("username", username);
		return "admin/login";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		String userName = AdminLoginInterceptor.getUserName(request);
		AdminLoginInterceptor.clearTicket(request);
		authUserService.removeTicket(userName);
		return "redirect:/admin/login";
	}
	
	@RequestMapping("/user/list")
	@ResponseBody
	public Map<String, Object> userList(int page, int size) {
		 Map<String, Object> result = new HashMap<String, Object>();
		 List<User> list = userService.list((page - 1) * size, size);
		 result.put("list", list);
		 long count = userService.count();
		 result.put("pages", count % size == 0? count/ size: count/size + 1);
		 return result;
	}
	
	@RequestMapping("/product/list")
	@ResponseBody
	public Map<String, Object> productList(int page, int size) {
		 Map<String, Object> result = new HashMap<String, Object>();
		 List<Order> list = orderService.list((page - 1) * size, size);
		 result.put("list", list);
		 long count = orderService.count();
		 result.put("pages", count % size == 0? count/ size: count/size + 1);
		return result;
	}
	
	@RequestMapping("/recharge/list")
	@ResponseBody
	public Map<String, Object> rechargeList(int page, int size) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Recharge> list = rechargeService.list((page - 1) * size, size);
		for (Recharge recharge : list) {
			ProductTypeEnums type = ProductTypeEnums.getByType(recharge
					.getProduct());
			recharge.setTimes(type.getJpTimes());
			recharge.setTdTimes(type.getTdTimes());
		}
		result.put("list", list);
		long count = rechargeService.count();
		result.put("pages", count % size == 0 ? count / size : count / size + 1);
		return result;
	}
	
	@RequestMapping("/requit/list")
	@ResponseBody
	public Map<String, Object> requitList(int page, int size) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Requit> list = requitService.list((page - 1) * size, size);
		 result.put("list", list);
		 long count = requitService.count();
		 result.put("pages", count % size == 0? count/ size: count/size + 1);
		return result;
	}
	
	@RequestMapping("/checkAuth")
	@ResponseBody
	public Map<String, Object> checkAuth(Long id, int status) {
		userService.checkAuth(id, status);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 200);
		result.put("msg", "用户认证状态修改成功");
		return result;
	}
	
	@RequestMapping("/updateProduct")
	@ResponseBody
	public Map<String, Object> updateProduct(Long id, int status) {
		orderService.updateStatus(id, status);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 200);
		result.put("msg", "订单状态修改成功");
		return result;
	}
	
	@RequestMapping("/updateRequit")
	@ResponseBody
	public Map<String, Object> updateRequit(Long id, int status) {
		requitService.updateStatus(id, status);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 200);
		result.put("msg", "申退订单状态修改成功");	
		return result;
	}
	
	@RequestMapping("/updateusertimes")
	@ResponseBody
	public Map<String, Object> updateUserTimes(Long id, int times, int tdTimes, boolean isTd) {
		Map<String, Object> result = new HashMap<String, Object>();
		User user = userService.getDetailById(id);
		if ((isTd && tdTimes < user.getTdTimes()) || (!isTd && times < user.getTimes())) {
			result.put("code", 301);
			result.put("msg", "余额只能增加");
			result.put("times", user.getTimes());
			result.put("tdTimes", user.getTdTimes());
		} else if (isTd)  {
			user.setTdTimes(tdTimes);
			userService.update(user);
			result.put("code", 200);
			result.put("msg", "余额修改成功");
		} else if (!isTd) {
			user.setTimes(times);
			userService.update(user);
			result.put("code", 200);
			result.put("msg", "余额修改成功");
		}
		return result;
	}
}

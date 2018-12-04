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
	public List<User> userList(int page, int size) {
		return userService.list((page - 1) * size, size);
	}
	
	@RequestMapping("/product/list")
	@ResponseBody
	public List<Order> productList(int page, int size) {
		return orderService.list((page - 1) * size, size);
	}
	
	@RequestMapping("/recharge/list")
	@ResponseBody
	public List<Recharge> rechargeList(int page, int size) {
		List<Recharge> list = rechargeService.list((page - 1) * size, size);
		for (Recharge recharge : list) {
			ProductTypeEnums type = ProductTypeEnums.getByType(recharge.getProduct());
			recharge.setTimes(type.getJpTimes());
			recharge.setTdTimes(type.getTdTimes());
		}
		
		return list;
	}
	
	@RequestMapping("/requit/list")
	@ResponseBody
	public List<Requit> requitList(int page, int size) {
		return requitService.list((page - 1) * size, size);
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
}

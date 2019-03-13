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
import com.wangku.miaodan.core.model.DataSource;
import com.wangku.miaodan.core.model.Order;
import com.wangku.miaodan.core.model.Recharge;
import com.wangku.miaodan.core.model.Requit;
import com.wangku.miaodan.core.model.User;
import com.wangku.miaodan.core.service.IAuthUserService;
import com.wangku.miaodan.core.service.IDataSourceService;
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
	
	@Autowired
	private IDataSourceService dataSourceService;
	
	@RequestMapping("")
	public String adminHome(String user, ModelMap model, HttpServletRequest request) {
		String userName = AdminLoginInterceptor.getUserName(request);
		String ticket = AdminLoginInterceptor.getTicket(request);
		if (!Strings.isBlank(userName)) {
			userName = authUserService.getDetailByTicket(ticket).getUsername();
		}
		model.put("user", AdminLoginInterceptor.getUserName(request));
		return "admin/temp";
	}
	
	@RequestMapping("/index")
	public String index(String type, ModelMap model) {
		model.put("type", type);
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
	public Map<String, Object> userList(int page, int size, String status, String city, String addTime, HttpServletRequest request) {
		 Map<String, Object> result = new HashMap<String, Object>();
		 List<User> list = userService.list(city, status, addTime, (page - 1) * size, size);
		 result.put("list", list);
		 long count = userService.count(city, status, addTime);
		 result.put("pages", count % size == 0? count/ size: count/size + 1);
		 result.put("count", count);
		 result.put("sum", userService.count(null, null, null));
		 result.put("user", AdminLoginInterceptor.getUserName(request));
		 return result;
	}
	
	@RequestMapping("/datasource/list")
	@ResponseBody
	public Map<String, Object> dataSourceList(int page, int size, String code, String status) {
		 Map<String, Object> result = new HashMap<String, Object>();
		 result.put("list", dataSourceService.list(code, status, (page - 1) * size, size));
		 long count = dataSourceService.count(code, status);
		 result.put("pages", count % size == 0? count/ size: count/size + 1);
		 result.put("count", count);
		 result.put("sum", dataSourceService.count(null, null));
		 return result;
	}
	
	@RequestMapping("/datasource/add")
	public String dataSourceAdd(DataSource source) {
		dataSourceService.add(source);
		return "redirect:/view/admin/datasource.html";
	}
	
	@RequestMapping("/datasource/edit")
	public String dataSourceUpdate(Long id, ModelMap map) {
		DataSource source = dataSourceService.getById(id);
		map.put("source", source);
		return "/admin/datasource_add";
	}	
	
	@RequestMapping("/datasource/updatestatus")
	@ResponseBody
	public Map<String, Object> changeStatus(Long id, int status) {
		 Map<String, Object> result = new HashMap<String, Object>();
		 try {
			DataSource source = new DataSource();
			 source.setId(id);
			 source.setStatus(status);
			 dataSourceService.update(source);
			 result.put("code", 200);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 500);
			result.put("msg", e.getMessage());
		}
		 return result;	
	}
	
	@RequestMapping("/product/list")
	@ResponseBody
	public Map<String, Object> productList(String applyTimeStart, String applyTimeEnd, String source, String status, String city, int page, int size) {
		 Map<String, Object> result = new HashMap<String, Object>();
		 List<Order> list = orderService.list(applyTimeStart, applyTimeEnd, source, status, city, (page - 1) * size, size);
		 result.put("list", list);
		 long count = orderService.count(applyTimeStart, applyTimeEnd, source, status, city);
		 result.put("pages", count % size == 0? count/ size: count/size + 1);
		 result.put("count", count);
		 result.put("sum", orderService.count(null, null, null, null, null));
		return result;
	}
	
	@RequestMapping("/recharge/list")
	@ResponseBody
	public Map<String, Object> rechargeList(String addTime, String name, int page, int size) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Recharge> list = rechargeService.list(addTime, name, (page - 1) * size, size);
		for (Recharge recharge : list) {
			ProductTypeEnums type = ProductTypeEnums.getByType(recharge.getProduct());
			recharge.setTimes(type.getJpTimes());
			recharge.setTdTimes(type.getTdTimes());
		}
		result.put("list", list);
		long count = rechargeService.count(addTime, name);
		result.put("pages", count % size == 0 ? count / size : count / size + 1);
		result.put("count", count);
		result.put("sum", rechargeService.count(null, null));
		return result;
	}
	
	@RequestMapping("/requit/list")
	@ResponseBody
	public Map<String, Object> requitList(String name, String status, String source, int page, int size) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Requit> list = requitService.list(name, status, source, (page - 1) * size, size);
		 result.put("list", list);
		 long count = requitService.count(name, status, source);
		 result.put("pages", count % size == 0? count/ size: count/size + 1);
		 result.put("count", count);
		 result.put("sum", requitService.count(null, null, null));
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
		if (isTd)  {
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

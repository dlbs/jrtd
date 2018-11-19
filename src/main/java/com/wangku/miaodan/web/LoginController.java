package com.wangku.miaodan.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wangku.miaodan.core.interceptor.LoginInterceptor;
import com.wangku.miaodan.core.service.IUserService;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	private IUserService userService;
	
	@RequestMapping("/index")
	public ModelAndView login(ModelAndView mav) {
		mav.setViewName("/login/login");
		return mav;
	}
	
	@RequestMapping("/out")
	public String logout(HttpServletRequest request) {
		LoginInterceptor.removeTicket(request);
		return "redirect:/login/index";
	}
	
	@RequestMapping("/checkVerifyCode")
	@ResponseBody
	public Map<String, Object> checkUser(String mobile, String msgCode) {
		Map<String, Object> result = new HashMap<String, Object>(4);
		String ticket = LoginInterceptor.isValid(mobile, msgCode);
		if (Objects.isNull(ticket)) {
			result.put("code", 601);
			result.put("msg", "无效验证码");
		} else {
			result.put("code", 200);
			result.put("msg", "验证通过");
			result.put("ticket", ticket);
			userService.addUser(mobile);// 直接进行新用户创建
		}
		return result;
	}
	
	@RequestMapping("/sendVerifyCode")
	@ResponseBody
	public Map<String, Object> sendVerifyCode(String mobile) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 验证手机号是否有效
		if (mobile == null || mobile.isEmpty() || !mobile.matches("^1(?:3\\d|4[4-9]|5[0-35-9]|6[67]|7[013-8]|8\\d|9\\d)\\d{8}")) {
			result.put("code", 601);
			result.put("msg", "无效手机号格式");
			return result;
		} else if (LoginInterceptor.sendCode(mobile)){
			result.put("code", 200);
			result.put("msg", "验证码发送成功");
		} else {
			result.put("code", 500);
			result.put("msg", "验证码发送失败");			
		}
		return result;
	}

}

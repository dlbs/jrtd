package com.wangku.miaodan.core.interceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wangku.miaodan.utils.VerifyCode;
import com.wangku.miaodan.utils.VerifyCodeUtil;
import com.wangku.miaodan.utils.message.aliyun.MessageUtils;

public class LoginInterceptor implements HandlerInterceptor {
	
	private static final Map<String, VerifyCode> codes = new HashMap<String, VerifyCode>();
	
	private static final Map<String, String> tickets = new HashMap<String, String>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String mobile = getMobile(request);// 未登录， 重定向到登录页面
		if (mobile == null) {
			response.sendRedirect(request.getContextPath()+"/login/index");
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}
	
	public static String getMobile(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("ticket")) {
					return tickets.get(cookie.getValue());
				}
			}
		}
		return null;
	}
	
	
	public static boolean sendCode(String mobile) {
		try {
			String verifyCode = VerifyCodeUtil.getVerifyCode();
			Map<String, Object> map = new HashMap<String, Object>(1);
			map.put("code", verifyCode);
			System.out.println(mobile + "------" + verifyCode);
			MessageUtils.sendSms(mobile, verifyCode);
			codes.put(mobile, new VerifyCode(verifyCode));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String isValid(String mobile, String code) {
		VerifyCode verifyCode = codes.get(mobile);
		if (verifyCode == null || !verifyCode.isValid(code)) {
			return null;
		} 
		String uuid = UUID.randomUUID().toString();
		tickets.put(uuid, mobile);
		codes.remove(mobile);
		return uuid;
	}

	public static void removeTicket(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("ticket")) {
				tickets.remove(cookie.getValue());
				break;
			}
		}
	}

}

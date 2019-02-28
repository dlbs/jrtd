package com.wangku.miaodan.core.interceptor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wangku.miaodan.utils.Base64Utils;
import com.wangku.miaodan.utils.Strings;
import com.wangku.miaodan.utils.message.aliyun.MessageUtils;

public class LoginInterceptor implements HandlerInterceptor {
	
	private static final boolean IS_MESSAGE = true;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String mobile = getMobile(request);
		if (!Strings.isBlank(mobile)) {
			return true;
		}
		response.sendRedirect("/login/index");
		return false;
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
	
	public static String getTicket(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("user_ticket")) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	public static void sendCode(String mobile, String verifyCode) {
		System.out.println(mobile + "------" + verifyCode);
		if (IS_MESSAGE && !"13931727436".equals(mobile)) {
			MessageUtils.sendSms(mobile, verifyCode);
		}
	}
	
	public static String getMobile(HttpServletRequest request) {
		String mobile = null;
		String ticket = getTicket(request);
		if (ticket != null) {
			try {
				mobile = Base64Utils.decode(URLDecoder.decode(ticket, "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return mobile;
	}

}

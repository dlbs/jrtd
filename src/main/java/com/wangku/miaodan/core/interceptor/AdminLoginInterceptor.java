package com.wangku.miaodan.core.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wangku.miaodan.utils.VerifyCode;

public class AdminLoginInterceptor implements HandlerInterceptor {
	
	private static final Map<String, VerifyCode> codes = new HashMap<String, VerifyCode>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String mobile = "";// 未登录， 重定向到登录页面
		return true;
/*		if (mobile == null) {
			response.sendRedirect(request.getContextPath()+"/login/index");
			return false;
		} else {
			return true;
		}*/
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

}

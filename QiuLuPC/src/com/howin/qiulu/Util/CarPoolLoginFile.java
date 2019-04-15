package com.howin.qiulu.Util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.howin.qiulu.pojo.User;


@WebFilter({"/user/*","/user/personal/*","/user/sam/*"})
public class CarPoolLoginFile implements Filter{

	@Override
	public void destroy() {
		
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest=(HttpServletRequest) request;
		HttpServletResponse httpServletResponse=(HttpServletResponse) response;
		String uri=httpServletRequest.getRequestURI();
		String path=httpServletRequest.getContextPath();
		User user=(User) httpServletRequest.getSession().getAttribute("user");
//		if(user !=null || uri.equals(path+"/user/login")){
//			chain.doFilter(request, response);
//		}else{
//			httpServletResponse.sendRedirect(path+"/index.jsp");
//		}
		if(uri.equals(path+"/user/login") || uri.equals(path+"/user/register")){
			chain.doFilter(request, response);
		}else if(user !=null || uri.equals(path+"/user/login")){
			chain.doFilter(request, response);
		}else{
			httpServletResponse.sendRedirect(path+"/login.html");
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
		
	}

}

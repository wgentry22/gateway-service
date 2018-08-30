package com.revature.zuul.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.revature.security.jwt.JwtConstants;

@Component
public class PreFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(PreFilter.class);

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletRequest request = context.getRequest();
		log.info("Value of requestHeader: " + request.getHeader(JwtConstants.HEADER_STRING));
		String token = request.getHeader(JwtConstants.HEADER_STRING);
		
		log.info("Inside PreFilter.run() method... " + context.getRequest().getMethod() + " going to " + context.getRequest().getRequestURI());
		log.info("Setting Authorization header...");
		context.getZuulRequestHeaders().put(JwtConstants.HEADER_STRING, token);
		log.info("Authorization Request Header Sent Out: " + context.getZuulRequestHeaders().get(JwtConstants.HEADER_STRING));
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 1;
	}
}

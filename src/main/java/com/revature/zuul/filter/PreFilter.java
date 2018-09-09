package com.revature.zuul.filter;

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
		String responseToken = context.getResponse().getHeader(JwtConstants.HEADER_STRING);
		String requestToken = context.getRequest().getHeader(JwtConstants.HEADER_STRING);
		log.info("Inside PreFilter.run() method... " + context.getRequest().getMethod() + " going to " + context.getRequest().getRequestURI());
		System.err.println("Request Header: " + requestToken);
		System.err.println("Response Header: " + responseToken);
		log.info("Setting Authorization header...");
		final String token = context.getRequest().getHeader(JwtConstants.HEADER_STRING) != null ? context.getRequest().getHeader(JwtConstants.HEADER_STRING) : context.getResponse().getHeader(JwtConstants.HEADER_STRING);
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
		return 2;
	}
}

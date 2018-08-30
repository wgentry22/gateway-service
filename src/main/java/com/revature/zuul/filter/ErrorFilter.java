package com.revature.zuul.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class ErrorFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(ErrorFilter.class);

	@Override
	public boolean shouldFilter() {
		return false;
	}

	@Override
	public Object run() {
		RequestContext context = RequestContext.getCurrentContext();
		log.info("Inside ErrorFilter.run() method... " + context.getRequest().getMethod() + " request to " + context.getRequest().getRequestURI() + " yielded a " + context.getResponseStatusCode());
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.ERROR_TYPE;
	}

	@Override
	public int filterOrder() {
		return 1;
	}
}

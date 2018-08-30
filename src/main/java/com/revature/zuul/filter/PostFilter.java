package com.revature.zuul.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class PostFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(PostFilter.class);

	@Override
	public boolean shouldFilter() {
		return false;
	}

	@Override
	public Object run() {
		RequestContext context = RequestContext.getCurrentContext();
		log.info("Inside PostFilter.run() method... " + context.getRequest().getMethod() + " going to " + context.getRequest().getRequestURI());
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.POST_TYPE;
	}

	@Override
	public int filterOrder() {
		return 1;
	}
}

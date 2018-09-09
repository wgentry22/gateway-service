package com.revature.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class WebAppSecurityInitializer extends AbstractSecurityWebApplicationInitializer {

	public WebAppSecurityInitializer() {
		super(WebSecurityConfig.class);
	}
}

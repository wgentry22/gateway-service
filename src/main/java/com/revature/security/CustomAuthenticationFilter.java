package com.revature.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	/**
	 * @author William
	 */
	
	public CustomAuthenticationFilter() {
		super(new AntPathRequestMatcher("/authenticate", "POST"));
	}
	
	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		final UsernamePasswordAuthenticationToken authRequest = getAuthRequest(request);
		System.err.println("Inside CustomAuthenticationFilter...");
		return customAuthenticationProvider.authenticate(authRequest);
	}
	
	private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		return new UsernamePasswordAuthenticationToken(username , password);
	}
}

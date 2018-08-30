package com.revature.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	/**
	 * @author William
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		final UsernamePasswordAuthenticationToken authRequest = getAuthRequest(request);
		setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);
	}
	
	private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) {
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		return new UsernamePasswordAuthenticationToken(username , password);
	}
}

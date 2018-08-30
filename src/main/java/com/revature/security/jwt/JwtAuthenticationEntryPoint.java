package com.revature.security.jwt;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.sendError(Response.SC_UNAUTHORIZED, "Unauthorized");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		String message;
		if (authException.getCause() != null) {
			message = authException.getCause().getMessage();
		} else {
			message = authException.getMessage();
		}
		byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", message));
		response.getOutputStream().write(body);
	}

}

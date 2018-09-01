package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.revature.model.UserDto;
import com.revature.security.jwt.JwtConstants;
import com.revature.security.jwt.JwtTokenUtil;

@Controller
public class AuthController {
	
	private static Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@PostMapping(value="/generate-token", consumes= {MediaType.APPLICATION_JSON_VALUE, "application/x-www-form-urlencoded", "application/x-www-form-urlencoded;charset=UTF-8"})
	public @ResponseBody ResponseEntity<String> authenticateUser(final HttpServletRequest request) throws AuthenticationException {
		final String username = request.getParameter("username");
		final String password = request.getParameter("password");
		UserDto userDto = new UserDto(username, password);
		System.err.println("Inside AuthController.authenticateUser");
		System.err.println("Value of userDto.getUsername(): " + userDto.getUsername());
		System.err.println("Value of userDto.getPassword(): " + userDto.getPassword());
		final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
		authentication.getAuthorities().forEach(auth -> logger.info("granted authorities: " + auth.getAuthority()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final String token = JwtConstants.TOKEN_PREFIX + jwtTokenUtil.generateToken(authentication);
		HttpHeaders headers = new HttpHeaders();
		headers.set(JwtConstants.HEADER_STRING, token);
		HttpEntity<?> entity = new HttpEntity<Object>(headers);
		return this.restTemplate.exchange("http://application-service", HttpMethod.POST, entity, String.class);
	}
}

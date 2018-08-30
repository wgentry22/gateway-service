package com.revature.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@PostMapping(value="/generate-token", consumes= {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public ResponseEntity<String> authenticateUser(@RequestBody UserDto userDto) throws AuthenticationException {
		final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
		authentication.getAuthorities().forEach(auth -> logger.info("granted authorities: " + auth.getAuthority()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final String token = jwtTokenUtil.generateToken(authentication);
		HttpHeaders headers = new HttpHeaders();
		headers.set(JwtConstants.HEADER_STRING, JwtConstants.TOKEN_PREFIX + token);
		HttpEntity<?> entity = new HttpEntity<Object>(headers);
		return this.restTemplate.exchange("http://application-service", HttpMethod.POST, entity, String.class);
	}
	
	
	
	
	
	
	
	
}

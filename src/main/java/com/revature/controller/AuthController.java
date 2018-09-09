package com.revature.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.revature.model.UserDto;
import com.revature.security.jwt.JwtConstants;
import com.revature.security.jwt.JwtTokenUtil;

@Controller
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@PostMapping(value="/generate-token", consumes= {MediaType.APPLICATION_JSON_VALUE, "application/x-www-form-urlencoded", "application/x-www-form-urlencoded;charset=UTF-8"})
	public @ResponseBody ResponseEntity<String> authenticateUser(HttpServletRequest request) throws AuthenticationException {
		final UserDto userDto = new UserDto(request.getParameter("username"), request.getParameter("password"));
		CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
		System.err.println("Inside AuthController.authenticateUser");
		System.err.println("Value of userDto.getUsername(): " + userDto.getUsername());
		System.err.println("Value of userDto.getPassword(): " + userDto.getPassword());
		System.err.println("Value of CSRF token (attribute): " + request.getAttribute("_csrf"));
		System.err.println("Value of CSRF token (parameter): " + request.getParameter("_csrf"));
		final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final String token = JwtConstants.TOKEN_PREFIX + jwtTokenUtil.generateToken(authentication);
		HttpHeaders headers = new HttpHeaders();
		headers.set(JwtConstants.HEADER_STRING, token);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
		headers.set("X-CSRF-TOKEN", csrfToken.getToken());
		headers.set("X-CSRF-HEADER", csrfToken.getHeaderName());
		headers.set("X-CSRF-PARAM", csrfToken.getParameterName());
		HttpEntity<?> entity = new HttpEntity<String>(headers);
//		return this.restTemplate.exchange("http://application-service", HttpMethod.POST, entity, String.class);
		return ResponseEntity.ok().headers(headers).build();
	}
	
	@GetMapping("/login")
	public String getHomePage(Model model) {
		model.addAttribute("userDto", new UserDto());
		return "login";
	}
}

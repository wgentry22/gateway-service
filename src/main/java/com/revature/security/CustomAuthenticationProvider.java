package com.revature.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.revature.model.User;
import com.revature.repository.UserRepository;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		System.err.println("Inside CustomAuthenticationProvider...");
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		User user = userRepository.findByUsername(username);
		User authorized = null;
		if (passwordEncoder.matches(password, user.getPassword())) {
			authorized = user;
			user = null;
		}
		if (authorized != null && passwordEncoder.matches(password, authorized.getPassword())) {
			final List<GrantedAuthority> grantedAuthorities = authorized.getAuthorities().stream().map(a -> new SimpleGrantedAuthority(a.getAuthority())).collect(Collectors.toList());
			final org.springframework.security.core.userdetails.UserDetails principal = new org.springframework.security.core.userdetails.User(username, password, grantedAuthorities);
			final Authentication auth = new UsernamePasswordAuthenticationToken(principal.getUsername(), principal.getPassword(), principal.getAuthorities());
			System.err.println("CustomAuthenticationProvider.getPrincipal(): " + auth.getPrincipal());
			System.err.println("CustomAuthenticationProvider.getCredentials(): " + auth.getCredentials());
			System.err.println("CustomAuthenticationProvider.getAuthorities(): " + auth.getAuthorities());
			return auth;
		} else {
			System.out.println("Something went wrong in CustomAuthenticationProvider....");
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}

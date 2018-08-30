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

import com.revature.model.Authorities;
import com.revature.model.User;
import com.revature.repository.AuthoritiesRepository;
import com.revature.repository.UserRepository;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	/**
	 * @author William
	 */
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthoritiesRepository authoritiesRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		User user = userRepository.findByUsername(username);
		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			List<Authorities> authorities = authoritiesRepository.findByUser(user);
			final List<GrantedAuthority> grantedAuthorities = authorities.stream().map(a -> new SimpleGrantedAuthority(a.getAuthority())).collect(Collectors.toList());
			final  org.springframework.security.core.userdetails.UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, password, grantedAuthorities);
			final Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), passwordEncoder.encode(userDetails.getPassword()), userDetails.getAuthorities());
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
	
	public boolean passwordsMatch(String rawPassword) {
		return passwordEncoder.matches(rawPassword, passwordEncoder.encode(rawPassword));
	}

}

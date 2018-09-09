package com.revature.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.revature.model.User;
import com.revature.repository.UserRepository;

@Service("customUserDetailsService")
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadByUsernameAndPassword(String username, String password) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) 
			throw new UsernameNotFoundException("Invalid Credentials");
		if (passwordEncoder.matches(password, user.getPassword())) {
			UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(username);
			builder.password(user.getPassword());
			builder.disabled(!user.isEnabled());
			builder.authorities(user.getAuthorities().stream().map(auth -> auth.getAuthority()).toArray(String[]::new));
			return builder.build();
		}
		throw new UsernameNotFoundException("Invalid Credentials");
	}
	
	@Override
	public UserDetails loadByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user != null) {
			UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(user.getUsername());
			builder.password(user.getPassword());
			builder.disabled(!user.isEnabled());
			builder.authorities(user.getAuthorities().stream().map(auth -> auth.getAuthority()).toArray(String[]::new));
			return builder.build();
		}
		throw new UsernameNotFoundException("Invalid Credentials");
	}
}

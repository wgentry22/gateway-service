package com.revature.service;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.revature.model.Authorities;
import com.revature.model.User;
import com.revature.repository.AuthoritiesRepository;
import com.revature.repository.UserRepository;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthoritiesRepository authoritiesRepository;

	@Override
	public UserDetails loadUserByUsername(final String incomingUsername) throws UsernameNotFoundException {
		if (!incomingUsername.contains(" "))
			throw new UsernameNotFoundException("Invalid Authentication Request");
		else {
			final String[] credentials = incomingUsername.split(" ");
			User user = userRepository.findByUsername(credentials[0]);
			UserBuilder builder = null;
			if (user == null) 
				throw new UsernameNotFoundException("Invalid Credentials");
			else {
				builder = org.springframework.security.core.userdetails.User.withUsername(credentials[0]);
				builder.disabled(!user.isEnabled());
				builder.password(user.getPassword());
				String[] authorities = authoritiesRepository.findByUser(user).stream().map(a -> a.getAuthority()).toArray(String[]::new);
				builder.authorities(authorities);
				return builder.build();
			}
		}
	}

}

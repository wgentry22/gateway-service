package com.revature.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailsService {

	UserDetails loadByUsernameAndPassword(String username, String password) throws UsernameNotFoundException;
	UserDetails loadByUsername(String username) throws UsernameNotFoundException;
}

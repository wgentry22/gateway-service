package com.revature.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	private PasswordEncoder passwordEncoder;
	private CustomUserDetailsService customUserDetailsService;
	
	public CustomUserDetailsAuthenticationProvider(PasswordEncoder passwordEncoder, CustomUserDetailsService customUserDetailsService) {
		super();
		this.passwordEncoder = passwordEncoder;
		this.customUserDetailsService = customUserDetailsService;
	}
	
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		if (authentication.getCredentials() == null) {
			logger.debug("Authentication Failed: No credentials provided");
			throw new BadCredentialsException("Bad Credentials");
		}
		
		String password = authentication.getCredentials().toString();
		
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			logger.debug("Value of password: " + password);
			logger.debug("Value of userDetails.getPassword(): " + userDetails.getPassword());
			throw new BadCredentialsException("Bad Credentials"); 
		}
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		UserDetails authenticated = null;
		try {
			authenticated = this.customUserDetailsService.loadByUsernameAndPassword(authentication.getPrincipal().toString(), authentication.getCredentials().toString());
		} catch (UsernameNotFoundException userNotFound) {
			logger.info("Bad Credentials: ", userNotFound);
		}
		if (authenticated == null) {
            throw new InternalAuthenticationServiceException("UserDetailsService returned null, "
                + "which is an interface contract violation");
        }
		return authenticated;
	}

	
}

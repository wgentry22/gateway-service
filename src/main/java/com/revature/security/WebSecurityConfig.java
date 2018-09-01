package com.revature.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.netflix.zuul.ZuulFilter;
import com.revature.security.jwt.JwtAuthenticationFilter;
import com.revature.zuul.filter.PreFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	/**
	 * @author William
	 */
	
	// Old Configuration
//	@Autowired
//	private UserDetailsServiceImpl userDetailsService;
	
//	@Autowired
//	private CustomAuthenticationProvider authProvider;
	
//	@Autowired
//	private JwtAuthenticationEntryPoint unauthorizedHandler;
	
	// New Configuration
	@Autowired
	private CustomUserDetailsService customUserDetailsService; 
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
		return new JwtAuthenticationFilter();
	}
	
	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}
	
	// Old Configuration
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http
//			.authorizeRequests().antMatchers("/login", "/favicon.ico", "/webjars**").anonymous().anyRequest().permitAll();
//		http
//			.cors().and().csrf().disable()
//			.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//			.authorizeRequests().antMatchers(HttpMethod.POST, "/generate-token").permitAll();
//	}
	
	// New Configuration
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.cors().and().csrf().disable()
			.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.authorizeRequests().antMatchers(HttpMethod.GET, "/login.html").permitAll()
//			.formLogin().loginPage("/login.html").loginProcessingUrl("/generate-token").defaultSuccessUrl("/application-service")
			.and()
			.authorizeRequests().antMatchers(HttpMethod.POST, "/generate-token").permitAll();
	}
	
	public AuthenticationProvider authProvider() {
		CustomUserDetailsAuthenticationProvider provider = new CustomUserDetailsAuthenticationProvider(passwordEncoder(), customUserDetailsService);
		return provider;
	}
	
	@Bean
	public CustomAuthenticationFilter authenticationFilter() throws Exception {
		CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManagerBean());
		return filter;
	}
	
	@Autowired
	public ZuulFilter preFilter() {
		return new PreFilter();
	}
}

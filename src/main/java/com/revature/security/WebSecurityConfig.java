package com.revature.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.netflix.zuul.ZuulFilter;
import com.revature.security.jwt.JwtAuthenticationEntryPoint;
import com.revature.security.jwt.JwtAuthenticationFilter;
import com.revature.zuul.filter.PreFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	/**
	 * @author William
	 */
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService; 
	
	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean() 
	public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
		return new JwtAuthenticationFilter();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}
	
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/webjars/**").permitAll();
		http
			.cors().configurationSource(corsConfigurationSource())
			.and().csrf().disable()
			.authorizeRequests().anyRequest().authenticated().and()
			.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.addFilterAt(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
			.formLogin()
				.loginPage("http://localhost:4200")
				.loginProcessingUrl("/authenticate")
				.successHandler(getCustomAuthenticationSuccessHandler())
				.permitAll()
			.and()
				.logout()
				.logoutUrl("/logout")
				.deleteCookies(new String[] {"JSESSIONID"})
				.clearAuthentication(true)
				.invalidateHttpSession(true)
				.logoutSuccessUrl("http://localhost:4200")
			.and()
				.sessionManagement()
				.maximumSessions(1)
				.and().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	public CustomUserDetailsAuthenticationProvider authProvider() {
		CustomUserDetailsAuthenticationProvider provider = new CustomUserDetailsAuthenticationProvider(passwordEncoder(), customUserDetailsService);
		return provider;
	}
	
	@Bean
	public CustomAuthenticationFilter authenticationFilter() throws Exception {
		CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManagerBean());
		filter.setAuthenticationSuccessHandler(getCustomAuthenticationSuccessHandler());
		return filter;
	}
	
	@Bean
	public CustomAuthenticationSuccessHandler getCustomAuthenticationSuccessHandler() {
		CustomAuthenticationSuccessHandler handler = new CustomAuthenticationSuccessHandler();
		return handler;
	}
	
	@Bean
	public ZuulFilter preFilter() {
		return new PreFilter();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS"));
		config.setExposedHeaders(Arrays.asList("Authorization"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}

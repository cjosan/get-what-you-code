package com.cjosan.getwhatyoucode.config.security;

import com.cjosan.getwhatyoucode.service.UserService;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private UserService userService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public WebSecurityConfig(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
				.permitAll()
				.antMatchers(HttpMethod.GET, SecurityConstants.VERIFICATION_EMAIL_URL)
				.permitAll()
				.antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_REQUEST_URL)
				.permitAll()
				.antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_URL)
				.permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.addFilter(getAuthenticationFilter())
				.addFilter(new AuthorizationFilter(authenticationManager()))
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
	}

	public AuthenticationFilter getAuthenticationFilter() throws Exception {
		final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
		filter.setFilterProcessesUrl("/users/login");

		return filter;
	}

}

package com.hololive.livestream.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		//web.ignoring().antMatchers("classpath:/static/**", "classpath:/resources/**", "classpath:/META-INF/resources/**", "classpath:/META-INF/resources/webjars/**");
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/**").permitAll();
		http.formLogin()
			.loginPage("/security/login")
			.defaultSuccessUrl("/")
			//.successHandler(new LoginSuccessHandler())
			.failureForwardUrl("/security/denied")
			.permitAll();
		http.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/security/logout"))
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true);
		http.exceptionHandling()
			.accessDeniedPage("/security/denied");
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		
	}
}

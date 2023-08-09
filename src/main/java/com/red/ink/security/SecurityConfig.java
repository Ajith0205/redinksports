package com.red.ink.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.red.ink.jwt.JwtAuthenticationFilter;
import com.red.ink.jwt.JwtAuthorizationFilter;
import com.red.ink.repository.RoleRepository;
import com.red.ink.repository.UserRepository;
import com.red.ink.serviceimpl.UserPrincipalDetailsServiceImpl;



@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
    private	 UserRepository userRepository;
	
	@Autowired
	
   private	RoleRepository  roleRepository;
	
	@Autowired
	private UserPrincipalDetailsServiceImpl principalDetailsServiceImpl;
	
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter2 = new JwtAuthenticationFilter(authenticationManager(),
				userRepository);
		jwtAuthenticationFilter2.setFilterProcessesUrl("/user/login");
		http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.addFilter(jwtAuthenticationFilter2)
				.addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository)).authorizeRequests()
				.antMatchers(HttpMethod.POST, "/user/forgotpassword","/user/forgotpasswordOTP","/user/checkOtp","/user/mobileotp","/user/checkMobileOtp").permitAll().antMatchers(HttpMethod.POST, "/user/").permitAll()
				.antMatchers(HttpMethod.GET, "/getBulkUploadTemplate").permitAll()
				//.antMatchers("/swagger-ui.html").permitAll()
				.antMatchers("/v2/api-docs","/home").permitAll()
				.antMatchers(HttpMethod.POST, "/user/login","/user/save").permitAll().antMatchers(HttpMethod.POST, "/user/").permitAll()
				//.antMatchers(HttpMethod.POST, "/user/register").permitAll().antMatchers(HttpMethod.POST, "/user")
				.antMatchers(HttpMethod.POST, "/").permitAll()
				
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll().antMatchers("/**")
				.access("hasRole('ROLE_ADMIN') or hasRole('ROLE_Trainer') or hasRole('ROLE_Player')")
				.anyRequest().authenticated();
		http.cors();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(Arrays.asList("*"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(this.principalDetailsServiceImpl);
		return daoAuthenticationProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// Allow swagger to be accessed without authentication
		web.ignoring()
		        .antMatchers("/v2/api-docs").antMatchers("/swagger-resources/**").antMatchers("/swagger-ui.html")
				.antMatchers("/configuration/**").antMatchers("/webjars/**").antMatchers("http://localhost:4200/**")
				.antMatchers("/public");
	}
	
	

}

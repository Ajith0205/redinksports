package com.red.ink.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.red.ink.dto.LoginDto;
import com.red.ink.model.User;
import com.red.ink.model.UserPrincipal;
import com.red.ink.repository.UserRepository;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	//UserRepository (userLoginReposirory ) is  temporarily used
	
	UserRepository userLoginRepository;
	
	private AuthenticationManager authenticationManager;
	
	LoginDto credentials = null;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userLoginRepository) {
		super.setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		this.authenticationManager = authenticationManager;
		this.userLoginRepository = userLoginRepository;
	}
	
	// request with /login POST method will directly come here...
		@Override
		public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
				throws AuthenticationException {

			try {
				this.credentials = new ObjectMapper().readValue(request.getInputStream(), LoginDto.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					this.credentials.getUsername(), this.credentials.getPassword(), new ArrayList<>());
			Authentication auth = authenticationManager.authenticate(authToken);
			return auth;
			
			
			
			
		}

		@Override
		protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
				Authentication authResult) throws IOException, ServletException {
			UserPrincipal userPrincipal = (UserPrincipal) authResult.getPrincipal();
			Optional<User> user = userLoginRepository.findByUsername(userPrincipal.getUsername());
			String id = "id";
			String role = "role";
			String username = "username";
			String roleId="roleId";
			String token = JWT.create().withSubject(userPrincipal.getUsername())
					.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION))
					.sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));
			//token generated
			response.addHeader(JwtProperties.HEADER, JwtProperties.PREFIX + token);
			String tokenConstruct = "{" + "\"" + JwtProperties.HEADER + "\":\"" + JwtProperties.PREFIX + token + "\","
					+ "\"" + id + "\":\"" + user.get().getId() +  "\","
					+ "\"" + role + "\":\"" + user.get().getRole().getRole() +  "\","
					+ "\"" + username + "\":\"" + user.get().getName() +  "\","
					+"\"" + roleId + "\":\"" + user.get().getRole().getId() +  "\","
					+ "\"code\":" + 200 + "}";
			String jsonResponse = "{\"response\": " + tokenConstruct + ", " + "\"status\": " + true + "}";

			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(jsonResponse);
			out.flush();

		}
	
	

}

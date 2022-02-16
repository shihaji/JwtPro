package com.example.demo;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{

	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtUtil jwtUtil;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authorizationHeader =request.getHeader("Authorization");
		
		String username=null;
		String jwt=null;
		
		if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")) {
			
			jwt=authorizationHeader.substring(7);
			
			username=jwtUtil.extractUsername(jwt);
		
			
			if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
				
				UserDetails userDetails=this.userDetailsService.loadUserByUsername(username);
				
				
				
				if(jwtUtil.validateToken(jwt,userDetails)) {
					
				
					
					UsernamePasswordAuthenticationToken upt=
							new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
					
					upt.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					
					SecurityContextHolder.getContext().setAuthentication(upt);
					
					
				}else {
					
					
					
				}
				
				
			}
			
		}
		
		filterChain.doFilter(request, response);
	
		
		
	}
	
	

}

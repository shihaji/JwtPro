package com.example.demo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	
	private String SECRET_KEY="secret";
	
	
	public String generateToken(UserDetails userDetails) {
		
		Map<String,Object> map=new HashMap<String,Object>();
		
		map.put("enabled", userDetails.isEnabled());
		
		return Jwts.builder()
			.setClaims(map)
			.setHeaderParam("type", "jwt")
			.setSubject(userDetails.getUsername())
		    .setIssuedAt(new Date(System.currentTimeMillis()))
		    .setExpiration(new Date(System.currentTimeMillis()+(1000*60*60*10)))
		    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
		    .compact();
		
		
	}


	public String extractUsername(String jwt) {
		
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody().getSubject();
	}


	public boolean validateToken(String jwt, UserDetails userDetails) {
		
		String name=extractUsername(jwt);
		
		return (name.equals(userDetails.getUsername()) && 
				Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody().getExpiration().after(new Date()));
			
	}
	

}

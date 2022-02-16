package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

	@Autowired
	AuthenticationManager manager;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@PostMapping("authenticate")
	public ResponseEntity authenticate(@RequestBody User user) {
		
		
		try {
		manager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
		
		UserDetails userDetails =userDetailsService.loadUserByUsername(user.getUsername());
		
		String token=jwtUtil.generateToken(userDetails);
		
		return new ResponseEntity(token,HttpStatus.OK);
		
		}catch(Exception e) {
		
			
			return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	@GetMapping("validate")
	public Boolean  validate() {
		return true;
	}
	
	
}

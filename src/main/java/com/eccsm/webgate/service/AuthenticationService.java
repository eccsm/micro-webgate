package com.eccsm.webgate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.eccsm.webgate.model.User;
import com.eccsm.webgate.security.UserPrincipal;
import com.eccsm.webgate.security.jwt.IJwtProvider;

@Service
public class AuthenticationService implements IAuthenticationService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private IJwtProvider jwtProvider;

	@Override
	public String loginAndReturnToken(User registered) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(registered.getUsername(), registered.getPassword()));

		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

		return jwtProvider.generateToken(userPrincipal);
	}

}

package com.eccsm.webgate.security.jwt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

import com.eccsm.webgate.security.UserPrincipal;

public interface IJwtProvider {

	String generateToken(UserPrincipal authentication);

	Authentication getAuthentication(HttpServletRequest request);

	boolean validateToken(HttpServletRequest request);

}

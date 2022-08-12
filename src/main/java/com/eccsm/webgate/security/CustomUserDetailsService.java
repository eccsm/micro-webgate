package com.eccsm.webgate.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eccsm.webgate.model.User;
import com.eccsm.webgate.repository.IUser;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private IUser repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = repository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("User can not found with given username: " + username));

		return new UserPrincipal(user.getId(), user.getUsername(), user.getPassword());
	}

}

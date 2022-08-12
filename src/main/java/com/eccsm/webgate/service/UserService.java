package com.eccsm.webgate.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eccsm.webgate.model.User;
import com.eccsm.webgate.repository.IUser;

@Service
public class UserService implements IUserService {

	@Autowired
	private IUser repository;

	@Autowired
	private PasswordEncoder encoder;

	@Override
	public User save(User user) {
		user.setCreatedAt(LocalDateTime.now());
		user.setPassword(encoder.encode(user.getPassword()));
		return repository.save(user);
	}

	@Override
	public List<User> getUsers() {
		return repository.findAll();
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return repository.findByUsername(username);
	}

	@Override
	public void delete(Long userId) {
		repository.deleteById(userId);
	}

}

package com.eccsm.webgate.service;

import java.util.List;
import java.util.Optional;

import com.eccsm.webgate.model.User;

public interface IUserService {

	List<User> getUsers();

	Optional<User> findByUsername(String username);

	void delete(Long userId);

	User save(User user);

}

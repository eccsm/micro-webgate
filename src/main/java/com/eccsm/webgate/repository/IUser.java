package com.eccsm.webgate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eccsm.webgate.model.User;

public interface IUser extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);
}

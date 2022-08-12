package com.eccsm.webgate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eccsm.webgate.model.User;
import com.eccsm.webgate.service.IAuthenticationService;
import com.eccsm.webgate.service.IUserService;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {

	@Autowired
	IAuthenticationService authService;

	@Autowired
	IUserService userService;

	@PostMapping("register")
	public ResponseEntity<?> register(@RequestBody User user) {
		if (userService.findByUsername(user.getUsername()).isPresent())
			return new ResponseEntity<>(HttpStatus.CONFLICT);

		return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
	}

	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody User user) {
		return new ResponseEntity<>(authService.loginAndReturnToken(user), HttpStatus.OK);
	}
}

package com.eccsm.webgate.service;

import com.eccsm.webgate.model.User;

public interface IAuthenticationService {

	String loginAndReturnToken(User registered);

}

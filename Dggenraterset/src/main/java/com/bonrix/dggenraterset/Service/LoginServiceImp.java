package com.bonrix.dggenraterset.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.User;
import com.bonrix.dggenraterset.Repository.UserRepository;

@Service("LoginService")
public class LoginServiceImp implements LoginService {

	@Autowired
	UserRepository  repository;
	
	@Override
	public User Login(String username) {
		return repository.findByUserName(username);
	}
}

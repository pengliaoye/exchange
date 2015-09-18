package com.dm.service;

import javax.enterprise.context.Dependent;

import com.dm.entity.User;

@Dependent
public class UserService extends AbstractService<User> {

	public UserService() {
		super(User.class);
	}

}

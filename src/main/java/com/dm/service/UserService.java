package com.dm.service;


import com.dm.entity.User;
import javax.ejb.Stateless;

@Stateless
public class UserService extends AbstractService<User> {

	public UserService() {
		super(User.class);
	}

}

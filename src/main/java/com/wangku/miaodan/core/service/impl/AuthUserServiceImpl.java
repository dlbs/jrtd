package com.wangku.miaodan.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangku.miaodan.core.dao.AuthUserMapper;
import com.wangku.miaodan.core.model.AuthUser;
import com.wangku.miaodan.core.service.IAuthUserService;

@Service
public class AuthUserServiceImpl implements IAuthUserService {
	
	@Autowired
	private AuthUserMapper authUserMapper;

	@Override
	public AuthUser getDetailByUserAndPass(String username, String password) {
		return authUserMapper.getDetailByNameAndPass(username, password);
	}

}

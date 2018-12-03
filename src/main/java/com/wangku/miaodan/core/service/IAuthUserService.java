package com.wangku.miaodan.core.service;

import com.wangku.miaodan.core.model.AuthUser;

public interface IAuthUserService {
	
	public AuthUser getDetailByUserAndPass(String username, String password);

}

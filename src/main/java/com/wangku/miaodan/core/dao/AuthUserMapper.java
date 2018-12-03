package com.wangku.miaodan.core.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.wangku.miaodan.core.model.AuthUser;

@Repository
public interface AuthUserMapper {
	
	public AuthUser getDetailByNameAndPass(@Param("username")String username, @Param("password")String password);
    
}
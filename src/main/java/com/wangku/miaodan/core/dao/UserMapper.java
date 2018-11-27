package com.wangku.miaodan.core.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.wangku.miaodan.core.model.User;

@Repository
public interface UserMapper {
	
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

	void addUser(@Param("mobile")String mobile);

	User selectByMobile(@Param("mobile")String mobile);

	long getTimesOfUser(@Param("mobile")String mobile, @Param("isTD")boolean isTD);

	void reduceTimesByMobile(@Param("mobile")String mobile, @Param("isTD")boolean isTD);

	void incrTimes(@Param("mobile")String mobile, @Param("times")int times);

	void updateByMobile(User user);

	void increTimesByRechargeNumber(@Param("number")String number, @Param("jpTimes")int jpTimes, @Param("tdTimes")int tdTimes);

	void addOpenId(@Param("mobile")String mobile, @Param("opendId")String openId);
    
}
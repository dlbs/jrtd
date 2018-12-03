package com.wangku.miaodan.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.wangku.miaodan.core.model.Recharge;

@Repository
public interface RechargeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Recharge record);

    int insertSelective(Recharge record);

    Recharge selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Recharge record);

    int updateByPrimaryKey(Recharge record);
    
    long getLimitOfUser(@Param("mobile")String mobile);
    
  	void reduceLimitByMobile(@Param("mobile")String mobile);

	Recharge getDetailByIdAndMobile(@Param("id")Long id, @Param("mobile")String mobile);

	void updateStatusByNumber(@Param("number")String number, @Param("status")int status);

	Recharge getdetailByNumber(@Param("number")String number);

	List<Recharge> list(@Param("start")int page, @Param("size")int size);
  	
}
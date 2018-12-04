package com.wangku.miaodan.core.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.wangku.miaodan.core.model.Requit;

@Repository
public interface RequitMapper {
	
    int deleteByPrimaryKey(Long id);

    int insert(Requit record);

    int insertSelective(Requit record);

    Requit selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Requit record);

    int updateByPrimaryKey(Requit record);

	List<Requit> list(@Param("start")int start, @Param("size")int size);

	Requit selectByOrderId(@Param("orderId")long orderId, @Param("mobile")String mobile);

	long count();
}
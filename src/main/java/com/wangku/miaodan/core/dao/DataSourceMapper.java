package com.wangku.miaodan.core.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.wangku.miaodan.core.model.DataSource;

@Repository
public interface DataSourceMapper {
	
	DataSource selectByPrimaryKey(Long id);
	
    int insert(DataSource record);

    int updateByPrimaryKeySelective(DataSource record);

	List<DataSource> list(@Param("code")String code, @Param("status")String status, @Param("start")int start, @Param("size")int size);

	long count(@Param("code")String code, @Param("status")String status);

	DataSource selectByCode(@Param("code")String code);

}
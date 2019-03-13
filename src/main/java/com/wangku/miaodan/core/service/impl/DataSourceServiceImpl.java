package com.wangku.miaodan.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangku.miaodan.core.dao.DataSourceMapper;
import com.wangku.miaodan.core.model.DataSource;
import com.wangku.miaodan.core.service.IDataSourceService;
import com.wangku.miaodan.utils.Strings;

@Service
public class DataSourceServiceImpl implements IDataSourceService {
	
	@Autowired
	private DataSourceMapper dataSourceMapper;
	
	@Override
	public DataSource getById(Long id) {
		return dataSourceMapper.selectByPrimaryKey(id);
	}	

	@Override
	public List<DataSource> list(String code, String status, int start, int size) {
		if (Strings.isBlank(code)) {
			code = null;
		}
		if (Strings.isBlank(status)) {
			status = null;
		}
		return dataSourceMapper.list(code, status, start, size);
	}

	@Override
	public long count(String code, String status) {
		if (Strings.isBlank(code)) {
			code = null;
		}
		if (Strings.isBlank(status)) {
			status = null;
		}
		return dataSourceMapper.count(code, status);
	}

	@Override
	public void add(DataSource source) {
		if (null != source.getId()) {
			dataSourceMapper.updateByPrimaryKeySelective(source);
		} else {
			dataSourceMapper.insert(source);
		}
	}

	@Override
	public void update(DataSource source) {
		dataSourceMapper.updateByPrimaryKeySelective(source);
	}

}

package com.wangku.miaodan.core.service;

import java.util.List;

import com.wangku.miaodan.core.model.DataSource;

public interface IDataSourceService {

	public List<DataSource> list(String code, String status, int start, int size);

	public long count(String code, String status);

	public void add(DataSource source);

	public void update(DataSource source);

	public DataSource getById(Long id);

}

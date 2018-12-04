package com.wangku.miaodan.core.service;

import java.util.List;

import com.wangku.miaodan.core.model.Order;
import com.wangku.miaodan.web.SearchBean;

public interface IOrderService {

	public List<Order> selectByCondition(SearchBean condition);

	public Order getOrderBySourceIdAndType(String sourceId, int type);

	public List<Order> getStoredByUser(String mobile);

	public boolean checkIsMine(String mobile, Long id);

	public void saveBatch(List<Order> list);

	public Order getOrderById(Long id);

	public List<Order> list(int i, int size);

	public void updateStatus(Long productId, int status);

	public long count();
	
}

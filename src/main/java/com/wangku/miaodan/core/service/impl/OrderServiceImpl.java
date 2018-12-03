package com.wangku.miaodan.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangku.miaodan.core.dao.OrderMapper;
import com.wangku.miaodan.core.model.Order;
import com.wangku.miaodan.core.service.IOrderService;
import com.wangku.miaodan.web.SearchBean;

@Service
public class OrderServiceImpl implements IOrderService {
	
	@Autowired
	private OrderMapper orderMapper;

	@Override
	public List<Order> selectByCondition(SearchBean condition) {
		Integer page = condition.getPage();
		page = page == null? 1:page <= 0? 1 : page;
		condition.setPage((condition.getPage() - 1) * condition.getSize());
		return orderMapper.selectByCondition(condition);
	}

	@Override
	public Order getOrderBySourceIdAndType(String sourceId, int type) {
		return orderMapper.selectBySourceIdAndType(sourceId, type);
	}

	@Override
	public List<Order> getStoredByUser(String mobile) {
		return orderMapper.getStoredByUser(mobile);
	}

	@Override
	public boolean checkIsMine(String mobile, Long id) {
		return orderMapper.selectByMobileAndId(mobile, id) > 0;
	}

	@Override
	public void saveBatch(List<Order> list) {
		if (list.size() > 0) {
			orderMapper.saveBatch(list);
		}
	}

	@Override
	public Order getOrderById(Long id) {
		return orderMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Order> list(int start, int size) {
		return orderMapper.list(start, size);
	}

	@Override
	public void updateStatus(Long productId, int status) {
		Order order = new Order();
		order.setId(productId);
		order.setStatus(status);
		orderMapper.updateByPrimaryKeySelective(order);
	}

}

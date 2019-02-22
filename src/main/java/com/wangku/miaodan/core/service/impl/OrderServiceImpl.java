package com.wangku.miaodan.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangku.miaodan.core.dao.OrderMapper;
import com.wangku.miaodan.core.model.Order;
import com.wangku.miaodan.core.service.IOrderService;
import com.wangku.miaodan.utils.Strings;
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
	public List<Order> getStoredByUser(String mobile, boolean isTD, int start, int size) {
		return orderMapper.getStoredByUser(mobile, isTD, start, size);
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
	public List<Order> list(String applyTimeStart, String applyTimeEnd, String source, String status, String city, int start, int size) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("size", size);
		map.put("applyTimeStart", Strings.isBlank(applyTimeStart)? null:applyTimeStart + " 00:00:00");
		map.put("applyTimeEnd", Strings.isBlank(applyTimeEnd)? null:applyTimeEnd + " 23:59:59");
		map.put("source", Strings.isBlank(source)? null:source);
		map.put("city", Strings.isBlank(city)? null:city.replace("市", ""));
		try {
			map.put("status", Integer.parseInt(status));
		} catch (NumberFormatException e) {}
		
		
		return orderMapper.list(map);
	}	

	@Override
	public void updateStatus(Long productId, int status) {
		Order order = new Order();
		order.setId(productId);
		order.setStatus(status);
		orderMapper.updateByPrimaryKeySelective(order);
	}

	@Override
	public long count(String applyTimeStart, String applyTimeEnd, String source, String status, String city) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("applyTimeStart", Strings.isBlank(applyTimeStart)? null:applyTimeStart + " 00:00:00");
		map.put("applyTimeEnd", Strings.isBlank(applyTimeEnd)? null:applyTimeEnd + " 23:59:59");
		map.put("source", Strings.isBlank(source)? null:source);
		map.put("city", Strings.isBlank(city)? null:city.replace("市", ""));
		try {
			map.put("status", Integer.parseInt(status));
		} catch (NumberFormatException e) {}	
		
		return orderMapper.count(map);
	}

	@Override
	public void updateMobileAndIdent(Order order) {
		orderMapper.updateMobileAndIdent(order);
	}

}

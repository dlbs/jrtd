package com.wangku.miaodan.core.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wangku.miaodan.core.dao.OrderMapper;
import com.wangku.miaodan.core.dao.RechargeMapper;
import com.wangku.miaodan.core.dao.StoredOrderMapper;
import com.wangku.miaodan.core.dao.UserMapper;
import com.wangku.miaodan.core.model.Recharge;
import com.wangku.miaodan.core.model.User;
import com.wangku.miaodan.core.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RechargeMapper rechargeMapper;
	
	@Autowired
	private StoredOrderMapper storedOrderMapper;
	
	@Autowired
	private OrderMapper orderMapper;

	@Override
	public boolean checkCanStore(String mobile) {
		// 暂时只判断是否还有剩余抢单次数
		return userMapper.getTimesOfUser(mobile) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public long storeOrder(Long orderId, String mobile) {
		// 0 已经被抢， 1 抢单成功
		int counsumeOrder = orderMapper.counsumeOrder(orderId);
		if (counsumeOrder > 0) {
			storedOrderMapper.insert(orderId, mobile);
			userMapper.reduceTimesByMobile(mobile);
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public void addUser(String mobile) {
		userMapper.addUser(mobile);
	}
	
	@Override
	public User getDetailByMobile(String mobile) {
		return userMapper.selectByMobile(mobile);
	}

	@Override
	public void incrTimesByMobile(String mobile, int times) {
		
	}

	@Override
	public void update(User user) {
		user.setUpdateTime(new Date());
		userMapper.updateByMobile(user);
	}

	@Override
	public Long addRecharge(Recharge recharge) {
		rechargeMapper.insert(recharge);
		return recharge.getId();
	}

	@Override
	public void notifyResultOfPay(Map<String, Object> map) {
		rechargeMapper.updateStatusByNumber(map);
	}

}

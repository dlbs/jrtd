package com.wangku.miaodan.core.service;

import java.util.Map;

import com.wangku.miaodan.core.model.Recharge;
import com.wangku.miaodan.core.model.User;

public interface IUserService {

	public boolean checkCanStore(String mobile);

	public long storeOrder(Long orderId, String mobile);

	public void addUser(String mobile);	
	
	public void incrTimesByMobile(String mobile, int times);

	public void update(User user);
	
	public User getDetailByMobile(String mobile);

	public Long addRecharge(Recharge recharge);

	public void notifyResultOfPay(Map<String, Object> map);

}

package com.wangku.miaodan.core.service;

import com.wangku.miaodan.core.model.Recharge;

public interface IRechargeService {
	
	public Recharge getDetailByIdAndMobile(Long id, String mobile);

}

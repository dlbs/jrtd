package com.wangku.miaodan.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangku.miaodan.core.dao.RechargeMapper;
import com.wangku.miaodan.core.model.Recharge;
import com.wangku.miaodan.core.service.IRechargeService;

@Service
public class RechargeServiceImpl implements IRechargeService {
	
	@Autowired
	private RechargeMapper rechargeMapper;

	@Override
	public Recharge getDetailByIdAndMobile(Long id, String mobile) {
		return rechargeMapper.getDetailByIdAndMobile(id, mobile);
	}

}

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

	@Override
	public void notifySuccessByNumber(String number) {
		rechargeMapper.updateStatusByNumber(number, 2);
	}

	@Override
	public void notifyFailByNumber(String number) {
		rechargeMapper.updateStatusByNumber(number, 3);
	}

	@Override
	public Recharge getdetailByNumber(String number) {
		return rechargeMapper.getdetailByNumber(number);
	}


}

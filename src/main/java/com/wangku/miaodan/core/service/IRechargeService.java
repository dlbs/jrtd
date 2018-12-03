package com.wangku.miaodan.core.service;

import java.util.List;
import java.util.Map;

import com.wangku.miaodan.core.model.Recharge;

public interface IRechargeService {
	
	public Recharge getDetailByIdAndMobile(Long id, String mobile);

	public void notifySuccessByNumber(String number);

	public void notifyFailByNumber(String number);

	public Recharge getdetailByNumber(String number);
	
	public List<Recharge> list(int page, int size);

}

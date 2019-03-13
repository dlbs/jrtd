package com.wangku.miaodan.core.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wangku.miaodan.constant.OrderSourceTypeEnum;
import com.wangku.miaodan.constant.ProductTypeEnums;
import com.wangku.miaodan.core.dao.DataSourceMapper;
import com.wangku.miaodan.core.dao.OrderMapper;
import com.wangku.miaodan.core.dao.RechargeMapper;
import com.wangku.miaodan.core.dao.StoredOrderMapper;
import com.wangku.miaodan.core.dao.UserMapper;
import com.wangku.miaodan.core.model.DataSource;
import com.wangku.miaodan.core.model.Order;
import com.wangku.miaodan.core.model.Recharge;
import com.wangku.miaodan.core.model.User;
import com.wangku.miaodan.core.service.IUserService;
import com.wangku.miaodan.utils.HttpUtils;
import com.wangku.miaodan.utils.Strings;

@Service
public class UserServiceImpl implements IUserService {
	
	private static final Logger LOG = Logger.getLogger(IUserService.class);
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RechargeMapper rechargeMapper;
	
	@Autowired
	private StoredOrderMapper storedOrderMapper;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private DataSourceMapper datasourceMapper;

	@Override
	public boolean checkCanStore(String mobile, boolean idTD) {
		return userMapper.getTimesOfUser(mobile, idTD) > 0;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public long storeOrder(Order order, String mobile, boolean isTD) {
		Long orderId = order.getId();
		int counsumeOrder = orderMapper.counsumeOrder(orderId);
		if (counsumeOrder > 0) {
			userMapper.reduceTimesByMobile(mobile, isTD);
			storedOrderMapper.insert(orderId, mobile, isTD? 0:1);
			DataSource dataSource = datasourceMapper.selectByCode(order.getSource());
			if (dataSource.getSensitive() == 0 && !transeData(order, dataSource, 0)) {
				throw new NullPointerException("订单 " + order.getId() + " 敏感信息转换失败");
			}
			return 1;
		} else {
			return 0;
		}
	}	
	
	private boolean transeData(Order order, int count) {
		boolean flag = false;
		if (count >= 3) {
			return flag;
		}
		try {
			OrderSourceTypeEnum.getInstByName(order.getSource()).transferSensitive(JSON.parseObject(order.getMkj(), Map.class), order);
			orderMapper.updateMobileAndIdent(order);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			transeData(order, ++count);
		}		
		return flag;
	}
	
	private boolean transeData(Order order, DataSource source, int count) {
		boolean flag = false;
		if (count >= 3) {
			return flag;
		}
		// 接口请求方式
		try {
			if ("post".equals(source.getReqType())) {// post请求方式
				Map parseObject = JSON.parseObject(order.getMkj(), Map.class);
				JSONObject result = JSON.parseObject(HttpUtils.post(source.getUrl(), JSON.toJSONString(parseObject)));
				if (source.getCallStatusSuccessValue().equals(result.getString(source.getCallStatusField()))) {
					String[] split = source.getSensitiveValueField().split("\\.");
					for (int i = 0; i < split.length; i++) {
						if(i == split.length - 1) {// 最后一层， 直接提取字段值
							order.setMobile(result.getString(split[i]));
							orderMapper.updateMobileAndIdent(order);
							flag = true;
						} else {
							result = JSON.parseObject(JSON.toJSONString(result.get(split[i])));
						}
					}
				}
			} else {// get请求方式
				// 获取参数
				String str = HttpUtils.get(source.getUrl() + JSON.parseObject(order.getMkj()).getString(source.getReqParam()));
				JSONObject result = JSON.parseObject(str);
				if (source.getCallStatusSuccessValue().equals(result.getString(source.getCallStatusField()))) {
					String[] split = source.getSensitiveValueField().split(",");
					for (int i = 0; i < split.length; i++) {
						if(i == split.length - 1) {// 最后一层， 直接提取字段值
							order.setMobile(result.getString(split[i]));
							orderMapper.updateMobileAndIdent(order);
							flag = true;
						} else {
							result = JSON.parseObject(JSON.toJSONString(result.get(split[i])));
						}
					}
				}			
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = transeData(order, source, ++count);
		}
		return flag;
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
	public void recharge(Map<String, String> restmap) {
		ProductTypeEnums byType = ProductTypeEnums.getByType(Long.parseLong(restmap.get("attach")));
		LOG.info("用户充值成功，更新抢单机会:" + byType.getJpTimes() + "---------" + byType.getTdTimes());
		userMapper.increTimesByRechargeNumber(restmap.get("out_trade_no"), byType.getJpTimes(), byType.getTdTimes());
	}

	@Override
	public void addOpenId(String mobile, String openId) {
		userMapper.addOpenId(mobile, openId);
	}

	@Override
	public boolean isAuth(String mobile) {
		User user = userMapper.selectByMobile(mobile);
		return user.getStatus() == 2;
	}

	
	@Override
	public List<User> list(String city, String status, String addTime, int start, int size) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("city", Strings.isBlank(city)? null: city.replace("市", ""));
		try {
			param.put("status", Integer.parseInt(status));
		} catch (NumberFormatException e) {}
		param.put("addTime", Strings.isBlank(addTime)?null:addTime);
		param.put("start", start);
		param.put("size", size);
		
		return userMapper.list(param);
	}	

	@Override
	public void checkAuth(Long userId, int status) {
		User record = new User();
		record.setId(userId);
		record.setStatus(status);
		userMapper.updateByPrimaryKey(record);
	}

	@Override
	public long count(String city, String status, String addTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("city", Strings.isBlank(city)? null: city.replace("市", ""));
		try {
			param.put("status", Integer.parseInt(status));
		} catch (NumberFormatException e) {}
		param.put("addTime", Strings.isBlank(addTime)?null:addTime);
		return userMapper.count(param);
	}

	@Override
	public User getDetailById(Long id) {
		return userMapper.selectByPrimaryKey(id);
	}

}

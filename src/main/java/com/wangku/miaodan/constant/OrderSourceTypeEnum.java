package com.wangku.miaodan.constant;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wangku.miaodan.core.model.Order;
import com.wangku.miaodan.utils.HttpUtils;
import com.wangku.miaodan.utils.MD5Utils;
import com.wangku.miaodan.utils.Strings;

@SuppressWarnings("rawtypes")
public enum OrderSourceTypeEnum {
	
	M01("微盟", "M01", false, true, "http://m.yidaikuan.net/api/middleware/getRealPhone") {
		@Override
		public void transferSensitive(Map params, Order order) {
			JSONObject result = JSON.parseObject(HttpUtils.post(getUrl(), JSON.toJSONString(params)));
			if ("000000".equals(result.getString("code"))) {
				JSONObject data = JSON.parseObject(JSON.toJSONString(result.get("data")));
				order.setMobile(data.getString("phone"));
			} else {
				throw new NullPointerException("微盟敏感数据转换出错, 参数信息:" + params + ", 错误信息:" + result);
			}
		}
	},
	
	Y02("小豆金融", "Y02", true, true, "http://hd.kapokmedia.com:60521/notify/buytheInformation") {
		@Override
		public void transferSensitive(Map params, Order order) {
			JSONObject result = JSON.parseObject(HttpUtils.post(getUrl(), JSON.toJSONString(params)));
			if ("0".equals(result.getString("code"))) {
				JSONObject data = JSON.parseObject(JSON.toJSONString(result.get("data")));
				
				order.setMobile(data.getString("phone"));
				order.setIdentyNumber(data.getString("identity"));
			} else {
				throw new NullPointerException("小豆金融敏感数据转换出错, 参数信息:" + params + ", 错误信息:" + result);
			}
		}
	},
	
	A03("好时代", "A03", false, true, "http://java.51haoshidai.com/edu/open/getPhone") {
		@Override
		public void transferSensitive(Map params, Order order) throws Exception {
			long time = System.currentTimeMillis()/ 1000;
			Object userId = params.get("userId");
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("userId", String.valueOf(userId));
			map.put("time", String.valueOf(time));
			
			String sign = MD5Utils.getMD5Lower(Strings.packageSign(map, "6b4b7ccf85761c1fd4277fa4caa4e7ab", "platformId"));
			JSONObject result = JSON.parseObject(HttpUtils.get(getUrl() + "?platformId=5&sign=" + sign + "&time=" + time + "&userId=" + String.valueOf(userId)));
			if (result.getInteger("code") == 0) {
				order.setMobile(result.getString("msg"));
			} else {
				throw new NullPointerException("好时代电话转换出错, 参数信息:" + String.valueOf(userId) + ", 错误信息:" + result);
			}
		}
	},
	
	B04("", "B04", false, false, "") {
		@Override
		public void transferSensitive(Map params, Order order) {
			
		}
	};
	
	private String name;
	
	private String desc;
	
	private boolean isTD;
	
	private boolean isTM;
	
	private String url;
	
	private OrderSourceTypeEnum(String desc, String name, boolean isTD, boolean isTM, String url) {
		this.name = name;
		this.desc = desc;
		this.isTD = isTD;
		this.isTM = isTM;
		this.url = url;
	}
	
	public abstract void transferSensitive(Map params, Order order) throws Exception;
	
	public static boolean isTDByDesc(String desc) {
		OrderSourceTypeEnum[] values = OrderSourceTypeEnum.values();
		for (OrderSourceTypeEnum source : values) {
			if (desc.equals(source.getName())) {
				return source.isTD;
			}
		}
		return false;
	}
	
	public static OrderSourceTypeEnum getInstByName(String name) {
		OrderSourceTypeEnum[] values = OrderSourceTypeEnum.values();
		for (OrderSourceTypeEnum type : values) {
			if (type.getName().equals(name)) {
				return type;
			}
		}
		throw new NullPointerException("source`name is invalided...");
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public boolean isTD() {
		return isTD;
	}

	public boolean isTM() {
		return isTM;
	}

	public String getUrl() {
		return url;
	}	

	
}

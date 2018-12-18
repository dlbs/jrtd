package com.wangku.miaodan.constant;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wangku.miaodan.utils.HttpUtils;
import com.wangku.miaodan.utils.MD5Utils;
import com.wangku.miaodan.utils.Strings;

public enum OrderSourceTypeEnum {
	
	M01("微盟", "M01", false, "http://m-dev.yidaikuan.net/api/middleware/getRealPhone") {
		@Override
		public String transMobile(Map params) {
			String post = HttpUtils.post(getUrl(), JSON.toJSONString(params));
			JSONObject obj = JSON.parseObject(post);
			if ("0".equals(Strings.transfer2String(obj.get("code")))) {
				return JSON.parseObject(JSON.toJSONString(obj.get("data"))).getString("phone");
			} else {
				System.out.println("微盟电话转换出错, 参数信息:" + params + ", 错误信息:" + post);
				return null;
			}
		}
	},
	
	Y02("", "Y02", true, "") {
		@Override
		public String transMobile(Map params) {
			return null;
		}
	},
	
	A03("好时代", "A03", false, "http://java.51haoshidai.com/edu/open/getPhone") {
		@Override
		public String transMobile(Map params) {
			long time = System.currentTimeMillis();
			StringBuffer sb = new StringBuffer(500);
			Object userId = params.get("userId");
			sb.append("key=6b4b7ccf85761c1fd4277fa4caa4e7ab").append("&time=").append(time).append("&userId=").append(String.valueOf(userId));
			try {
				String sign = MD5Utils.getMD5Lower(sb.toString());
				String url = getUrl() + "?platformId=2&sign=" + sign + "&time=" + time + "&userId=" + String.valueOf(userId);
				String result = HttpUtils.get(url);
				
				JSONObject parse = JSON.parseObject(result);
				if (parse.getInteger("code") == 0) {
					return parse.getString("code");
				} else {
					System.out.println("好时代电话转换出错, 参数信息:" + url + ", 错误信息:" + parse.getString("code"));
				}
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return null;
		}
	},
	
	B04("", "B04", false, "") {
		@Override
		public String transMobile(Map params) {
			return null;
		}
	};
	
	private String name;
	
	private String desc;
	
	private boolean isTD;
	
	private String url;
	
	private OrderSourceTypeEnum(String desc, String name, boolean isTD, String url) {
		this.name = name;
		this.desc = desc;
		this.isTD = isTD;
		this.url = url;
	}
	
	public abstract String transMobile(Map params);
	
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
	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isTD() {
		return isTD;
	}

	public void setTD(boolean isTD) {
		this.isTD = isTD;
	}

}

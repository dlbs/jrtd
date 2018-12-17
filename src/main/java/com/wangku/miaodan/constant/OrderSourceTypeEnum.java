package com.wangku.miaodan.constant;

public enum OrderSourceTypeEnum {
	
	M01("", "M01", false),
	Y02("", "Y02", true),
	A03("", "A03", false),
	B04("", "B04", false);
	
	private String name;
	
	private String desc;
	
	private boolean isTD;
	
	public static boolean isTDByDesc(String desc) {
		OrderSourceTypeEnum[] values = OrderSourceTypeEnum.values();
		for (OrderSourceTypeEnum source : values) {
			if (desc.equals(source.getName())) {
				return source.isTD;
			}
		}
		return false;
	}
	
	private OrderSourceTypeEnum(String desc, String name, boolean isTD) {
		this.name = name;
		this.desc = desc;
		this.isTD = isTD;
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

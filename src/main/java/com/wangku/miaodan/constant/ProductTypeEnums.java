package com.wangku.miaodan.constant;

import java.math.BigDecimal;


public enum ProductTypeEnums {
	
	PRODUCT_ONE("13精品", 1, 13, 0, new BigDecimal(688)),
	PRODUCT_TWO("32精品", 2, 32, 0, new BigDecimal(1588)),
	PRODUCT_THREE("66精品", 3, 66, 0, new BigDecimal(2888)),
	PRODUCT_FOUR("派单模式", 4, 0, 0, new BigDecimal(5000));
	
	
	private String desc;
	
	private int type;
	
	private int jpTimes;
	
	private int tdTimes;
	
	private BigDecimal price;
	
	private ProductTypeEnums(String desc, int type, int jpTimes, int tdTimes, BigDecimal price) {
		this.desc = desc;
		this.type = type;
		this.jpTimes = jpTimes;
		this.tdTimes = tdTimes;
		this.price = price;
	}
	
	public static ProductTypeEnums getByType(long type) {
		if (type == 1) {
			return PRODUCT_ONE;
		} else if (type == 2) {
			return PRODUCT_TWO;
		} else {
			return PRODUCT_THREE;
		}
			
	}

	public String getDesc() {
		return desc;
	}

	public int getType() {
		return type;
	}

	public int getJpTimes() {
		return jpTimes;
	}

	public int getTdTimes() {
		return tdTimes;
	}

	public BigDecimal getPrice() {
		return price;
	}
	
}

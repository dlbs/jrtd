package com.wangku.miaodan.constant;

import java.math.BigDecimal;


public enum ProductTypeEnums {
	
	PRODUCT_ONE("13精品", 1, 13, 0, new BigDecimal(500)),
	PRODUCT_TWO("30精品", 2, 30, 0, new BigDecimal(1000)),
	PRODUCT_THREE("70精品", 3, 70, 0, new BigDecimal(2000));
	
/*	PRODUCT_ONE("0精品/30淘单", 1, 0, 30, new BigDecimal(688)),
	PRODUCT_TWO("15精品/0淘单", 2, 15, 0, new BigDecimal(1688)),
	PRODUCT_THREE("32精品/15淘单", 3, 30, 15, new BigDecimal(2888)),
	PRODUCT_FOUR("66精品/30淘单", 4, 66, 30, new BigDecimal(5688)),
	PRODUCT_FIVE("102精品/30淘单", 5, 102, 30, new BigDecimal(8088)),
	PRODUCT_SIX("153精品/30淘单", 6, 153, 30, new BigDecimal(10888));*/
	
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

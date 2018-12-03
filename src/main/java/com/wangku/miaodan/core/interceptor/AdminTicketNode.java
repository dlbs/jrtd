package com.wangku.miaodan.core.interceptor;


public class AdminTicketNode {
	
	private String account;
	
	private long invalidTime;// 失效时间(两个小时,溢出重新登录)
	
	private long criticalityTime;// 临界时间(十五分钟， 溢出临界时间且未溢出失效时间 需要重新设置临界时间和失效时间)
	
	public AdminTicketNode() {
		
	}
	
	public AdminTicketNode(String account) {
		this.account = account;
		long now = System.currentTimeMillis();
		invalidTime = now + 2 * 60 * 60 * 1000;
		criticalityTime = now + 45 * 60 * 1000;
	}
	
	public boolean isValid() {
		long now = System.currentTimeMillis();
		if (now < invalidTime) {
			return true;
		} else {
			return false;
		}
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public long getInvalidTime() {
		return invalidTime;
	}

	public long getCriticalityTime() {
		return criticalityTime;
	}

}

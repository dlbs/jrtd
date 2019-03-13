package com.wangku.miaodan.core.model;

import java.util.Date;

public class DataSource {

	private Long id;

	private String name;

	private String code;

	private Integer scramble;

	private Integer sensitive;

	private String url;

	private String reqType;

	private String reqParam;

	private String callStatusField;

	private String callStatusSuccessValue;

	private String sensitiveValueField;

	private Integer status;

	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getScramble() {
		return scramble;
	}

	public void setScramble(Integer scramble) {
		this.scramble = scramble;
	}

	public Integer getSensitive() {
		return sensitive;
	}

	public void setSensitive(Integer sensitive) {
		this.sensitive = sensitive;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getReqParam() {
		return reqParam;
	}

	public void setReqParam(String reqParam) {
		this.reqParam = reqParam;
	}

	public String getCallStatusField() {
		return callStatusField;
	}

	public void setCallStatusField(String callStatusField) {
		this.callStatusField = callStatusField;
	}

	public String getCallStatusSuccessValue() {
		return callStatusSuccessValue;
	}

	public void setCallStatusSuccessValue(String callStatusSuccessValue) {
		this.callStatusSuccessValue = callStatusSuccessValue;
	}

	public String getSensitiveValueField() {
		return sensitiveValueField;
	}

	public void setSensitiveValueField(String sensitiveValueField) {
		this.sensitiveValueField = sensitiveValueField;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "DataSource [id=" + id + ", name=" + name + ", code=" + code
				+ ", scramble=" + scramble + ", sensitive=" + sensitive
				+ ", url=" + url + ", reqType=" + reqType + ", reqParam="
				+ reqParam + ", callStatusField=" + callStatusField
				+ ", callStatusSuccessValue=" + callStatusSuccessValue
				+ ", sensitiveValueField=" + sensitiveValueField + ", status="
				+ status + ", createTime=" + createTime + "]";
	}

}
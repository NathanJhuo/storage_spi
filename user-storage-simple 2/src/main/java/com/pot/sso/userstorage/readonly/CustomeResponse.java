package com.pot.sso.userstorage.readonly;

import java.util.Map;

public class CustomeResponse {
	private String success;
	private Map<String, Object> error;
	private String data;
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public Map<String, Object> getError() {
		return error;
	}
	public void setError(Map<String, Object> error) {
		this.error = error;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

}

package com.kh.api.login.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgEntity {
	private String msg;
	private Object result;
	
	/*
	 @AllArgsConstructor
	 =
	public MsgEntity(String msg, Object result) {
		this.msg = msg;
		this.result = result;
	}
	*/
}

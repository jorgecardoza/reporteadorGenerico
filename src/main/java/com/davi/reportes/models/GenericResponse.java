package com.davi.reportes.models;

import org.springframework.http.ResponseEntity;

public class GenericResponse<T> {
	private int code;
	private String message;
	private T response;

	public ResponseEntity<byte[]> getRespuesa() {
		return respuesa;
	}
	public void setRespuesa(ResponseEntity<byte[]> response) {
		this.respuesa = response;
	}
	public void setResponse(T response) {
		this.response = response;
	}
	private ResponseEntity<byte[]> respuesa;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getResponse() {
		return response;
	}

	
	
	

}

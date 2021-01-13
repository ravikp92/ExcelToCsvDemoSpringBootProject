package com.ravi.exceltocsv.ExcelToCsvProject.model;

/**
 * This is used to generate response message for file upload
 * @author RaviP
 *
 */
public class ResponseMessage {
	private String message;

	public ResponseMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
	    this.message = message;
	  }
}
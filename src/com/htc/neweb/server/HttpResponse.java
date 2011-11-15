package com.htc.neweb.server;

public class HttpResponse {
	public int status_code = 200;
	public String status_text = "OK";
	public String content_type = "text/html";
	
	public byte[] content;
	
	public HttpResponse() {
		
	}
}

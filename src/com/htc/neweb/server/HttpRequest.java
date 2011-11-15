package com.htc.neweb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

public class HttpRequest {
	private String method;
	private String uri;
	private String protocol;
	
	//Http Headers 
	
	
	HttpRequest() {
		super();
	}
	
	public String getMethod() {
		return method;
	}
	
	public String getUri() {
		return uri;
	}
	
	public String getProtocol() {
		return protocol;
	}
	
	public static final HttpRequest parseRequest(InputStream s) throws IOException {
		HttpRequest req = new HttpRequest();
		
		BufferedReader r = new BufferedReader(new InputStreamReader(s));
		String line = r.readLine();
		if(line == null) {
			throw new IOException("Invalid Http request");
		}
		
		String[] lineParts = line.split(" ");
		//TODO: Should check 
		req.method = lineParts[0];
		req.uri = lineParts[1];
		req.protocol = lineParts[2];
		
		//Consume request
		while((line = r.readLine())!=null) {
			Log.d("HttpRequest", line);
			if("".equals(line.trim())) {
				break;
			}
		}
		
		return req;
	}
}

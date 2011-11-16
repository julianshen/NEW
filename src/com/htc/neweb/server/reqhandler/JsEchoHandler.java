package com.htc.neweb.server.reqhandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import com.htc.neweb.server.HttpRequest;
import com.htc.neweb.server.HttpResponse;
import com.htc.neweb.server.RequestHandler;

public class JsEchoHandler implements RequestHandler {

	@Override
	public void doGet(HttpRequest req, HttpResponse resp) {
		String uri = req.getUri();
		String message = "test";
		
		
		HashMap<String, String> params = new HashMap<String, String>();
		if(uri.indexOf('?') > 0) {
			String queryString = uri.substring(uri.indexOf('?') + 1);
			
			//parse queryString
			String[] parts = queryString.split("&");
			if(parts!=null) {
				for(String p:parts) {
					String[] namevaluepair = p.split("=");
					
					if(namevaluepair!=null && namevaluepair.length>0) {
						String name = namevaluepair[0];
						String value = namevaluepair.length > 1 ? namevaluepair[1]:"";
						params.put(name, value);
					}
				}
			}
		}
		
		message = params.get("message");
		if(message == null) {
			message = "No message to echo";
		}
		
		String callback = params.get("callback");
		
		resp.setContentType("text/json");
		
		PrintWriter writer;
		try {
			String output = "{\"message\": \"" +message+ "\"}";
			
			if(callback!=null) {
				//JSONP
				output = callback + "(" + output + ");";
			}
			writer = resp.getWriter();
			writer.print(output);
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}

package com.htc.neweb.server.reqhandler;

import java.io.IOException;
import java.io.PrintWriter;

import com.htc.neweb.server.HttpRequest;
import com.htc.neweb.server.HttpResponse;
import com.htc.neweb.server.RequestHandler;

public class JsEchoHandler implements RequestHandler {

	@Override
	public void doGet(HttpRequest req, HttpResponse resp) {
		String uri = req.getUri();
		String message = "test";
		
		if(uri.indexOf('?') > 0) {
			String queryString = uri.substring(uri.indexOf('?') + 1);
			
			message = queryString; //I'm lazy
		}
		
		resp.setContentType("application/json");
		
		PrintWriter writer;
		try {
			writer = resp.getWriter();
			writer.println("{msg:'" +message+ "'}");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}

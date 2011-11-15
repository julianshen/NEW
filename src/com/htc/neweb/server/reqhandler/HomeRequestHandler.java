package com.htc.neweb.server.reqhandler;

import com.htc.neweb.server.HttpRequest;
import com.htc.neweb.server.HttpResponse;
import com.htc.neweb.server.RequestHandler;

public class HomeRequestHandler implements RequestHandler {

	@Override
	public HttpResponse process(HttpRequest req) {
		HttpResponse resp = new HttpResponse();
		
		resp.content = "<h1>Hello</h1>".getBytes();
		return resp;
	}

}

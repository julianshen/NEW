package com.htc.neweb.server;

public interface RequestHandler {

	public HttpResponse process(HttpRequest req);
}

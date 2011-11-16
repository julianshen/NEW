package com.htc.neweb.server;

public interface RequestHandler {

	public void doGet(HttpRequest req, HttpResponse resp);
}

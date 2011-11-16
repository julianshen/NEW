package com.htc.neweb.server.reqhandler;

import java.io.IOException;
import java.io.PrintWriter;

import com.htc.neweb.server.HttpRequest;
import com.htc.neweb.server.HttpResponse;
import com.htc.neweb.server.RequestHandler;

public class HomeRequestHandler implements RequestHandler {

	@Override
	public void doGet(HttpRequest req, HttpResponse resp) {
		PrintWriter writer = null;

		try {
			writer = resp.getWriter();
			writer.println("<h1>Hello ±z¦n</h1>");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}

}

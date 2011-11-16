package com.htc.neweb.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class TinyWebServer implements Runnable {

	private Thread mSrvThread = null;
	private boolean mActive = false;
	private ServerSocket mSrvSocket = null;
	private int mPort = 0;

	private LinkedList<UriHandler> handlers = new LinkedList<UriHandler>();

	public TinyWebServer(int port) {
		super();
		mPort = port;
	}

	public void regiesterHandler(String path, RequestHandler handler) {
		Pattern p = Pattern.compile("^" + path + "$");
		regiesterHandler(p, handler);
	}

	public void regiesterHandler(Pattern pattern, RequestHandler handler) {
		UriHandler uriHandler = new UriHandler();
		uriHandler.pattern = pattern;
		uriHandler.handler = handler;
		handlers.add(uriHandler);
	}

	public void start() {
		if (!mActive) {
			mSrvThread = new Thread(this);
			mSrvThread.start();
		}
	}

	public void stop() {
		if (mActive) {
			mActive = false;
			mSrvThread.interrupt();
		}
	}

	@Override
	public void run() {
		mActive = true;
		try {
			mSrvSocket = new ServerSocket(mPort);
		} catch (IOException e1) {
			e1.printStackTrace();

			// Stop running
			return;
		}

		try {
			while (mActive) {
				Socket mSocket = mSrvSocket.accept();
				(new ConnectionHandler(this, mSocket)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				mSrvSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	synchronized RequestHandler getHandler(String path) {
		String uri = path;
		
		if(uri.indexOf('?') > 0) {
			uri = uri.substring(0, uri.indexOf('?'));
		}
		Log.d("AAAA", uri);
		
		for (UriHandler uriHandler : handlers) {
			Matcher m = uriHandler.pattern.matcher(uri);
			if (m.find()) {
				return uriHandler.handler;
			}
		}

		return null;
	}

	static class UriHandler {
		Pattern pattern;
		RequestHandler handler;
	}

	static class ConnectionHandler implements Runnable {
		Thread mConnThread;
		Socket mSocket = null;
		TinyWebServer mServer;

		ConnectionHandler(TinyWebServer server, Socket socket) {
			super();
			mSocket = socket;
			mServer = server;

			mConnThread = new Thread(this);
		}

		public void start() {
			mConnThread.start();
		}

		@Override
		public void run() {
			try {
				InputStream in = mSocket.getInputStream();
				OutputStream out = mSocket.getOutputStream();

				HttpRequest req = HttpRequest.parseRequest(in);
				HttpResponse resp = new HttpResponse(out);
				resp.setProtocol(req.getProtocol());

				RequestHandler handler = mServer.getHandler(req.getUri());

				if (handler != null) {
					handler.doGet(req, resp);

				} else {
					resp.setStatus(404);
					PrintWriter p = resp.getWriter();

					p.println("<h1>cannot find page for " + req.getUri()
							+ "</h1>");
				}
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

			}
		}

	}
}

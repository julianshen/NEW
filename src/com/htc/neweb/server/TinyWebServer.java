package com.htc.neweb.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import android.util.Log;

public class TinyWebServer implements Runnable {

	private Thread mSrvThread = null;
	private boolean mActive = false;
	private ServerSocket mSrvSocket = null;
	private int mPort = 0;
	
	private HashMap<String, RequestHandler> handlers = new HashMap<String, RequestHandler>();
	
	public TinyWebServer(int port) {
		super();
		mPort = port;
	}
	
	public void regiesterHandler(String path, RequestHandler handler) {
		handlers.put(path, handler);
	}

	public void start() {
		if(!mActive) {
			mSrvThread = new Thread(this);
			mSrvThread.start();
		}
	}
	
	public void stop() {
		if(mActive) {
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
			
			//Stop running
			return;
		}
		
		try {
			while(mActive) {
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
				ByteArrayOutputStream headerOut = new ByteArrayOutputStream();
				PrintWriter writer = new PrintWriter(headerOut);
				
				HttpRequest req = HttpRequest.parseRequest(in);
				
				RequestHandler handler = mServer.handlers.get(req.getUri());
				
				if(handler != null) {
					HttpResponse resp = handler.process(req);
					
					writer.println(req.getProtocol() + " " + resp.status_code + " "+ resp.status_text);
					writer.println("Content-type: " + resp.content_type);
					writer.print("\r\n");
					writer.flush();
					
					
					out.write(headerOut.toByteArray());
					out.write(resp.content);
				} else {
					writer.println(req.getProtocol()+ " 404 NOT FOUND");
					writer.print("\r\n");
					writer.print("<h1>Not found</h1>");
					writer.flush();
					
					out.write(headerOut.toByteArray());
				}
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}

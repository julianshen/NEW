package com.htc.neweb.server.reqhandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLConnection;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.htc.neweb.server.HttpRequest;
import com.htc.neweb.server.HttpResponse;
import com.htc.neweb.server.RequestHandler;

public class AssetFilesHandler implements RequestHandler {

	String mUriPrefix = null;
	AssetManager assets = null;
	
	public AssetFilesHandler(Context context, String uriPrefix) {
		super();
		mUriPrefix = uriPrefix;
		
		assets = context.getAssets();
	}
	
	public static String getContentType(String uri) {
		String type = URLConnection.guessContentTypeFromName(uri);
		
		if(type == null) {
			if(uri.endsWith(".js")) {
				type = "application/javascript";
			}
		}
		
		return type;
	}
	
	@Override
	public void doGet(HttpRequest req, HttpResponse resp) {
		String uri = req.getUri();
		String path = uri.substring(uri.indexOf(mUriPrefix) + mUriPrefix.length() );
		String mimeType = getContentType(uri);
		
		if(mimeType!=null) {
			resp.setContentType(mimeType);
		}
		
		String[] list;
		try {
			list = assets.list("/");
			for(String s:list) {
				Log.d("AAAA", "ss:" + s);
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		PrintWriter writer;
		try {
			Log.d("AAAA", "open path:"+path);
			BufferedInputStream in = new BufferedInputStream(assets.open(path));
			OutputStream out = resp.getOutputStream();
			
			byte[] buf = new byte[2048];
			int cnt = 0;
			while((cnt = in.read(buf)) !=-1) { 
				out.write(buf, 0, cnt);
			}
			
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			resp.setContentType("text/html");
			resp.setStatus(404);
			try {
				writer = resp.getWriter();
				writer.println("PAGE NOT FOUND");
				writer.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}

}

package com.htc.neweb;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.regex.Pattern;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.htc.neweb.server.TinyWebServer;
import com.htc.neweb.server.reqhandler.AssetFilesHandler;
import com.htc.neweb.server.reqhandler.HomeRequestHandler;
import com.htc.neweb.server.reqhandler.JsEchoHandler;
import com.htc.neweb.server.reqhandler.VoiceHandler;

public class NEWService extends Service {
	TinyWebServer mTinyServer = null;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		mTinyServer.stop();
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		mTinyServer = new TinyWebServer(6666);
        mTinyServer.regiesterHandler("/", new HomeRequestHandler());
        mTinyServer.regiesterHandler("/voice", new VoiceHandler(this));
        mTinyServer.regiesterHandler("/echo", new JsEchoHandler());
        String filePrefix = "/web/";
        mTinyServer.regiesterHandler(Pattern.compile(filePrefix + ".+"), new AssetFilesHandler(this, filePrefix));
        
        Enumeration<NetworkInterface> e;
		try {
			e = NetworkInterface.getNetworkInterfaces();
			String a = "";
	        while(e.hasMoreElements()) {
	        	Enumeration<InetAddress> e2 = e.nextElement().getInetAddresses();
	        	while(e2.hasMoreElements()) {
	        		a+=e2.nextElement().toString()+"/";
	        	}
	        }
			final String addr = a;
			Log.d("AAAA", addr);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

        Log.d("AAAA", "" + Charset.defaultCharset().name());
		
        mTinyServer.start();
	}

}

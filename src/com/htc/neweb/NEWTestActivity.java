package com.htc.neweb;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.htc.neweb.server.TinyWebServer;
import com.htc.neweb.server.reqhandler.HomeRequestHandler;
import com.htc.neweb.server.reqhandler.VoiceHandler;

public class NEWTestActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TinyWebServer w = new TinyWebServer(6110);
        w.regiesterHandler("/", new HomeRequestHandler());
        w.regiesterHandler("/voice", new VoiceHandler(this));
        
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        
		
        w.start();
    }
}
package com.htc.neweb.server.reqhandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.util.Log;

import com.htc.neweb.ResultReceiver;
import com.htc.neweb.server.HttpRequest;
import com.htc.neweb.server.HttpResponse;
import com.htc.neweb.server.RequestHandler;

public class VoiceHandler implements RequestHandler {

	Context mContext;
	ResultReceiver mResultReceiver = null;
	private Object lock = new Object();
	String result = "test1";

	public VoiceHandler(Context cx) {
		super();
		mContext = cx;
		mResultReceiver = new ResultReceiver();
	}

	@Override
	public void doGet(HttpRequest req, HttpResponse resp) {
		String uri = req.getUri();
		
		HashMap<String, String> params = new HashMap<String, String>();
		if(uri.indexOf('?') > 0) {
			String queryString = uri.substring(uri.indexOf('?') + 1);
			
			//parse queryString
			String[] parts = queryString.split("&");
			if(parts!=null) {
				for(String p:parts) {
					String[] namevaluepair = p.split("=");
					
					if(namevaluepair!=null && namevaluepair.length>0) {
						String name = namevaluepair[0];
						String value = namevaluepair.length > 1 ? namevaluepair[1]:"";
						params.put(name, value);
					}
				}
			}
		}
		
		
		Intent recoIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		recoIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
				.getPackage().getName());
		
		//mContext.getPackageName()

		recoIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				"Speech recognition demo");

		recoIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

		recoIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

		
		ResultReceiver.ResultManager.getManager().startActivityForResult(mContext, recoIntent, new ResultCallback());

		try {
			synchronized(lock) {
				lock.wait();
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		
		
		PrintWriter writer = null;
		try {
			resp.setContentType("application/json");
			String callback = params.get("callback");
			
			String output = "{\"message\": \"" +result+ "\"}";
			
			if(callback!=null) {
				//JSONP
				output = callback + "(" + output + ");";
			}
			writer = resp.getWriter();
			writer.print(output);
			Log.d("AAAA", "Result:"+result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
		
	}

	
	class ResultCallback implements ResultReceiver.Callback {

		
		@Override
		public void onResult(Intent data) {
			Log.d("AAAA", "receive broadcast");
			synchronized(lock) {
				lock.notify();
			}
			ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
			Log.d("AAAA", "notified : " + matches.size());
			
			if(matches.size() > 0) {
				result = matches.get(0);
			}
		}
		
	}
}

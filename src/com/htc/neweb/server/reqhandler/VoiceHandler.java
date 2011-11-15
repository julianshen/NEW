package com.htc.neweb.server.reqhandler;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
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
	private BroadcastReceiver mReceiver;
	private Object lock = new Object();
	String result = "test1";

	public VoiceHandler(Context cx) {
		super();
		mContext = cx;
		mResultReceiver = new ResultReceiver();
	}

	@Override
	public HttpResponse process(HttpRequest req) {
		HttpResponse resp = new HttpResponse();
		
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
		
		resp.content = result.getBytes();
		try {
			mContext.unregisterReceiver(mResultReceiver);
		} catch(Exception e) {
			e.printStackTrace();
		}
 		return resp;
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
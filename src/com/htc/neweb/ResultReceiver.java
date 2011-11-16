package com.htc.neweb;

import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ResultReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
	    long callbackid = intent.getExtras().getLong("callbackid");
	    Intent data = (Intent) intent.getExtras().get("data");
	    
	    ResultManager.getManager().onResult(callbackid, data);
	}

	public static interface Callback {
		public void onResult(Intent intent) ;
	}
	
	public static class ResultManager {
		public static ResultManager manager = null;
		HashMap<Long, Callback> waitQueue = new HashMap<Long, Callback>();
		ResultManager() {
			super();
		}
		
		public static final ResultManager getManager() {
			if(manager == null) {
				manager = new ResultManager();
			}
			
			return manager;
		}
		
		public void startActivityForResult(Context context, Intent targetIntent, Callback resultCallback) {
			Intent intent = new Intent();
			intent.setClass(context.getApplicationContext(),
			ResultWrapperActivity.class);
			
			intent.putExtra("target", targetIntent);
			
			long id = System.currentTimeMillis();
			waitQueue.put(id, resultCallback);
			
			Intent resultIntent = new Intent("com.htc.new.result");
			resultIntent.putExtra("callbackid", id);
			intent.putExtra("result", resultIntent);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.getApplicationContext().startActivity(intent);
		}
		
		public void onResult(long callbackid, Intent intent) {
			Callback callback = waitQueue.remove(callbackid);
			
			if(callback != null) {
				callback.onResult(intent);
			}
		}
	}
}

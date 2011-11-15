package com.htc.neweb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ResultWrapperActivity extends Activity {
	
	Intent mResult = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    Log.d("AAAA", "ssss1111");
	    Intent intent = getIntent();
	    
	    Intent target = (Intent) intent.getExtras().get("target");
	    mResult = (Intent) intent.getExtras().get("result");
	    startActivityForResult(target, 0);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		this.setIntent(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mResult.putExtra("data", data);
		Log.d("AAAA", "send broadcast : " + mResult.getAction());
		this.sendBroadcast(mResult, null);
		finish();
	}

}

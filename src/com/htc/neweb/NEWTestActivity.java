package com.htc.neweb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class NEWTestActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Intent intent = new Intent("com.htc.tinyweb.service");
        
        startService(intent);
    }
}
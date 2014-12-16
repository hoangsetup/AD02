package com.longnd.tracuudiemthi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
	public static int SPLASH_TIME_OUT = 700;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SplashScreen.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		}, SPLASH_TIME_OUT);
	} 
}

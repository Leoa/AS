package com.sugardefynery.animeconvention.scheduler;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {

	
	private long splashDelay=5000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		int removeSplash = PreferenceConnector.readInteger(this,
				PreferenceConnector.REMOVESPLASH, 0);
		
		
		if (removeSplash != 1) {
		setContentView(R.layout.splash);
		
		
		TimerTask splashTimer=new TimerTask(){
			
			public void run(){
				int removeSplash = 1;

				PreferenceConnector.writeInteger(Splash.this,
						PreferenceConnector.REMOVESPLASH, removeSplash);
											
				
				Intent intent = new Intent(Splash.this, TabBarExample.class);

					startActivity(intent);
					
					finish();
					
				
				
			}
			
			
		};
		
		Timer timer= new Timer();
		timer.schedule(splashTimer, splashDelay);
		
		}else{
			

			Intent intent = new Intent(Splash.this, TabBarExample.class);

				startActivity(intent);
			
		}
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}

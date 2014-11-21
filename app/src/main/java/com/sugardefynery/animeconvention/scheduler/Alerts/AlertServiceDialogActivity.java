package com.sugardefynery.animeconvention.scheduler.Alerts;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;



import com.sugardefynery.animeconvention.scheduler.PreferenceConnector;
import com.sugardefynery.animeconvention.scheduler.R;
import com.sugardefynery.animeconvention.scheduler.sqllite.DatabaseSqlite;

public class AlertServiceDialogActivity extends Activity {
	MediaPlayer player;
	
	
	Bundle bundle;
	Vibrator vibrate;
	DatabaseSqlite entry = new DatabaseSqlite(AlertServiceDialogActivity.this);
	int vibrateState;
	int soundState;
	AlertDialog.Builder builder;
	ArrayList<String> cl= new ArrayList<String>();
	int idInteger;
	int alertDismissed = 1;
	String name;
	SoundPool sp;
	int alarmSound=0;
	AudioManager  mAudioManager;
	int i;
	AlertDialog alert;
	 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	      
		
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		
		cl =getIntent().getExtras().getStringArrayList("alertNames");
		
//
//		AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
//		int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//		int soundVolume= 75;		
//		final float volume = (float) (1 - (Math.log(maxVolume - soundVolume) / Math.log(maxVolume)));
//		player.setVolume(volume, volume);

		
		
		for ( i=0;i<cl.size();i++){

		name =cl.get(i);
		
		
		sound();
		
		vibrate();

		String textForDisplay = "Alert time passed";
		
		builder = new AlertDialog.Builder(AlertServiceDialogActivity.this);
		builder.setMessage(textForDisplay)
				.setTitle(name)
				.setCancelable(false)
				.setNegativeButton("Dismiss",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
						
								
								 
							     entry.open();
						    		alertDismissed=1;
						    		entry.updateAlertDismissedState(id, alertDismissed);
						    		
						    		entry.close();
									
								
								soundState = PreferenceConnector.readInteger(
										AlertServiceDialogActivity.this,
										PreferenceConnector.SOUND_ON_OFF, 0);
								if (soundState == 0) {

									player.pause();
								

								}

								vibrateState = PreferenceConnector.readInteger(
										AlertServiceDialogActivity.this,
										PreferenceConnector.VIBRATE_ON_OFF, 0);

								if (vibrateState == 0) {

									vibrate.cancel();
								}
				
								if(i==cl.size()-1){
									
									
									Intent intent = new Intent(Intent.ACTION_MAIN);
									intent.addCategory(Intent.CATEGORY_HOME);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									alert.cancel();
									alert.dismiss();
									
									finish();
									startActivity(intent);
									
									
								}

							}
							
						});
		alert = builder.create();
		alert.show();
		
    } 
		
		
//		
//		 Thread t = new Thread(new Runnable() {
//			    @Override
//			    public void run() {
//			          handler.sendEmptyMessage(0);
//			    } 
//			  }); //thread
//			t.start();
//		}
		

		
		
		}

	
	

	@Override
	protected  void onDestroy() {
		super.onDestroy();
		player.pause();
		player.release();

	}

	public void sound() {
		

		soundState = PreferenceConnector.readInteger(AlertServiceDialogActivity.this,
				PreferenceConnector.SOUND_ON_OFF, 0);
		if (soundState == 0) {

			player = MediaPlayer.create(this, R.raw.alarm_clock); 
			player.start();

		}

	}

	

	private void vibrate() {

		vibrateState = PreferenceConnector
				.readInteger(AlertServiceDialogActivity.this,
						PreferenceConnector.VIBRATE_ON_OFF, 0);

		if (vibrateState == 0) {
			// Get instance of Vibrator from current Context
			vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

			// Start immediately
			// Vibrate  for 200 milliseconds
			// Sleep for 500 milliseconds
			long[] pattern = { 0, 200, 500 };

			// The "0" means to repeat the pattern starting at the beginning
			// CUIDADO: If you start at the wrong index (e.g., 1) then your
			// pattern will be off --
			// You will vibrate for your pause times and pause for your vibrate
			// times !
			vibrate.vibrate(pattern, 0);
		} else {
		}

	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	}


}
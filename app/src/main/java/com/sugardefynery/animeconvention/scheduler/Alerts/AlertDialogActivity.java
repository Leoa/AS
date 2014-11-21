package com.sugardefynery.animeconvention.scheduler.Alerts;

import java.io.IOException;

import com.sugardefynery.animeconvention.scheduler.DemoActivity;
import com.sugardefynery.animeconvention.scheduler.PreferenceConnector;
import com.sugardefynery.animeconvention.scheduler.R;
import com.sugardefynery.animeconvention.scheduler.TabBarExample;
import com.sugardefynery.animeconvention.scheduler.EventList.UpdateLocalDatabaseService;
import com.sugardefynery.animeconvention.scheduler.sqllite.DatabaseSqlite;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.ContextThemeWrapper;

public class AlertDialogActivity extends Activity {

	MediaPlayer player = new MediaPlayer();
	Bundle bundle;
	Vibrator vibrate;
	DatabaseSqlite entry = new DatabaseSqlite(AlertDialogActivity.this);
	int vibrateState;
	int soundState;
	 AlertDialog.Builder builder;
	Intent intent;
	int idInteger;
	int alertDismissed = 1;
	AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		bundle = getIntent().getExtras();
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		
		String name = bundle.getString("name");
		String id = bundle.getString("id");

		idInteger = Integer.parseInt(id);		
		
		intent = new Intent(AlertDialogActivity.this, TabBarExample.class);
		sound();
		vibrate();

		entry.open();
		alertDismissed=1;
		entry.updateAlertDismissedState(idInteger, alertDismissed);
		int alertState=entry.getAlertState(idInteger);
		String text = entry.getMinutesForOneShot(idInteger);
		entry.close();

		if(alertState==0){
		String textForDisplay = "In " + text + " Minutes";

		
		//run?
		
		builder = new AlertDialog.Builder( AlertDialogActivity.this);
		builder.setMessage(textForDisplay)
				.setTitle(name)
				.setCancelable(false)
				.setNegativeButton("Dismiss",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
						
								soundState = PreferenceConnector.readInteger(
										AlertDialogActivity.this,
										PreferenceConnector.SOUND_ON_OFF, 0);
								if (soundState == 0) {

									player.pause();
									player.release();

								}

								vibrateState = PreferenceConnector.readInteger(
										AlertDialogActivity.this,
										PreferenceConnector.VIBRATE_ON_OFF, 0);

								if (vibrateState == 0) {

									vibrate.cancel();
								}
									
								alert.cancel();
								alert.dismiss();
								finish();
								//onDestroy();
								//startActivity(intent);
							}
						});
		alert = builder.create();
		alert.show();
		
		}
		else{
			
			finish();
			
		}
    } 
		
		
//		 Thread t = new Thread(new Runnable() {
//			    @Override
//			    public void run() {
//			    	
			    	
			    	
//
//					AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
//					int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//					int soundVolume= 75;		
//					final float volume = (float) (1 - (Math.log(maxVolume - soundVolume) / Math.log(maxVolume)));
//					player.setVolume(volume, volume);

			    	
			    	
			    	
//			          handler.sendEmptyMessage(0);
//			    } 
//			  }); //thread
//			t.start();

		
//		Task alertUser= new Task();
//		alertUser .execute();
	
	
	private Handler handler= new Handler() {
        public void handleMessage(Message msg){

//        	Intent intent = new Intent(Intent.ACTION_MAIN);
//			intent.addCategory(Intent.CATEGORY_HOME);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			
//			startActivity(intent);
        	
        	
        	String name = bundle.getString("name");
    		String id = bundle.getString("id");

    		idInteger = Integer.parseInt(id);		
    		
    		intent = new Intent(AlertDialogActivity.this, TabBarExample.class);
    		sound();
    		vibrate();

    		entry.open();
    		alertDismissed=1;
    		entry.updateAlertDismissedState(idInteger, alertDismissed);
    		String text = entry.getMinutesForOneShot(idInteger);
    		entry.close();

    		String textForDisplay = "In " + text + " Minutes";

    		
    		//run?
    		
    		builder = new AlertDialog.Builder( AlertDialogActivity.this);
    		builder.setMessage(textForDisplay)
    				.setTitle(name)
    				.setCancelable(false)
    				.setNegativeButton("Dismiss",
    						new DialogInterface.OnClickListener() {
    							public void onClick(DialogInterface dialog, int id) {
    						
    								soundState = PreferenceConnector.readInteger(
    										AlertDialogActivity.this,
    										PreferenceConnector.SOUND_ON_OFF, 0);
    								if (soundState == 0) {

    									player.pause();
    									player.release();

    								}

    								vibrateState = PreferenceConnector.readInteger(
    										AlertDialogActivity.this,
    										PreferenceConnector.VIBRATE_ON_OFF, 0);

    								if (vibrateState == 0) {

    									vibrate.cancel();
    								}
    									
    								finish();
    								//onDestroy();
    								//startActivity(intent);
    							}
    						});
    		AlertDialog alert = builder.create();
			alert.show();
			
        } 
    }; 

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	
	 private static class Task extends AsyncTask<Void, Void, Void> {
		

		@Override
		protected void onPreExecute() {
			super.onPreExecute();


		}

		
		@Override
		protected Void doInBackground(Void... unused) {

			
			return null;
			
		}
		
		@Override
		protected void onPostExecute(Void unused) {
			
			
		}
		
		
		
	}
	
	private void sound() {

		soundState = PreferenceConnector.readInteger(AlertDialogActivity.this,
				PreferenceConnector.SOUND_ON_OFF, 0);
		if (soundState == 0) {

			// play  sound

			AssetFileDescriptor afd = this.getResources().openRawResourceFd(
					R.raw.alarm_clock);
			try {
				player.setDataSource(afd.getFileDescriptor(),
						afd.getStartOffset(), afd.getLength());
				afd.close();
				player.setAudioStreamType(AudioManager.STREAM_ALARM);
				player.setLooping(true);
				player.prepare();
				player.start();

			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
		}

	}

	private void vibrate() {

		vibrateState = PreferenceConnector
				.readInteger(AlertDialogActivity.this,
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
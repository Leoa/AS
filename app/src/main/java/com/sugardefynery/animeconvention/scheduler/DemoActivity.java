package com.sugardefynery.animeconvention.scheduler;

import static com.sugardefynery.animeconvention.scheduler.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.sugardefynery.animeconvention.scheduler.CommonUtilities.EXTRA_MESSAGE;
import static com.sugardefynery.animeconvention.scheduler.CommonUtilities.SENDER_ID;
import static com.sugardefynery.animeconvention.scheduler.CommonUtilities.SERVER_URL;

import com.google.android.gcm.GCMRegistrar;
import com.sugardefynery.animeconvention.scheduler.R;
import com.sugardefynery.animeconvention.scheduler.Alerts.AlertView;
import com.sugardefynery.animeconvention.scheduler.EventList.DataView;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Main UI for the demo app.
 */
public class DemoActivity extends Activity implements OnClickListener {
	Intent intent;
	String register;
	String msg;
	Button btnRegister;
	Button btnUnregister;
	Button btnBack;
	TextView mDisplay;

	AsyncTask<Void, Void, Void> mRegisterTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		checkNotNull(SERVER_URL, "SERVER_URL");
		checkNotNull(SENDER_ID, "SENDER_ID");
		

		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);
		btnUnregister = (Button) findViewById(R.id.btnUnRegister);
		btnUnregister.setOnClickListener(this);

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		mDisplay = (TextView) findViewById(R.id.display);

		GCMRegistrar.checkDevice(DemoActivity.this.getApplicationContext());
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(DemoActivity.this.getApplicationContext());

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));
		final String regId = GCMRegistrar.getRegistrationId(DemoActivity.this.getApplicationContext());
		Log.v("regid is :", regId);

		
		
		if (regId.equals("")) {
			
			

			int unregisterStatus = 0;

			 unregisterStatus = PreferenceConnector.readInteger(this,
					PreferenceConnector.REGISTERSTATUS, unregisterStatus);

			if (unregisterStatus != 1) {
				
			GCMRegistrar.register(DemoActivity.this.getApplicationContext(), SENDER_ID);
			
			
			}
			else{
				
				mDisplay.append(" \n");
				
				mDisplay.append("Device is unregistered");
				mDisplay.append(" \n");
			}
			
		} else {
			// Device is already registered on GCM, needs to check if it is
			// registered on our server as well.
			if (GCMRegistrar.isRegisteredOnServer(DemoActivity.this.getApplicationContext())) {

				// Skips registration.
				mDisplay.append(getString(R.string.already_registered));
			} else {

				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;

				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						boolean registered =

						ServerUtilities.register(DemoActivity.this.getApplicationContext(), regId);
						// At this point all attempts to register with the
						// server failed, so we need to unregister the device
						// from GCM - the app will try to register again when
						// it  is restarted. Note that GCM will send an
						// unregistered callback upon completion, but
						// GCMIntentService.onUnregistered() will ignore it.
						if (!registered) {
							GCMRegistrar.unregister(context);
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}
		
		


		if (getIntent().getExtras().getString("sampleData") != null) {
			intent = getIntent();

			msg = intent.getStringExtra("sampleData");

			String s = "demoActivity";
			getLocations(s);

		} else {
		}

		if (getIntent().getExtras().getString("register") != null) {
			intent = getIntent();
			register = intent.getStringExtra("register");

		} else {

			register = "NoRegister";

		}

	}

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}


		if (mHandleMessageReceiver != null) {
			try {
				unregisterReceiver(mHandleMessageReceiver);
			} catch (IllegalArgumentException e) {
				// Do nothing
			}

		}


		GCMRegistrar.onDestroy(DemoActivity.this.getApplicationContext());
		super.onDestroy();
		
		mDisplay.setText("");
	}

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(getString(R.string.error_config,
					name));
		}
	}

	private BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			
			mDisplay.append(" \n");
			mDisplay.append(newMessage);
			mDisplay.append(" \n");
			

		//	System.out.println(newMessage);

		}
	};

	public void getLocations(String s) {
		Intent intent = getIntent();
		intent.putExtra("returnedData", s);
		setResult(RESULT_OK, intent);
		
		finish();

	}

	@Override
	public void onClick(View v) {
		
		int registerStatus;

		if (v.getId() == R.id.btnRegister) {

			GCMRegistrar.register(DemoActivity.this.getApplicationContext(),

			SENDER_ID);
			
			
			 registerStatus = 0;

			PreferenceConnector.writeInteger(DemoActivity.this,
					PreferenceConnector.REGISTERSTATUS, registerStatus);

		}

		if (v.getId() == R.id.btnUnRegister) {

			GCMRegistrar.unregister(DemoActivity.this.getApplicationContext());

			 registerStatus = 1;

			PreferenceConnector.writeInteger(DemoActivity.this,
					PreferenceConnector.REGISTERSTATUS, registerStatus);
			
			
			
		}
		if (v.getId() == R.id.btnBack) {

			Intent back = new Intent(DemoActivity.this.getApplicationContext(),

			TabBarExample.class);

			startActivity(back);

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent(
					DemoActivity.this.getApplicationContext(),
					TabBarExample.class);
			startActivity(intent);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	

	
	
	
}
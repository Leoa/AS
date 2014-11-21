package com.sugardefynery.animeconvention.scheduler;

import static com.sugardefynery.animeconvention.scheduler.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.sugardefynery.animeconvention.scheduler.CommonUtilities.DISPLAY_MESSAGE_ERROR;
import static com.sugardefynery.animeconvention.scheduler.CommonUtilities.ERROR_MESSAGE;
import static com.sugardefynery.animeconvention.scheduler.CommonUtilities.EXTRA_MESSAGE;
import static com.sugardefynery.animeconvention.scheduler.CommonUtilities.SENDER_ID;
import static com.sugardefynery.animeconvention.scheduler.CommonUtilities.SERVER_URL;

import java.io.File;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;

import com.sugardefynery.animeconvention.scheduler.EventsView;
import com.sugardefynery.animeconvention.scheduler.R;
import com.sugardefynery.animeconvention.scheduler.Alerts.AlertDialogActivity;
import com.sugardefynery.animeconvention.scheduler.Alerts.AlertSettings;
import com.sugardefynery.animeconvention.scheduler.Alerts.AlertView;
import com.sugardefynery.animeconvention.scheduler.Alerts.AlertsDetails;
import com.sugardefynery.animeconvention.scheduler.EventList.DataView;
import com.sugardefynery.animeconvention.scheduler.Notifications.NotesView;

public class TabBarExample extends TabActivity {
	/** Called when the activity is first created. */

	String regId;
	TabHost tabHost;
	String tempValue = "x";
	AlertDialog alert;
	AlertDialog.Builder builder;

	// Custom intent action used to open secondary activity
	private static final String CUSTOM_INTENT_ACTION_ENTER_TEXT = "ro.ovidiuconeac.startactivityforresult.ENTER_TEXT";

	// Request code
	public static final int CUSTOM_REQUEST_CODE_ENTER_TEXT = 666;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tab);
		checkNotNull(SERVER_URL, "SERVER_URL");
		checkNotNull(SENDER_ID, "SENDER_ID");

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		regId = GCMRegistrar.getRegistrationId(this);
		System.out.println(regId);

		if (regId.equals("")) {
			// Automatically registers application on startup.

			// Here we can controll unregistration

			int unregisterStatus = 0;

			unregisterStatus = PreferenceConnector.readInteger(this,
					PreferenceConnector.REGISTERSTATUS, unregisterStatus);

			if (unregisterStatus != 1) {

				GCMRegistrar.register(
						TabBarExample.this.getApplicationContext(), SENDER_ID);

			}
		} else {
			// Device is already registered on GCM, needs to check if it is
			// registered on our server as well.
			if (GCMRegistrar.isRegisteredOnServer(TabBarExample.this
					.getApplicationContext())) {

			}
		}

		// /** TabHost will have Tabs */
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		//
		// /**
		// * TabSpec used to create a new tab. By using TabSpec only we can able
		// * to setContent to the tab. By using TabSpec setIndicator() we can
		// set
		// * name to tab.
		// */
		//

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ERROR));

		Intent i = new Intent(TabBarExample.this.getApplicationContext(),
				DataView.class);
		setupTab(new TextView(TabBarExample.this.getApplicationContext()),
				"Events", i);

		i = new Intent(TabBarExample.this.getApplicationContext(),
				ScheduleView.class);
		setupTab(new TextView(TabBarExample.this.getApplicationContext()),
				"Schedule", i);

		i = new Intent(TabBarExample.this.getApplicationContext(),
				ConventionView.class);
		setupTab(new TextView(TabBarExample.this.getApplicationContext()),
				"Layout", i);

		i = new Intent(TabBarExample.this.getApplicationContext(),
				AlertView.class);
		setupTab(new TextView(TabBarExample.this.getApplicationContext()),
				"Alerts", i);

		i = new Intent(TabBarExample.this.getApplicationContext(),
				NotesView.class);
		setupTab(new TextView(TabBarExample.this.getApplicationContext()),
				"Message", i);
		
		Bundle extras = getIntent().getExtras(); 
		String userName = null;
System.out.println("user name is "+userName);
		if (extras != null) {
		    userName = extras.getString("name");
		    
		    tabHost.setCurrentTab(1); 
		    
		    System.out.println("user name is "+userName);
		    // and get whatever type user account id is
		}



		//
		// Intent intent = new Intent(this, DemoActivity.class);
		// intent.putExtra("sampleData", "sampleData");
		// startActivityForResult(intent, 1);

		// pop up control
		// Registration and popup 0 is off, 1 is on

		int off = 0;
		PreferenceConnector.writeInteger(this,
				PreferenceConnector.REGISTRATION, off);

		int removePopup = PreferenceConnector.readInteger(this,
				PreferenceConnector.REMOVEPOPUP, off);

		if (removePopup != 1) {

			PopupWindowAsync popWindowTask = new PopupWindowAsync();

			String th = "threading";

			popWindowTask.execute(th);
		}

	}

	private class PopupWindowAsync extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(2000);
				if (isCancelled()) {

					this.cancel(true);
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return "threading";
		}

		@Override
		protected void onPostExecute(String result) {

			LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View popupView = layoutInflater.inflate(R.layout.popup, null);
			int screenWidthpop = getWindowManager().getDefaultDisplay()
					.getWidth() - 100;
			int screenHeightpop = getWindowManager().getDefaultDisplay()
					.getHeight() - 200;
			int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
			int screenHeight = getWindowManager().getDefaultDisplay()
					.getHeight();

			final PopupWindow popupWindow = new PopupWindow(popupView,
					screenWidthpop, screenHeightpop, true);
			//
			// int x = screenWidth / 2 - popupWindow.getWidth() / 2;
			// int y = (screenHeight / 2 - popupWindow.getHeight() / 2) - 200;

			popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

			// GCMRegistrar.register(TabBarExample.this, SENDER_ID);

			Button btnDismiss = (Button) popupView
					.findViewById(R.id.btnDismiss);
			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated  method stub
					int removePopup = 1;

					PreferenceConnector.writeInteger(TabBarExample.this,
							PreferenceConnector.REMOVEPOPUP, removePopup);

					PreferenceConnector.writeInteger(TabBarExample.this,
							PreferenceConnector.VIBRATE_ON_OFF, 0);
					PreferenceConnector.writeInteger(TabBarExample.this,
							PreferenceConnector.SOUND_ON_OFF, 0);
					popupWindow.dismiss();

				}
			});

		}

	}// end popWindowTask

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int off;
		int removePopup = 0;

		switch (item.getItemId()) {
		/*
		 * Typically, an application regi sters automatically, so options below
		 * are disabled . Uncomment them if you want to manually register or
		 * unregister the device (you will also need to uncomment the equivalent
		 * options on options_menu.xml).
		 */

		case R.id.options_alarmSettings:

			Intent intent = new Intent(
					TabBarExample.this.getApplicationContext(),
					AlertSettings.class);
			startActivity(intent);
			return true;
		case R.id.options_register:

			Intent register = new Intent(
					TabBarExample.this.getApplicationContext(),
					DemoActivity.class);
			register.putExtra("register", "register");
			startActivity(register);

			removePopup = 1;
			PreferenceConnector.writeInteger(this,
					PreferenceConnector.REMOVEPOPUP, removePopup);

			return true;
			
		 case R.id.options_refresh:
	        	Intent i = new Intent(TabBarExample.this
						.getApplicationContext(), TabBarExample.class);
				startActivity(i);
	        	
	        	return true;
		 case R.id.options_tutorial:
			 
			 Intent j = new Intent(TabBarExample.this
						.getApplicationContext(), Tutorial.class);
				startActivity(j);
			 
			 return true;
 case R.id.options_about:
			 
			 Intent o = new Intent(TabBarExample.this
						.getApplicationContext(), About.class);
				startActivity(o);
			 
			 return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(getString(R.string.error_config,
					name));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 1) {
			String msg = data.getStringExtra("returnedData");
			Log.v("locations from intent", msg);
		}

	}

	private void setupTab(final View view, final String tag, Intent intent) {
		View tabview = createTabView(tabHost.getContext(), tag);
		TabSpec tabSpec = tabHost.newTabSpec(tag).setIndicator(tabview)
				.setContent(intent);
		tabHost.addTab(tabSpec);
	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		// tv.setTypeface(tf);
		return view;
	}

	private BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			String newMessage = intent.getExtras().getString(ERROR_MESSAGE);

			showErrorToUser(newMessage);
			// Toast.makeText(TabBarExample.this, newMessage,
			// Toast.LENGTH_LONG).show();

		}
	};

	@Override
	protected void onResume() {
		super.onResume();

		// Automatically registers application on startup.
		registerReceiver(mHandleMessageReceiver,
				new IntentFilter(ERROR_MESSAGE));
	}

	public void showErrorToUser(String message) {

		String checkMsg = null;

		checkMsg = PreferenceConnector.readString(this,
				PreferenceConnector.REMOVEDIALOG, checkMsg);

		if (message.equals("") || message.equals(checkMsg)) {
		} else {

			checkMsg = message;
			PreferenceConnector.writeString(this,
					PreferenceConnector.REMOVEDIALOG, checkMsg);

			builder = new AlertDialog.Builder(TabBarExample.this);
			builder.setMessage(message)

					.setCancelable(false)
					.setNegativeButton("Dismiss",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									alert.cancel();
									alert.dismiss();

								}
							});
			alert = builder.create();
			alert.show();

		}

	}

	@Override
	protected void onDestroy() {

		if (mHandleMessageReceiver != null) {
			try {
				unregisterReceiver(mHandleMessageReceiver);
			} catch (IllegalArgumentException e) {
				// Do nothing
			}

		}

		GCMRegistrar.onDestroy(TabBarExample.this.getApplicationContext());
		super.onDestroy();

	}

	@Override
	protected void onStop() {
//		unregisterReceiver(mHandleMessageReceiver);
//		GCMRegistrar.onDestroy(TabBarExample.this.getApplicationContext());
		super.onStop();
	}
	
	@Override 
	protected void onPause() {
	    super.onPause();
//		try {
//			unregisterReceiver(mHandleMessageReceiver);
//		} catch (IllegalArgumentException e) {
//			// Do nothing
//		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent(
					TabBarExample.this.getApplicationContext(),
					TabBarExample.class);
			startActivity(intent);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}


}
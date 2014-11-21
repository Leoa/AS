package com.sugardefynery.animeconvention.scheduler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.sugardefynery.animeconvention.scheduler.R;

public class About extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		TextView tv =(TextView)findViewById(R.id.aboutApp);
		tv.setText("Anime Convention Scheduler \n\nAnime Convention Scheduler replaces paper schedule with a mobile application. This application  features maps, descriptions, and reminders to assist attendees as they navigate their day. Anime Convention Scheduler uses Google Cloud Messaging to help convention organizers to communicate directly with attendees on the latest changes to the schedule. Current application is a Prototype. The data can be customize to any convention schedule. Contact Developer for more information.\n \n\u00A9  2012-2013 Copyright \n\nSugar Defynery 2012-2013");
		
	
	// go back  to list
	Button button2 = (Button) findViewById(R.id.btnBack);
	button2.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(About.this
					.getApplicationContext(), TabBarExample.class);
			startActivity(intent);
		}
	});
}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent(
					About.this.getApplicationContext(),
					TabBarExample.class);
			startActivity(intent);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

}


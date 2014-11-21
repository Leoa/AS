package com.sugardefynery.animeconvention.scheduler;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.AdapterView.OnItemSelectedListener;
import com.sugardefynery.animeconvention.scheduler.R;


public class Tutorial extends Activity{
	TextView tv;
	ImageView iv;
	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial);
		
		 tv=(TextView)findViewById(R.id.txtdataview);
		 iv=(ImageView)findViewById(R.id.imageView1);
		 
		 tv.setText("The Events list is sorted by the start time of the events. Most of the functions of this application derive from this list.");
			iv.setImageResource(R.drawable.list);
		
		Spinner spinner = (Spinner) findViewById(R.id.spinnere);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter
				.createFromResource(this, R.array.country_arrays,
						android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					
			spinner.setAdapter(adapter);
			spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
			
			

		
		Button button2 = (Button) findViewById(R.id.btnBack2);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Tutorial.this
						.getApplicationContext(), TabBarExample.class);
				startActivity(intent);
			}
		});

	}

	
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

		int count = 0;

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id1) {
			
			
			// if alert is off  an spinner is chosen 
		
			if (count >= 1) {
				
				
				
				String text =parent.getItemAtPosition(pos).toString();
				if(text.equals("List of Events")){
					tv.setText("The Events list is sorted by the start time of the events. Most of the functions of this application derive from this list. ");
					iv.setImageResource(R.drawable.list);
				
					
				}
				
if(text.equals("Event Details")){
					
					tv.setText("From the Events Details screen, alerts can be created.");
					iv.setImageResource(R.drawable.events);
					
				}
				if(text.equals("Set Alert")){
					
					tv.setText("When the alert is set, it is located under the \"Alerts\" tab. In this section, the settings for the alert can be changed.");
					iv.setImageResource(R.drawable.alerts);
					
				}
				if(text.equals("Notifications")){
					
					tv.setText("Members receive messages from the convention administration. Also, alerts are automatically update through GCM notifications.");
					iv.setImageResource(R.drawable.notifications);
					
				}
				
			
			if(text.equals("Schedule Chart")){
					
					tv.setText("Any changes to the schedule are updated in real-time.");
					iv.setImageResource(R.drawable.chart);
					
				}
			
			
			

			}
			count++;
		}

		public void onNothingSelected(AdapterView parent) {
			
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent(
					Tutorial.this.getApplicationContext(),
					TabBarExample.class);
			startActivity(intent);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}


}


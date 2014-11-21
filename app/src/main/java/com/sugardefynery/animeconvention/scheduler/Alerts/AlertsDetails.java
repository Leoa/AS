package com.sugardefynery.animeconvention.scheduler.Alerts;

import com.sugardefynery.animeconvention.scheduler.R;
import com.sugardefynery.animeconvention.scheduler.TabBarExample;
import com.sugardefynery.animeconvention.scheduler.EventList.ConvertStdTime;
import com.sugardefynery.animeconvention.scheduler.EventList.EventDetails;
import com.sugardefynery.animeconvention.scheduler.EventList.UpdateLocalDatabaseService;
import com.sugardefynery.animeconvention.scheduler.sqllite.DatabaseSqlite;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AlertsDetails extends Activity {

	public SimpleCursorAdapter ladapter;
	Context context;
	Integer id;
	String name;
	String date;
	String startTime;
	String endTime;
	String location;
	Bundle bundle;
	String alertTime;
	int alertState;
	String eventdate;
	List<Alerts> alarmlist;
	PendingIntent pendingIntent;
	String time;
	String alertTimeDB;
	String startTimeMilitary;
	Toast mToast;
	PendingIntent sender;
	Intent intent;
	AlarmManager am;

	Button button4;
	private int intHour=0;
	private int intMin=0;
	private int intDay=0;
	private int intMonth=0;
	private int intYear=0;

	private String sAlertInMillis;
	
	private TextView mTitleDisplay;
	private TextView mDateDisplay;
	private TextView mTimeDisplay;
	private TextView mTimeEndDisplay;
	private TextView mLocationDisplay;
	private TextView mAlertTime;
	private TextView alertTitle;
	
	DatabaseSqlite entry = new DatabaseSqlite(AlertsDetails.this);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_details);

		// get detail info from bundle
		bundle = getIntent().getExtras();
		id = bundle.getInt("id");
		name = bundle.getString("eventName");
		date = bundle.getString("date");
		startTime = bundle.getString("startTime");
		endTime = bundle.getString("endTime");
		location = bundle.getString("location");
		alertTime = bundle.getString("alertTime");

		// capture our View elements
		mTitleDisplay = (TextView) findViewById(R.id.titleDisplay);
		mDateDisplay = (TextView) findViewById(R.id.dateDisplay);

		mLocationDisplay = (TextView) findViewById(R.id.locationDisplay);
		mAlertTime = (TextView) findViewById(R.id.alertDisplay);

		button4 = (Button) findViewById(R.id.btnSetAlert);
		alertTitle=(TextView)findViewById(R.id.alertText);
		
		// set alert time
		mAlertTime.setText("");
		
		// Set Title
		mTitleDisplay.setText(name);

		// set Date
		
		String eventYear = date.substring(0, 4);
		String eventMonth  = date.substring(5, 7);
		String eventDay= date.substring(8, 10);				
		String displayDate=eventYear+":"+eventMonth+":"+eventDay;
		formatUpdateAlertYearMonthDay(displayDate);
		String formatDisplayDate=""+getMonth()+"-"+getDay()+"-"+getYear();
		mDateDisplay.setText(formatDisplayDate+" at "+startTime);
		
		// set location
		mLocationDisplay.setText(location);
		
		entry.open();
		// get status of alert state 		
		alertState = entry.getAlertState(id);
		System.out.println("alertState DB "+alertState);
	
		int eventState=entry.getEventStateForService(id);
		// get alert time from the local database in GMT format
		alertTimeDB = entry.getAlertTime(id);
				
		// start time of event is needed to establish the degree of time used to create alert
		String startTimefromDB = entry.getStartTime(id);				
		entry.close();
		
		// convert utc time to military time for processing 
		ConvertStdTime startTimeConvert = new ConvertStdTime();
		startTimeMilitary = startTimeConvert.toMilitaryString(startTimefromDB);
				
		// event date of event is needed to establish the degree of time before event
		eventdate = eventMonth + "/" + eventDay + "/" + eventYear;

		// check to see alert is on or off and display state to user
		

		
		
				if (alertState == 1) {

				button4.setText("ALERT OFF");
				mAlertTime.setText("ALERT OFF");
				mAlertTime.setBackgroundResource(R.color.DarkGreen);	
				alertTitle.setText("");
				alertTitle.setBackgroundResource(R.color.White);
				System.out.println("alertState ==1 "+alertState);
				}

				if (alertState == 0) {
					System.out.println("alertState ==0 "+alertState);
					button4.setText("ALERT ON");	
					
					String fuatmt=formatUpdateAlertToMilitaryTime(alertTimeDB);					
					formatUpdateAlertYearMonthDay(alertTimeDB);
					updateAlertHourAndMinute(fuatmt);
					
					int intHour = getHour();
					int intMin = getMin();
					int intDay = getDay();
					int intMonth = getMonth();
					int intYear = getYear();
					
					
					String alertDateDisplay = intMonth  + "-" + intDay + "-"+ intYear;					
					ConvertStdTime ast = new ConvertStdTime();						
					String alertsStandardTime = ast.toStandard(intHour, intMin);
					
					mAlertTime.setBackgroundResource(R.color.LightGreen);		
					mAlertTime.setText( alertDateDisplay + " "+ alertsStandardTime);
					
				}

				// go back to list
				Button button2 = (Button) findViewById(R.id.btnBack);

				button2.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						
						Intent intent = new Intent(
								AlertsDetails.this.getApplicationContext(),
								TabBarExample.class);
						startActivity(intent);

					}
				});


				// remove from list
				Button button3 = (Button) findViewById(R.id.btnRemoveAlert);

				button3.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
					
						// cancel alarm
						  Intent osa = new Intent();
						  sender = PendingIntent.getBroadcast(AlertsDetails.this, id, osa, 0);
						  am = (AlarmManager) getSystemService(ALARM_SERVICE);
						  am.cancel(sender);
						
						// Tell the user about what we did.
						Toast.makeText(AlertsDetails.this, "Alert Removed", Toast.LENGTH_LONG)
								.show();
						
						// delete alert 
						entry.open();
						entry.deleteEntry(id);
						entry.close();
//
//						Intent intent = new Intent(AlertsDetails.this.getApplicationContext(), TabBarExample.class);
//						startActivity(intent);

					}
				});

				if (eventState== 1) {
					
					  Intent osa = new Intent();
					  sender = PendingIntent.getBroadcast(AlertsDetails.this, id, osa, 0);
					  am = (AlarmManager) getSystemService(ALARM_SERVICE);
					  am.cancel(sender);
					
					button4.setText("Canceled");
					mAlertTime.setText("Event Canceled");
					mAlertTime.setBackgroundResource(R.color.DarkestBlue);	
					alertTitle.setText("");
					alertTitle.setBackgroundResource(R.color.White);

					mLocationDisplay.setText("");
					mAlertTime.setText( " ");
					mDateDisplay.setText("");
					
					
				}else{

				// set on/off of alert state
				button4 = (Button) findViewById(R.id.btnSetAlert);

				button4.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						System.out.println("alertState btnSetAlert "+alertState);
						DatabaseSqlite updateAlertStatus = new DatabaseSqlite(AlertsDetails.this);
						
						
						switch (alertState) {
						case 0:
							button4.setText("ALERT OFF");							
							mAlertTime.setText("ALERT OFF");
							mAlertTime.setBackgroundResource(R.color.DarkGreen);	
							alertTitle.setText("");
							alertTitle.setBackgroundResource(R.color.White);
							alertState = 1;
							int alertDismissed=1;
							System.out.println("alertState case0=1 "+alertState);
							
							// remove alarm				
							 Intent intent = new Intent(getBaseContext(), OneShotAlarm.class);
							  PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), id, intent, 0);
							  AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
							  alarmManager.cancel(pendingIntent);
							
							
							
							// update the status of the alert state and alert dismissed state
							updateAlertStatus.open();
							updateAlertStatus.updateAlertState(id, alertState);
							alertState = updateAlertStatus.getAlertState(id);		
				    		updateAlertStatus.updateAlertDismissedState(id, alertDismissed);				    		
							updateAlertStatus.close();		
							
							
							
							break;

						case 1:
							
							
							// get alert time from local database
							alertState = 0;					
							alertDismissed=0;
							System.out.println("alertState case1=0 "+alertState);
							updateAlertStatus.open();
							updateAlertStatus.updateAlertDismissedState(id, alertDismissed);
							String getAlertInMillisFromDB = updateAlertStatus.getAlertTime(id);
							String getAlertInMinutesFromDB = updateAlertStatus.getMinutesForOneShot(id);
							updateAlertStatus.updateAlertState(id, alertState);
							alertState = updateAlertStatus.getAlertState(id);
							
							updateAlertStatus.close();
							
							resetAlarm(getAlertInMillisFromDB, getAlertInMinutesFromDB);

							int intHour = getHour();
							int intMin = getMin();
							int intDay = getDay();
							int intMonth = getMonth();
							int intYear = getYear();
							
							String alertDateDisplay =   intMonth + "-"+intDay + "-"
									+ intYear;
							
							
							ConvertStdTime ast = new ConvertStdTime();			
							String alertsStandardTime = ast.toStandard(intHour, intMin);
						
							break;
						}

					}
				});

				Spinner spinner = (Spinner) findViewById(R.id.spinner);
				ArrayAdapter<CharSequence> adapter = ArrayAdapter
						.createFromResource(this, R.array.Times_array,
								android.R.layout.simple_spinner_item);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				if(eventState==1){
					
					spinner.setEnabled(false);
					spinner.setClickable(false);
					
					spinner.setAdapter(adapter);
					
				}else{
					
					
					spinner.setAdapter(adapter);
					spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
					
					
				}
				
				
				
				
				
				}
			}

			public class MyOnItemSelectedListener implements OnItemSelectedListener {

				int count = 0;

				public void onItemSelected(AdapterView<?> parent, View view, int pos,
						long id1) {
					
					
					// if alert is off  an spinner is chosen 
				
					if (count >= 1) {
						
						

						String mins = parent.getItemAtPosition(pos).toString();

						int intmins=0;
						
						// process user's selection of alert time
						if(mins.equals("0 minutes")){intmins = 0;}
						if(mins.equals("5 minutes")){intmins = 5;}
						if(mins.equals("10 minutes")){intmins = 10;}
						if(mins.equals("20 minutes")){intmins = 20;}
						if(mins.equals("30 minutes")){intmins = 30;}
						if(mins.equals("40 minutes")){intmins = 40;}
						if(mins.equals("50 minutes")){intmins = 50;}
				
						String stringMinutes=""+intmins;
						setAlarm(intmins, stringMinutes);
						
						
						
					} else {

					}
					count++;
				}

				public void onNothingSelected(AdapterView parent) {
					mLocationDisplay.setText(" " + location);
				}
			}

			public void setAlarm(int intmins, String mins) {
				// based alarm time on start time of event. TODO get info from database.

				String currentDate;
				SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				Date date1 = null;
				DateTime dt;
				currentDate = eventdate + " " + startTimeMilitary;// startTimeMilitary;
			
			try {
					date1 = myFormat.parse(currentDate);
				

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				dt = new DateTime(date1);
				long dateInMillis = dt.getMillis();
				String sDateInMillis = Long.toString(dateInMillis);

				// subtract the selected time from the event's start time
					
				String	 newAlertTime = subtractTime(dt, intmins);
			
				 newAlertTime = subtractTime(dt, intmins);

				// extract the military time from the UTC format
				String alertsMilitaryTime = formatUpdateAlertToMilitaryTime(newAlertTime);

				// find the hour and minute
				updateAlertHourAndMinute(alertsMilitaryTime);

				// extract date from the UTC format
				formatUpdateAlertYearMonthDay(newAlertTime);

				int intHour = getHour();
				int intMin = getMin();
				int intDay = getDay();
				int intMonth = getMonth();
				int intYear = getYear();
				String alertStringInMills = getStringAlertInMillis();
			
				updateAlert(sDateInMillis, alertStringInMills, mins, newAlertTime,
						intYear, intMonth, intDay, intHour, intMin);
//				Intent intent = new Intent(AlertsDetails.this.getApplicationContext(),
//						TabBarExample.class);
//				intent.putExtra("goToTab", "EventsList");
//				startActivity(intent);

			}

			public String subtractTime(DateTime dt, int minusTime) {
				
				DateTime greaterDate;
				
				if(minusTime==60){
					
				 greaterDate = dt.minusMinutes(30);
				 
				 greaterDate=greaterDate.minusMinutes(30);
					
				}else{
					 greaterDate = dt.minusMinutes(minusTime);
					
				}

				// newAlertTime is in UTC format
				String newAlertTime = greaterDate.toString();

				long alertInMillis = greaterDate.getMillis();
				String sAlertInMillis = Long.toString(alertInMillis);
				// ////new alert time is a stirng

				setStringAlertInMillis(sAlertInMillis);

				return newAlertTime;

			}

			
			public void updateAlert(final String dateInMillis,
					final String alertinMillis, final String minutes,
					final String alertTime, int intYear, int intMonth, int intDay,
					int intHour, int intMin) {
				
				// create pending intent for alarm
				  Intent osa = new Intent(this, OneShotAlarm.class);
				  String idStringFormat=""+id+"";
					String warning="In "+ minutes +" Minutes";
					osa.putExtra("name", name);
					osa.putExtra("text", warning);
					osa.putExtra("id", idStringFormat);				  
				  sender = PendingIntent.getBroadcast(this, id, osa, 0);
				  am = (AlarmManager) getSystemService(ALARM_SERVICE);

		
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				System.out.println( "current time in millis" +calendar.getTimeInMillis());
				calendar.clear();				
				TimeZone timeZone = calendar.getTimeZone();
				calendar.setTimeZone(timeZone);
				calendar.set(intYear, intMonth -1, intDay, intHour, intMin, 0);
			
		
				
				
	if(System.currentTimeMillis()>calendar.getTimeInMillis() ){
					System.out.println("out of time"	);
					
					// remove pending intent 
					am.cancel(sender);
					
					button4.setText("No Update");							
					mAlertTime.setText("Time Passed for Alert");
					mAlertTime.setBackgroundResource(R.color.DarkGreen);	
					alertTitle.setText("");
					alertTitle.setBackgroundResource(R.color.White);								
					
					DatabaseSqlite updateAlertStatus = new DatabaseSqlite(AlertsDetails.this);
					
					// update the status of the alert state and alert dismissed state
					updateAlertStatus.open();					
					updateAlertStatus.updateAlertState(id, 1);
					alertState = updateAlertStatus.getAlertState(id);		
					System.out.println(" time base alertstate "+alertState);
		    		updateAlertStatus.updateAlertDismissedState(id, 1);				    		
					updateAlertStatus.close();				
					

					Toast.makeText(AlertsDetails.this.getApplicationContext(), name+" time has passed.", Toast.LENGTH_LONG).show();
					
				}
				else{
					// save update to local database
					DatabaseSqlite update =new DatabaseSqlite(AlertsDetails.this);			
					update.open();							
					update.updateAlertInDB(id, date, alertinMillis, minutes, alertTime);
					update.updateAlertDismissedState(id, 0);
					update.updateAlertState(id, 0);
					update.close();							
				
					String alertDateDisplay = "";
					alertDateDisplay=intMonth + "-"+intDay + "-"+ intYear;

					ConvertStdTime ast = new ConvertStdTime();
					String alertsStandardTime ="";
					alertsStandardTime = ast.toStandard(intHour, intMin);
					mAlertTime.setText( alertDateDisplay + " "+ alertsStandardTime);
					button4.setText("ALERT ON");
					mAlertTime.setBackgroundResource(R.color.LightGreen);
					alertTitle.setBackgroundResource(R.color.LightGreen);
					alertTitle.setText("Alert Set To:");
					
					
					am.cancel(sender);
					am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
					

					Toast.makeText(AlertsDetails.this.getApplicationContext(), name+" alert has been updated.", Toast.LENGTH_LONG).show();
					
			
					
				}
				
				
				
				
			}
			
			
			public void resetAlarm(String getAlertInMillisFromDB,
					String getAlertInMinutesFromDB) {

				// based alarm time on start time of event. TODO get info from database.
				String currentDate;
				SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				Date date1 = null;
				DateTime dt;
				currentDate = eventdate + " " + startTimeMilitary;// startTimeMilitary;
				

				try {
					date1 = myFormat.parse(currentDate);
					

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				dt = new DateTime(date1);
				long dateInMillis = dt.getMillis();
				String sDateInMillis = Long.toString(dateInMillis);

				// extract the military time from the UTC format
				String alertsMilitaryTime = formatUpdateAlertToMilitaryTime(getAlertInMillisFromDB);

				// find the hour and minute
				updateAlertHourAndMinute(alertsMilitaryTime);

				// extract date from the UTC format
				formatUpdateAlertYearMonthDay(getAlertInMillisFromDB);

				int intHour = getHour();
				int intMin = getMin();
				int intDay = getDay();
				int intMonth = getMonth();
				int intYear = getYear();
				String alertStringInMills = getStringAlertInMillis();
				
				
				

				updateAlert(sDateInMillis, alertStringInMills, getAlertInMinutesFromDB,
						getAlertInMillisFromDB, intYear, intMonth, intDay, intHour,
						intMin);
//				Intent intent = new Intent(AlertsDetails.this.getApplicationContext(),
//						TabBarExample.class);
//				intent.putExtra("goToTab", "EventsList");
//				startActivity(intent);

			}

			
			public void formatUpdateAlertYearMonthDay(String newAlertTime) {
//yyyymmdd
				String zero = "0";
			
				String updateAlertdate ;
				String eventYear;
				String eventDay ;
				String eventMonth ;
				String findZero;
				String findZeroDay;
				
				if(newAlertTime.contains("T")){
					
				int indexT = newAlertTime.indexOf('T');
				 updateAlertdate = newAlertTime.substring(0, indexT);
				 eventYear = updateAlertdate.substring(0, 4);
				 eventDay = updateAlertdate.substring(8, 10);
				 eventMonth = "";
				 findZero = updateAlertdate.substring(5, 6);
				 findZeroDay=updateAlertdate.substring(8,9);
				
				if (findZero.equals(zero)) {

					eventMonth = updateAlertdate.substring(6, 7);

				} else {

					eventMonth = updateAlertdate.substring(5, 7);
				}

				if (findZeroDay.equals(zero)) {

					eventDay = updateAlertdate.substring(9, 10);

				} else {

					eventDay = updateAlertdate.substring(8, 10);
				}
				
				
				int intYear = Integer.parseInt(eventYear);
				int intMonth = Integer.parseInt(eventMonth);
				int intDay = Integer.parseInt(eventDay);

				setYear(intYear);
				setMonth(intMonth);
				setDay(intDay);
				}
				
				if(!newAlertTime.contains("T")){
					
					
					 updateAlertdate = newAlertTime;
					 eventYear = updateAlertdate.substring(0, 4);
					 eventDay = updateAlertdate.substring(8, 10);
					 eventMonth = "";
					 findZero = updateAlertdate.substring(5, 6);
					 findZeroDay=updateAlertdate.substring(8,9);
					
					if (findZero.equals(zero)) {

						eventMonth = updateAlertdate.substring(6, 7);

					} else {

						eventMonth = updateAlertdate.substring(5, 7);
					}

					if (findZeroDay.equals(zero)) {

						eventDay = updateAlertdate.substring(9, 10);

					} else {

						eventDay = updateAlertdate.substring(8, 10);
					}
					
					
					int intYear = Integer.parseInt(eventYear);
					int intMonth = Integer.parseInt(eventMonth);
					int intDay = Integer.parseInt(eventDay);

					setYear(intYear);
					setMonth(intMonth);
					setDay(intDay);
					}
			}

			
			
			
			public void updateAlertHourAndMinute(String alertsMilitaryTime) {

				int index = alertsMilitaryTime.indexOf(':');
				String defaultStartHour = "";
				String defaultStartMin = "";
				String findhourzero = alertsMilitaryTime.substring(0, index - 1);
				String findminzero = alertsMilitaryTime.substring(index + 1, index + 2);

				String zero = "0";

				if (findminzero.equals(zero)) {

					defaultStartMin = alertsMilitaryTime
							.substring(index + 2, index + 3);

				} else {

					defaultStartMin = alertsMilitaryTime
							.substring(index + 1, index + 3);
				}

				if (findhourzero.equals(zero)) {

					defaultStartHour = alertsMilitaryTime.substring(1, index);

				} else {

					defaultStartHour = alertsMilitaryTime.substring(0, index);
				}

				// get integer of hour and minute for calendar
				int intHour = Integer.parseInt(defaultStartHour);
				int intMin = Integer.parseInt(defaultStartMin);

				setHour(intHour);
				setMin(intMin);
			}

			
			public String formatUpdateAlertToMilitaryTime(String newAlertTime) {
				// remove the date and T from string to get the Military time
				String updateAlertTime = newAlertTime;
				int indexT = updateAlertTime.indexOf('T');
				int indexPeriod = updateAlertTime.indexOf('.');
				int difference = indexPeriod - updateAlertTime.length();
				int removeEnd = difference + updateAlertTime.length();// remove timezone
				String alertsMilitaryTime = updateAlertTime.substring(indexT + 1,
						removeEnd);


				return alertsMilitaryTime;

			}

			// for older phones, override hard back button
			@Override
			public boolean onKeyDown(int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
					Intent intent = new Intent(
							AlertsDetails.this.getApplicationContext(), TabBarExample.class);
					startActivity(intent);
					return true;
				}

				return super.onKeyDown(keyCode, event);
			}
			
	
	public int getHour() {

		return intHour;

	}

	public void setHour(int intHour) {
		this.intHour = intHour;
	}

	public int getMin() {

		return intMin;

	}

	public void setMin(int intMin) {

		this.intMin = intMin;

	}

	public void setYear(int intYear) {

		this.intYear = intYear;

	}

	public int getYear() {

		return intYear;

	}

	public int getMonth() {

		return intMonth;

	}

	public void setMonth(int intMonth) {

		this.intMonth = intMonth;

	}

	public int getDay() {

		
		return intDay;

	}

	public void setDay(int intDay) {

		this.intDay = intDay;

	}

	public String getStringAlertInMillis() {

		return sAlertInMillis;

	}

	public void setStringAlertInMillis(String sAlertInMillis) {
		this.sAlertInMillis = sAlertInMillis;
	}
		

}
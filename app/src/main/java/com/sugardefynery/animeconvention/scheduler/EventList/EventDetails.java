package com.sugardefynery.animeconvention.scheduler.EventList;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;




import com.sugardefynery.animeconvention.scheduler.DemoActivity;
import com.sugardefynery.animeconvention.scheduler.R;
import com.sugardefynery.animeconvention.scheduler.TabBarExample;
import com.sugardefynery.animeconvention.scheduler.Alerts.AlertDialogActivity;
import com.sugardefynery.animeconvention.scheduler.Alerts.AlertView;
import com.sugardefynery.animeconvention.scheduler.Alerts.AlertsDetails;
import com.sugardefynery.animeconvention.scheduler.Alerts.OneShotAlarm;
import com.sugardefynery.animeconvention.scheduler.sqllite.DatabaseSqlite;

public class EventDetails extends Activity {

	Integer id;
	String name;
	String date;
	String startTime;
	String endTime;
	String location;
	String description;
	Bundle bundle;

	private TextView mTitleDisplay;
	private TextView mDateDisplay;
	private TextView mTimeDisplay;
	private TextView mTimeEndDisplay;
	private TextView mLocationDisplay;
	private TextView mDescriptionDisplay;
	private String update_alarmTime;
	String eventdate;
	
	Toast mToast;
	PendingIntent sender;
	Intent intent;
	AlarmManager am;


	private int intHour;
	private int intMin;
	private int intDay;
	private int intMonth;
	private int intYear;

	private String sAlertInMillis;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_details);
		

		bundle = getIntent().getExtras();
		id = bundle.getInt("id");
		name = bundle.getString("name");
		date = bundle.getString("date");
		startTime = bundle.getString("startTime");
		endTime = bundle.getString("endTime");
		location = bundle.getString("location");
		description=bundle.getString("description");
		
	
		mTitleDisplay = (TextView) findViewById(R.id.titleDisplay);
		mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
	
		mLocationDisplay = (TextView) findViewById(R.id.locationDisplay);
		mDescriptionDisplay = (TextView) findViewById(R.id.descriptionDisplay);
		
		// set start
	
		
		//set description
		mDescriptionDisplay.setText(description);
		
		// set title
		mTitleDisplay.setText(name);

		// set location
		mLocationDisplay.setText(location);
				
		// set Date
		String eventYear = date.substring(0, 4);
		String eventMonth = date.substring(5, 7);
		String eventDay = date.substring(8, 10);
        String displayDate = eventYear + ":" + eventMonth + ":" +eventDay ;
//
		formatUpdateAlertYearMonthDay(displayDate);
		String formatDisplayDate = "" +getMonth() + "-" +getDay() + "-"+ getYear();
		mDateDisplay.setText(formatDisplayDate +" " +startTime+ " to "+ endTime);

		eventdate = eventMonth + "/" + eventDay + "/" + eventYear;
		
		
		Button button2 = (Button) findViewById(R.id.btnBack);

		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(EventDetails.this
						.getApplicationContext(), TabBarExample.class);
				startActivity(intent);

			}
		});

		Button button3 = (Button) findViewById(R.id.btnSave);

		button3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					
					SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					Date date1 = null;
					DateTime dt;

					ConvertStdTime alertConvert = new ConvertStdTime();
					String startTimeMilitary = alertConvert.toMilitaryString(startTime);

					String alertStringDate = eventdate + " "+ startTimeMilitary;// startTimeMilitary

			
					try {

						date1 = myFormat.parse(alertStringDate);
						

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					dt = new DateTime(date1);
					long dateInMillis = dt.getMillis();
					String sDateInMillis = Long.toString(dateInMillis);

					DateTime greaterDate = dt.minusMinutes(10);
					String newAlertTime = greaterDate.toString();

					long alertInMillis = greaterDate.getMillis();
					String sAlertInMillis = Long.toString(alertInMillis);

				

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
					setStringAlertInMillis(sAlertInMillis);
					String alertStringInMills = getStringAlertInMillis();
					String minutes = "10";
					
					updateAlert(sDateInMillis, alertStringInMills, minutes,
							newAlertTime, intYear, intMonth, intDay, intHour,
							intMin);
			
				} catch (Exception e) {

					String error = e.toString();
					System.out.println(error);

				}

				Intent intent = new Intent(
						EventDetails.this.getApplicationContext(),
						TabBarExample.class);
				startActivity(intent);
				
			}
		});

	}
	
	public void updateAlert(final String dateInMillis,
			final String alertinMillis, final String minutes,
			final String alertTime, int intYear, int intMonth, int intDay,
			int intHour, int intMin) {
		
		
		
//		  
		  Calendar calendar = Calendar.getInstance();

			calendar.setTimeInMillis(System.currentTimeMillis());
			
			calendar.clear();
			//
			TimeZone timeZone = calendar.getTimeZone();
			calendar.setTimeZone(timeZone);

			calendar.set(intYear, intMonth -1, intDay, intHour, intMin, 0);
			
		
			
			long todayTimeInMillis=System.currentTimeMillis();
			long alertInMillis= calendar.getTimeInMillis();
			
			
			if(todayTimeInMillis>alertInMillis){
				

				Toast.makeText(EventDetails.this.getApplicationContext(), "Alert time is too close to event time. Alert was not created", Toast.LENGTH_LONG).show();
				
				
			}else{

		
		int alarmDismissed=0;
		int eventstate = 0;
		int alertState = 0;
		
		DatabaseSqlite entry = new DatabaseSqlite(EventDetails.this);

		entry.open();
		entry.createEntry(id, name, startTime, alertTime,
				alertState, location, eventstate, date,
				sAlertInMillis, minutes,alarmDismissed);


		// Tell the user about what we did.
		Toast.makeText(EventDetails.this, "Alert Set 10 minutes Before Event time. See the Alert's tab for more...", Toast.LENGTH_LONG)
				.show();
		
		int newId=entry.getHighestID();
				
		entry.updateAlertInDB(newId, date, alertinMillis, minutes, alertTime);

		entry.close();		
		
				Intent osa = new Intent(EventDetails.this.getApplicationContext(), OneShotAlarm.class);
		  		String idStringFormat=""+newId+"";
		  		String warning="In 10 Minutes";
				osa.putExtra("name", name);
				osa.putExtra("text", warning);
				osa.putExtra("id", idStringFormat);				  
				sender = PendingIntent.getBroadcast(EventDetails.this.getApplicationContext(), newId, osa, 0);
				am = (AlarmManager) getSystemService(ALARM_SERVICE);


			
			
			am.cancel(sender);
			am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
	


		
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

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    	Intent intent = new Intent(
					EventDetails.this.getApplicationContext(),
					TabBarExample.class);
			startActivity(intent);
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	
	public void formatUpdateAlertYearMonthDay(String newAlertTime) {

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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	
	
}
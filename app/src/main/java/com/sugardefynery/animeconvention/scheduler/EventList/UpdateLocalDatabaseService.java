package com.sugardefynery.animeconvention.scheduler.EventList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.sugardefynery.animeconvention.scheduler.GCMIntentService;
import com.sugardefynery.animeconvention.scheduler.R;
import com.sugardefynery.animeconvention.scheduler.TabBarExample;
import com.sugardefynery.animeconvention.scheduler.Alerts.AlertAdapter;
import com.sugardefynery.animeconvention.scheduler.Alerts.AlertView;
import com.sugardefynery.animeconvention.scheduler.Alerts.Alerts;
import com.sugardefynery.animeconvention.scheduler.Alerts.AlertsDetails;
import com.sugardefynery.animeconvention.scheduler.Alerts.OneShotAlarm;
import com.sugardefynery.animeconvention.scheduler.sqllite.DatabaseSqlite;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;

public class UpdateLocalDatabaseService extends Service{
		
	JSONObject json;
	private static final String ARRAY_NAME = "events";
	private static final String rssFeed = "http://www.txtease.com/android/push/login/updateMemberDatabase.php";
	private static final String UNPROCESSED_EVENT_DATE = "event_date";
	private static final String EVENT_START = "event_start";
	private static final String EVENT_END = "event_end";
	private static final String EVENT_LOCATION = "event_location";
	private static final String EVENT_STATE = "event_delete_flag";
	Bundle bundle;
	List<Item> arrayOfList;
	Integer server_id;
	String serverID;	
	Integer local_id;
	String eventName;
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
	
	
	String tag = "UpdateLocalDB Service";
	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.i(tag, "Service created...");
		

	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("TAG", "started onstart command Created from Alerts service .");
		return super.onStartCommand(intent, flags, startId);// START_STICKY;
	}
	

	@Override
	public void onStart(final Intent intent, int startId) {
		super.onStart(intent, startId);		
	
		Log.i(tag, "Service started...");		
		
		bundle = intent.getExtras();
		
		server_id = bundle.getInt("server_id");
		serverID = Integer.toString(server_id);
		local_id=bundle.getInt("local_id");
		eventName=bundle.getString("event_name");
		System.out.println("UPDATELOCALDATABASE -----  server_id "+ server_id + "server_ID "+ serverID+ " local_id "+local_id);
		
		arrayOfList = new ArrayList<Item>();

		if (Utils.isNetworkAvailable(UpdateLocalDatabaseService.this)) {
			new MyTask().execute(rssFeed);
		} else {
			Toast.makeText(this, "Application is unable to load data.", Toast.LENGTH_LONG).show();
			
			stopService(new Intent(getBaseContext(), UpdateLocalDatabaseService.class));
		}

	}
	
	class MyTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();


		}

		
		@Override
		protected String doInBackground(String... params) {

			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
			paramsx.add(new BasicNameValuePair("id",serverID));
	
			json = jsonParser.getJSONFromUrl(rssFeed, paramsx);
			String jsonString=json.toString();
			return jsonString;
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			int i=0;
			JSONObject mainJson = null;
			JSONArray jsonArray = null;
			
			try {
				mainJson = new JSONObject(result);
				jsonArray = mainJson.getJSONArray(ARRAY_NAME);
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
		
			
			if (null == result || result.length() == 0) {
				Toast.makeText(UpdateLocalDatabaseService.this,"Application is unable to load data.", Toast.LENGTH_LONG).show();
				
				stopService(new Intent(getBaseContext(), UpdateLocalDatabaseService.class));
				
			} else {

				try {
					//JSONObject mainJson = new JSONObject();
					
					for ( i = 0; i < jsonArray.length(); i++) {
						JSONObject objJson = jsonArray.getJSONObject(i);

						String newEventStart = objJson.getString(EVENT_START);
						String newEventEnd = objJson.getString(EVENT_END);
						String newLocation = objJson.getString(EVENT_LOCATION);
						String newEventDate = objJson.getString(UNPROCESSED_EVENT_DATE);
						String newEventState = objJson.getString(EVENT_STATE);
						
						
						int intNewEventState=Integer.parseInt(newEventState);
						
						//update local database here.
						System.out.println("UPDATELOCALDATABASE ----- new event State "+newEventState);

						String eventYear = newEventDate.substring(0, 4);
						String eventMonth = newEventDate.substring(5, 7);
						String eventDay = newEventDate.substring(8, 10);
						String eventdate = eventMonth + "/" + eventDay + "/"+ eventYear;
						
						SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
						Date date1 = null;
						DateTime dt;

						ConvertStdTime alertConvert = new ConvertStdTime();
						String startTimeMilitary = alertConvert.toMilitaryString(newEventStart);

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
						
						
						
						updateAlert(sDateInMillis, alertStringInMills, "10",
								newAlertTime, intYear, intMonth, intDay, intHour,
								intMin,newEventDate,newEventStart, newLocation,intNewEventState);

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				if(i == jsonArray.length()-1){
					
			System.out.println("UpdatelocaldatabaseSerive stopped");
					
				stopService(new Intent(getBaseContext(), UpdateLocalDatabaseService.class));
				}
			}								
		}
	}

	public void updateAlert(final String dateInMillis,
			final String alertinMillis, final String minutes,
			final String alertTime, int intYear, int intMonth, int intDay,
			int intHour, int intMin, String newEventDate, String newEventStart,String newLocation,int intNewEventState) {

		
		DatabaseSqlite update = new DatabaseSqlite(UpdateLocalDatabaseService.this);
		update.open();
		

		update.updateAlertFromMember(local_id,newEventDate, alertinMillis, minutes, alertTime, newEventStart, newLocation, intNewEventState);

		// wher is new start time?Location?
		//update.updateAlertInDB(local_id, newEventDate, alertinMillis, minutes, alertTime,newEventStart,newLocation);

		update.close();
		

		  Intent osa = new Intent(UpdateLocalDatabaseService.this.getApplicationContext(), OneShotAlarm.class);
		  String idStringFormat=""+local_id+"";
			String warning="In "+ minutes +" Minutes";
			osa.putExtra("name", eventName);
			osa.putExtra("text", warning);
			osa.putExtra("id", idStringFormat);				  
		  sender = PendingIntent.getBroadcast(UpdateLocalDatabaseService.this.getApplicationContext(), local_id, osa, 0);
		  am = (AlarmManager) getSystemService(ALARM_SERVICE);

//		  
		  Calendar calendar = Calendar.getInstance();

			calendar.setTimeInMillis(System.currentTimeMillis());
			
			calendar.clear();
			//
			TimeZone timeZone = calendar.getTimeZone();
			calendar.setTimeZone(timeZone);
intMonth=intMonth-1;
			calendar.set(intYear, intMonth , intDay, intHour, intMin, 0);
			
			
			
			
// if time has passed, no updates
			
			if(System.currentTimeMillis()>calendar.getTimeInMillis() || intNewEventState==1){
				
				// remove pending intent 
				am.cancel(sender);
				DatabaseSqlite updateAlertStatus = new DatabaseSqlite(UpdateLocalDatabaseService.this);
				
				// update the status of the alert state and alert dismissed state
				updateAlertStatus.open();					
				updateAlertStatus.updateAlertState(local_id, 1);
				int alertState = updateAlertStatus.getAlertState(local_id);		
				System.out.println(" time base alertstate "+alertState);
	    		updateAlertStatus.updateAlertDismissedState(local_id, 1);				    		
				updateAlertStatus.close();				
				

				Toast.makeText(UpdateLocalDatabaseService.this.getApplicationContext(), eventName+" was removed from the schedule. Alert has been canceled", Toast.LENGTH_LONG).show();
				
			}
			else{
				
		DatabaseSqlite updateAlertStatus = new DatabaseSqlite(UpdateLocalDatabaseService.this);
				
				// update the status of the alert state and alert dismissed state
				updateAlertStatus.open();					
				updateAlertStatus.updateAlertState(local_id,0);
				int alertState = updateAlertStatus.getAlertState(local_id);		
				System.out.println(" time base alertstate else "+alertState);
	    		updateAlertStatus.updateAlertDismissedState(local_id, 0);				    		
				updateAlertStatus.close();				
				
				
				am.cancel(sender);
				am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
				

				Toast.makeText(UpdateLocalDatabaseService.this.getApplicationContext(), eventName+" alert has been updated", Toast.LENGTH_LONG).show();
				
		
				
			}
			

		// Tell the user about what we did.
		if (mToast != null) {
			mToast.cancel();
		}

		
		
		//stopService(new Intent(getBaseContext(), UpdateLocalDatabaseService.class));

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
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	

	

}

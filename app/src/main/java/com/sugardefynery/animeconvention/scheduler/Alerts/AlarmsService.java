package com.sugardefynery.animeconvention.scheduler.Alerts;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.TimeZone;

import org.joda.time.DateTime;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;


import com.sugardefynery.animeconvention.scheduler.PreferenceConnector;
import com.sugardefynery.animeconvention.scheduler.R;
import com.sugardefynery.animeconvention.scheduler.TabBarExample;
import com.sugardefynery.animeconvention.scheduler.EventList.ConvertStdTime;
import com.sugardefynery.animeconvention.scheduler.sqllite.DatabaseSqlite;




public class AlarmsService extends Service {

	DatabaseSqlite db = new DatabaseSqlite(this);
	List<Alerts> listAlerts;
	
	PendingIntent sender;
	Intent intent;
	AlarmManager am;
	int id;
	private int intHour=0;
	private int intMin=0;
	private int intDay=0;
	private int intMonth=0;
	private int intYear=0;
						
	
	 ArrayList<String> alertNames = new ArrayList<String>();

	String alertInUTC;
	String alertDuration;
	String eventName ;
    int eventState;
	MediaPlayer player = new MediaPlayer();
	Bundle bundle;
	Vibrator vibrate;
	DatabaseSqlite entry = new DatabaseSqlite(AlarmsService.this);
	int vibrateState;
	int soundState;
	 AlertDialog.Builder builder;
	 Intent i = new Intent();
		

	int idInteger;
	int alertDismissed = 1;

	private String sAlertInMillis;
	
	String tag = "Alert Service";

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
		

				

		
					
					db.open();
					listAlerts = db.getAlertsforService();
					db.close();
					int alerts=listAlerts.size();
					

					for (int i = 0; i < alerts; i++) {
						Alerts item = listAlerts.get(i);
					
					 id =item.getRowId();					
					 alertInUTC = item.getAlertTime();
					 alertDuration = item.getAlertMinutes();
					 eventName = item.getEventName();
					 
					 // check if alert is on
					int eventCanceled=item.getEventState();
					int alertOff=item.getAlertState();
					int alertDismissed=item.getAlarmDismissed();
					
					
					
					
					
				     
					if(eventCanceled==0 && alertOff==0 && alertDismissed==0){
				   
						Calendar calendar = Calendar.getInstance();

						calendar.setTimeInMillis(System.currentTimeMillis());

						long todayTimeInMillis=System.currentTimeMillis();
						String alertsMilitaryTime = formatUpdateAlertToMilitaryTime( alertInUTC);
				     
				     
						
						// find the hour and minute
						updateAlertHourAndMinute(alertsMilitaryTime);

						// extract date from the UTC format
						formatUpdateAlertYearMonthDay(alertInUTC);

						int intHour = getHour();
						int intMin = getMin();
						int intDay = getDay();
						int intMonth = getMonth();
						int intYear = getYear();
						
						
						SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
						Date date1 = null;
						DateTime dt;

						String alertStringDate = intMonth +"/"+intDay+"/"+intYear+" "+intHour +":"+intMin+":00";
				
						
						
						try {

							date1 = myFormat.parse(alertStringDate);
							

						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						dt = new DateTime(date1);
						long alertInMillis = dt.getMillis();
						String sDateInMillis = Long.toString(alertInMillis);
					
						if(todayTimeInMillis>alertInMillis){
							
							
							  Intent osa = new Intent(AlarmsService.this.getApplicationContext(), OneShotAlarm.class);										  
							  sender = PendingIntent.getBroadcast(AlarmsService.this.getApplicationContext(), id, osa, 0);
							  am = (AlarmManager) getSystemService(ALARM_SERVICE);
							  am.cancel(sender);
								
						    
						     
						     entry.open();
					    		alertDismissed=1;
					    		entry.updateAlertDismissedState(id, alertDismissed);
					    		
					    		entry.close();
								
							
						     alertNames.add(eventName);
							 			  
							
						}else{
					
					resetAlarm( alertInUTC,alertDuration);
					
						}
					}// if checker

					}//for loop
					
					//// send list intent to AlertServiceDialogActivity
					
					i.setClass(this, AlertServiceDialogActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);					 
					i.putStringArrayListExtra("alertNames", alertNames);					
                    i.putExtra("eventName", eventName);
                     startActivity(i); 

                     	
	

				}

	
	public void resetAlarm(String getAlertInMillisFromDB,
			String getAlertInMinutesFromDB) {

	
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
		

		updateAlert(id, eventName, getAlertInMinutesFromDB,
				getAlertInMillisFromDB, intYear, intMonth, intDay, intHour,
				intMin);


	}


	public void updateAlert(int id, String name,
			 final String minutes,
			final String alertTime, int intYear, int intMonth, int intDay,
			int intHour, int intMin) {
		
		  Intent osa = new Intent(AlarmsService.this.getApplicationContext(), OneShotAlarm.class);
		  String idStringFormat=""+id+"";
		  String warning="In "+ minutes +" Minutes";
		  osa.putExtra("name", name);
		  osa.putExtra("text", warning);
		  osa.putExtra("id", idStringFormat);				  
		  sender = PendingIntent.getBroadcast(AlarmsService.this.getApplicationContext(), id, osa, 0);
		  am = (AlarmManager) getSystemService(ALARM_SERVICE);

		

		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(System.currentTimeMillis());
		
		calendar.clear();
		//
		TimeZone timeZone = calendar.getTimeZone();
		calendar.setTimeZone(timeZone);

		intMonth=intMonth-1;
		calendar.set(intYear, intMonth, intDay, intHour, intMin, 0);
				
		
	

		am.cancel(sender);
		am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
	
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
		
	

	
	public void formatUpdateAlertYearMonthDay(String newAlertTime) {
		
						String zero = "0";
					
						String updateAlertdate ;
						String eventYear;
						String eventDay ;
						String eventMonth ;
						String findZero;
						String findZeroDay;
						
						
							
						int indexT = newAlertTime.indexOf('T');
						 updateAlertdate = newAlertTime.substring(0, indexT);
						 eventYear = updateAlertdate.substring(0, 4);
						 eventDay = updateAlertdate.substring(8, 10);
						 eventMonth = "";
						 findZero = updateAlertdate.substring(5, 6);
						 findZeroDay=updateAlertdate.substring(8,9);
						
					

						eventMonth = updateAlertdate.substring(5, 7);
						
						eventDay = updateAlertdate.substring(8, 10);
						
						
						
						int intYear = Integer.parseInt(eventYear);
						int intMonth = Integer.parseInt(eventMonth);
						int intDay = Integer.parseInt(eventDay);

						setYear(intYear);
						setMonth(intMonth);
						setDay(intDay);
						
						
						
					}

					
					
					
					public void updateAlertHourAndMinute(String alertsMilitaryTime) {

						int index = alertsMilitaryTime.indexOf(':');
						String defaultStartHour = "";
						String defaultStartMin = "";
						String findhourzero = alertsMilitaryTime.substring(0, index - 1);
						String findminzero = alertsMilitaryTime.substring(index + 1, index + 2);

						String zero = "0";


							defaultStartMin = alertsMilitaryTime
									.substring(index + 1, index + 3);
						
							defaultStartHour = alertsMilitaryTime.substring(0, index);
						

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
					public void onDestroy() {
						super.onDestroy();
						Toast.makeText(this, "Service destroyed...", Toast.LENGTH_LONG).show();
					}

					@Override
					public IBinder onBind(Intent intent) {
						// TODO Auto-generated method stub
						return null;
					}

					
				
		
					
					
					private void sound() {

						soundState = PreferenceConnector.readInteger(AlarmsService.this,
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
								.readInteger(AlarmsService.this,
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

	
	
		}
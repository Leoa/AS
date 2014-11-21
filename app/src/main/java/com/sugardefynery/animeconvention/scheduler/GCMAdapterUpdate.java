package com.sugardefynery.animeconvention.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sugardefynery.animeconvention.scheduler.EventList.Item;
import com.sugardefynery.animeconvention.scheduler.EventList.NewsRowAdapter;
import com.sugardefynery.animeconvention.scheduler.EventList.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;


public class GCMAdapterUpdate extends Activity{
	

	private static final String rssFeed = "http://txtease.com/android/push/login/memberschedule.php";
	private static final String ARRAY_NAME = "events";
	private static final String EVENT_NAME = "event_name";
	private static final String EVENT_ID = "event_id";
	private static final String EVENT_DATE = "event_date";
	private static final String EVENT_START = "event_start";
	private static final String EVENT_END = "event_end";
	private static final String EVENT_LOCATION = "event_location";
	private static final String UNPROCESSED_EVENT_DATE= "event_date";
	private static final String DESCRIPTION="event_description";

	List<Item> arrayOfList;
	
	NewsRowAdapter objAdapter;
	JSONObject json;
	private int intHour;
	private int intMin;
	private int intDay;
	private int intMonth;
	private int intYear;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		arrayOfList = new ArrayList<Item>();

		if (Utils.isNetworkAvailable(GCMAdapterUpdate.this)) {
			new MyTask().execute(rssFeed);
		}

	}

	// My AsyncTask start...

	class MyTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			if (isCancelled()) {

				this.cancel(true);
			}
		}

		@Override
		protected String doInBackground(String... params) {

			if (isCancelled()) {

				this.cancel(true);
			}

			return Utils.getJSONString(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);


			if (null == result || result.length() == 0) {
				
				GCMAdapterUpdate.this.finish();
			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					JSONArray jsonArray = mainJson.getJSONArray(ARRAY_NAME);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject objJson = jsonArray.getJSONObject(i);

						Item objItem = new Item();

						// here objItem is reciveing and processing data from
						// JSON string text
						objItem.setId(objJson.getInt(EVENT_ID));
						objItem.setName(objJson.getString(EVENT_NAME));
						objItem.setDescription(objJson.getString(DESCRIPTION));
						objItem.setStartTime(objJson.getString(EVENT_START));
						objItem.setEndTime(objJson.getString(EVENT_END));
						objItem.setLocation(objJson.getString(EVENT_LOCATION));
						objItem.setUprocessedDate(objJson.getString(UNPROCESSED_EVENT_DATE));
						formatUpdateAlertYearMonthDay(objJson.getString(EVENT_DATE));
						String formatDisplayDate=""+getMonth()+"-"+getDay()+"-"+getYear();
						objItem.setDate(formatDisplayDate);


						arrayOfList.add(objItem);

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			setAdapterToListview();
			   System.out.println("GCMAdapterUpdate " );
			   
			     
		}
	}

	
	public void setAdapterToListview() {

		objAdapter = new NewsRowAdapter(GCMAdapterUpdate.this,
				R.layout.schedulelistrow, arrayOfList);
		objAdapter.notifyDataSetChanged();
		
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




	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		Intent tbx=new Intent(GCMAdapterUpdate.this, TabBarExample.class);
//		   startActivity(tbx);
	}
					



	
}
	
	

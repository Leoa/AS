package com.sugardefynery.animeconvention.scheduler.EventList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.sugardefynery.animeconvention.scheduler.R;
import com.sugardefynery.animeconvention.scheduler.TabBarExample;


public class DataView extends Activity implements OnItemClickListener {

	ProgressDialog pDialog;
	private static final String rssFeed = "http://txtease.com/android/push/login/memberschedule.php";
	private static final String ARRAY_NAME = "events";
	private static final String EVENT_NAME = "event_name";
	private static final String EVENT_ID = "event_id";
	private static final String EVENT_DATE = "event_date";
	private static final String EVENT_START = "event_start";
	private static final String EVENT_START_COMPARE = "event_start";
	private static final String EVENT_END = "event_end";
	private static final String EVENT_LOCATION = "event_location";
	private static final String UNPROCESSED_EVENT_DATE= "event_date";
	private static final String DESCRIPTION="event_description";

	List<Item> arrayOfList;
	ListView listView;
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
		setContentView(R.layout.schedulelistview);

		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemClickListener(this);

		// List <Item> arrayofList = new ArrayList<Item>();
		// List with generic type of Item, from class Item, is assigned and
		// arraylist that is strict typed to Item.
		// Array list is a collection type, like Linked List ect... Item is the
		// type of element in the collection
		// Collection defines the objects features,sort manor basics are Map,
		// list, Arraylist
		// Generics strict type the list of object ex Item in List<Item>
		// The ArrayList class extends AbstractList and implements the List
		// interface. ArrayList supports dynamic
		// arrays that can grow as needed.

		arrayOfList = new ArrayList<Item>();

		if (Utils.isNetworkAvailable(DataView.this)) {
			
			new MyTask().execute(rssFeed);
		} else {
			showToast("Internet connection is unavaiable. Application is unable to get data.");
		}

		
		
		
	}

	// My AsyncTask start...

	class MyTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(DataView.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();

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

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showToast("Internet is not avaialble.");
				DataView.this.finish();
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
						
						
						//////////////
						String standardTime =objJson.getString(EVENT_START);
						  
					    ConvertStdTime cst= new ConvertStdTime();
					   String convertedTime =cst.toMilitaryString(standardTime);
					   System.out.println("*****START convertedTime" + convertedTime);
					   
					   // utC time 
					   
					   String currentDate;
						SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm:ss");
						Date date1 = null;
						DateTime dt;
						currentDate = convertedTime;// startTimeMilitary;
						

						try {
							date1 = myFormat.parse(currentDate);
							

						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						dt = new DateTime(date1);
						long dateInMillis = dt.getMillis();
						
						///////////////////
						
						
						objItem.setStartTimeCompare(dateInMillis);						
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
			   System.out.println("Dataview " );
			     
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View viewDel, int position,
			long id) {

		Intent intent = new Intent(DataView.this.getApplicationContext(),
				EventDetails.class);

		for (int i = 0; i < 1; i++) {
			Item item = arrayOfList.get(position);
			intent.putExtra("id", item.getId());
			intent.putExtra("name", item.getName());
			intent.putExtra("date", item.getUprocessedDate());
			intent.putExtra("startTime", item.getStartTime());
			intent.putExtra("endTime", item.getEndTime());
			intent.putExtra("location", item.getLocation());
			intent.putExtra("description", item.getDescription());

		}
		startActivity(intent);
	}

	public void setAdapterToListview() {

		objAdapter = new NewsRowAdapter(DataView.this,
				R.layout.schedulelistrow, arrayOfList);
		objAdapter.sortByNoteDesc();
		objAdapter.notifyDataSetChanged();
		listView.setAdapter(objAdapter);
	}

	public void showToast(String msg) {
		Toast.makeText(DataView.this, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent(DataView.this.getApplicationContext(),
					TabBarExample.class);
			intent.putExtra("goToTab", "EventsList");

			startActivity(intent);
			return true;
		}

		return super.onKeyDown(keyCode, event);
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
					

	
	 protected void onStop(){
	 super.onStop();
	 pDialog.dismiss();
	
	 }
	
	 protected void onDestroy(){
	 super.onDestroy();
	 pDialog.dismiss();
	
	 }

	@Override
	protected void onResume() {
//		 TODO Auto-generated method stub
		super.onPostResume();
		


	}
//


	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		setContentView(R.layout.schedulelistview);
//
//		listView = (ListView) findViewById(R.id.listview);
//		listView.setAdapter(null);
//		listView.setOnItemClickListener(this);
//
//
//
//		arrayOfList = new ArrayList<Item>();
//
//		if (Utils.isNetworkAvailable(DataView.this)) {
//			new MyTask().execute(rssFeed);
//		} else {
//			showToast("Internet connection is unavaiable. Application is unable to get data.");
//		}
	

	}
	
	
//	
}
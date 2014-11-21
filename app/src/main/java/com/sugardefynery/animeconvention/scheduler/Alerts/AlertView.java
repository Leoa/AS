package com.sugardefynery.animeconvention.scheduler.Alerts;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import com.sugardefynery.animeconvention.scheduler.DemoActivity;
import com.sugardefynery.animeconvention.scheduler.R;
import com.sugardefynery.animeconvention.scheduler.TabBarExample;
import com.sugardefynery.animeconvention.scheduler.Notifications.NotesView;
import com.sugardefynery.animeconvention.scheduler.sqllite.DatabaseSqlite;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AlertView extends Activity implements OnItemClickListener {

	ListView listView;
	AlertAdapter objAdapter;
	DatabaseSqlite db = new DatabaseSqlite(this);
	List<Alerts> listAlerts;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert);

		listView = (ListView) findViewById(R.id.alertlist);
		listView.setOnItemClickListener(this);		
		new MyTask().execute();
		
		
		

	}

	// My AsyncTask start...

	class MyTask extends AsyncTask<Void, Void, List<Alerts>> {

		ProgressDialog pDialog;
		


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
	

			pDialog = new ProgressDialog(AlertView.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
			
			if (isCancelled()) {

				this.cancel(true);
			}

		}


		@Override
		protected List<Alerts> doInBackground(Void... params) {

		
			db.open();
			listAlerts = db.getData();
			
			if (isCancelled()) {

				this.cancel(true);
			}

			
			return null;
		}
		
		protected void onPostExecute(List<Alerts> alerts) {
			
		

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}
			db.close();
			setAdapterToListview();

		}


	}

	@Override
	public void onItemClick(AdapterView<?> parent, View viewDel, int position,
			long id) {
		Alerts state = listAlerts.get(position);
		
		if(state.getEventState()==0){
		

		Intent intent = new Intent(AlertView.this.getApplicationContext(),
				AlertsDetails.class);

		for (int i = 0; i < 1; i++) {
			Alerts item = listAlerts.get(position);
			intent.putExtra("id", item.getRowId());
			intent.putExtra("remoteId", item.getRemoteServerId());
			intent.putExtra("eventState", item.getEventState());
			intent.putExtra("startTime", item.getStartTime());
			intent.putExtra("alertState", item.getAlertState());
			intent.putExtra("eventName", item.getEventName());
			intent.putExtra("location", item.getLocation());
			intent.putExtra("alertTime", item.getAlertTime());
			intent.putExtra("date", item.getDate());

		}
		startActivity(intent);
		
		}if(state.getEventState()==1){
			
			Toast.makeText(AlertView.this, "This event has been canceled", Toast.LENGTH_LONG).show();
			
			Intent intent = new Intent(AlertView.this.getApplicationContext(),
					AlertsDetails.class);

			for (int i = 0; i < 1; i++) {
				Alerts item = listAlerts.get(position);
				intent.putExtra("id", item.getRowId());			
				intent.putExtra("eventName", item.getEventName());
				intent.putExtra("location", item.getLocation());
				intent.putExtra("alertTime", item.getAlertTime());
				intent.putExtra("date", item.getDate());

			}
			startActivity(intent);
			
		}
	}

	public void setAdapterToListview() {

		objAdapter = new AlertAdapter(AlertView.this, R.layout.row, listAlerts);
		objAdapter.notifyDataSetChanged();
		listView.setAdapter(objAdapter);
	}

	public void showToast(String msg) {
		Toast.makeText(AlertView.this, msg, Toast.LENGTH_LONG).show();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    	Intent intent = new Intent(
					AlertView.this.getApplicationContext(),
					TabBarExample.class);
			intent.putExtra("goToTab","Alerts");
			startActivity(intent);
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
//		setContentView(R.layout.alert);
//
//		listView = (ListView) findViewById(R.id.alertlist);
//		listView.setAdapter(null);
//		listView.setOnItemClickListener(this);
//		new MyTask().execute();
	}

    @Override
    protected void onResume() {
    	
        super.onResume();
        
       
     
    }

    
    @Override
	protected void onDestroy() {
    	super.onDestroy();
   
    	
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_refresh, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
       
        case R.id.options_refresh:
    		setContentView(R.layout.alert);
        	
       			listView = (ListView) findViewById(R.id.alertlist);
        			listView.setAdapter(null);
        			listView.setOnItemClickListener(this);
        			new MyTask().execute();
        	
        	return true;
        	
        case R.id.options_exit:
			
			return true;
        default:
        return super.onOptionsItemSelected(item);
    }
 }
	
}
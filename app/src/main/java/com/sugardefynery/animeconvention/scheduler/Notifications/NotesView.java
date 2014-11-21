package com.sugardefynery.animeconvention.scheduler.Notifications;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


import com.sugardefynery.animeconvention.scheduler.DemoActivity;
import com.sugardefynery.animeconvention.scheduler.PreferenceConnector;
import com.sugardefynery.animeconvention.scheduler.R;
import com.sugardefynery.animeconvention.scheduler.TabBarExample;
import com.sugardefynery.animeconvention.scheduler.Alerts.AlertAdapter;
import com.sugardefynery.animeconvention.scheduler.Alerts.AlertView;
import com.sugardefynery.animeconvention.scheduler.Alerts.Alerts;
import com.sugardefynery.animeconvention.scheduler.Alerts.AlertsDetails;
import com.sugardefynery.animeconvention.scheduler.sqllite.DatabaseSqlite;
import com.sugardefynery.animeconvention.scheduler.sqllite.NotificationsDatabase;

public class NotesView extends Activity implements OnItemClickListener {

	ListView listView;
	NoteAdapter objAdapter;
	NotificationsDatabase db = new NotificationsDatabase(this);
	List<Notes> listAlerts;
	String note;
	String time;

	TextView noteView;
	TextView timeView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note);

		listView = (ListView) findViewById(R.id.notelist);
		listView.setOnItemClickListener(this);
		noteView = (TextView) findViewById(R.id.noteDisplay);
		timeView = (TextView) findViewById(R.id.notetimeStampDisplay);

		new MyTask().execute();

	}

	// My AsyncTask start...
	class MyTask extends AsyncTask<Void, Void, List<Notes>> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(NotesView.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();

			if (isCancelled()) {

				this.cancel(true);
			}

		}

		@Override
		protected List<Notes> doInBackground(Void... params) {

			db.open();
			listAlerts = db.getData();

			if (isCancelled()) {

				this.cancel(true);
			}

			return null;
		}

		protected void onPostExecute(List<Notes> alerts) {

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}
			db.close();
			setAdapterToListview();

		}

	}// end myTask

	public void setAdapterToListview() {

		objAdapter = new NoteAdapter(NotesView.this, R.layout.row_notes,
				listAlerts);
		objAdapter.sortByNoteDesc();
		objAdapter.notifyDataSetChanged();
		listView.setAdapter(objAdapter);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent(NotesView.this.getApplicationContext(),
					TabBarExample.class);
			intent.putExtra("goToTab", "Alerts");
			startActivity(intent);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public void onItemClick(AdapterView<?> parent, View viewDel, int position,
			long id) {

		for (int i = 0; i < 1; i++) {
			Notes item = listAlerts.get(position);
			int ids = item.getId();
			note = item.getNote();
			time = item.getTimeStamp();

		}

		System.out.println(note + "  " + time);
		PopupWindowAsync popWindowTask = new PopupWindowAsync();
		String th = "threading";
		popWindowTask.execute(th);
		//
	}

	private class PopupWindowAsync extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(1000);
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
			View popupView = layoutInflater.inflate(R.layout.popup_notes, null);

			int screenWidthpop = getWindowManager().getDefaultDisplay()
					.getWidth() - 100;
			int screenHeightpop = getWindowManager().getDefaultDisplay()
					.getHeight() - 500;

			final PopupWindow popupWindow = new PopupWindow(popupView,
					screenWidthpop, screenHeightpop, true);

			((TextView) popupWindow.getContentView().findViewById(
					R.id.notetimeStampDisplay)).setText(time);

			((TextView) popupWindow.getContentView().findViewById(
					R.id.noteDisplay)).setText(note);
			// noteView.setText(note);
			// timeView.setText(time);
			//

			Button btnDismiss = (Button) popupView
					.findViewById(R.id.btnDismiss);
			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {

					// TODO Auto-generated method stub

					popupWindow.dismiss();
				}
			});

			popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
		}

	}// end pop

	 @Override
	 protected void onPause() {
	 super.onPause();
	
//	 setContentView(R.layout.note);
//	
//	 listView = (ListView) findViewById(android.R.id.list);
//	 listView.setAdapter(null);
//	 listView.setOnItemClickListener(this);
//	 noteView=(TextView)findViewById(R.id.noteDisplay);
//	 timeView=(TextView)findViewById(R.id.notetimeStampDisplay);
//	 new MyTask().execute();
//	
	
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
			listView.setAdapter(null);
			listView.setOnItemClickListener(NotesView.this);
			new MyTask().execute();

			return true;

		case R.id.options_exit:
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	



}

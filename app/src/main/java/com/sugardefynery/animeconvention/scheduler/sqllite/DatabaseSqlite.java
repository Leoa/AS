package com.sugardefynery.animeconvention.scheduler.sqllite;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.json.JSONObject;

import com.sugardefynery.animeconvention.scheduler.R;
import com.sugardefynery.animeconvention.scheduler.Alerts.AlertAdapter;
import com.sugardefynery.animeconvention.scheduler.Alerts.Alerts;
import com.sugardefynery.animeconvention.scheduler.EventList.Item;
import com.sugardefynery.animeconvention.scheduler.EventList.NewsRowAdapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ListView;

public class DatabaseSqlite extends Activity {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_EVENTNAME = "event_name";
	public static final String KEY_EVENTSTARTTIME = "event_start_time";
	public static final String KEY_EVENTALERTTIME = "event_alert_time";
	public static final String KEY_EVENTALERTSTATE = "event_alert_state";
	public static final String KEY_EVENTSTATE = "event_state";
	public static final String KEY_IDFROMREMOTESERVER = "event_id";
	public static final String KEY_LOCATION = "event_location";
	public static final String KEY_DATE = "date";
	private static final String KEY_ORIGINALALALERTDATE = "original_alert_date";
	private static final String KEY_ALERTMINUTES = "alert_in_minutes";
	private static final String KEY_ALERTINMILLIS = "alert_in_millis";
	private static final String KEY_ALERTDISMISSED = "alert_dismissed";

	// database name:
	private static final String DATABASE_NAME = "Reminders_DB";
	private static final String DATABASE_TABLE = "Alerts";
	private static final int DATABASE_VERSION = 1;

	private DBHelper dbHelper;
	private final Context context;
	private SQLiteDatabase database;
	Cursor cursor = null;

	ArrayList<String> alertList = null;
	List<Alerts> alertsOfList;
	ListView listView;
	AlertAdapter objAdapter;

	// JSONObject json;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	}

	private static class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ KEY_IDFROMREMOTESERVER + " INTEGER, " + KEY_EVENTNAME
					+ " TEXT, " + KEY_EVENTSTARTTIME + " TEXT, "
					+ KEY_EVENTSTATE + " INTEGER," + KEY_ALERTDISMISSED + " INTEGER," + KEY_LOCATION + " TEXT, "
					+ KEY_DATE + " TEXT, " + KEY_EVENTALERTTIME + " TEXT, "
					+ KEY_ALERTMINUTES + " TEXT, " + KEY_ALERTINMILLIS
					+ " TEXT, " + KEY_ORIGINALALALERTDATE + " TEXT, "
					+ KEY_EVENTALERTSTATE + " INTEGER);"

			);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
			System.out.println("DROP TABLE IF EXISTS ");
		}

	}

	public DatabaseSqlite(Context c) {
		context = c;
	}

	public synchronized DatabaseSqlite open() throws SQLException {
		dbHelper = new DBHelper(context);
		database = dbHelper.getWritableDatabase();
		
		return this;
	}

	public synchronized void close() {
		try {
			if (database != null && database.isOpen()) {

				dbHelper.close();
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public long createEntry(int idFromRemoteServer, String eventName,
			String startTime, String alertTime, int alertState,
			String location, int eventState, String date, String alertInMillis, String minutes, int alertDismissedState) {

		ContentValues cv = new ContentValues();
		cv.put(KEY_IDFROMREMOTESERVER, idFromRemoteServer);
		cv.put(KEY_EVENTNAME, eventName);
		cv.put(KEY_EVENTSTARTTIME, startTime);
		cv.put(KEY_EVENTALERTTIME, alertTime);
		cv.put(KEY_EVENTSTATE, eventState);
		cv.put(KEY_EVENTALERTSTATE, alertState);
		cv.put(KEY_LOCATION, location);
		cv.put(KEY_DATE, date);
		cv.put(KEY_ALERTINMILLIS, alertInMillis);
		cv.put(KEY_ALERTMINUTES, minutes);
		cv.put(KEY_ALERTDISMISSED, alertDismissedState);
		
		return database.insert(DATABASE_TABLE, null, cv);

	}

	
	
	public int getHighestID() {
		final String MY_QUERY = "SELECT MAX(_id) FROM " + DATABASE_TABLE;
		Cursor cur = database.rawQuery(MY_QUERY, null);
		cur.moveToFirst();
		int ID = cur.getInt(0);
		cur.close();
		return ID;
		}   
	
	
	public List<Alerts> getData() {

		
		alertsOfList = new ArrayList<Alerts>();

		try {

			String[] columns = new String[] { KEY_ROWID,
					KEY_IDFROMREMOTESERVER, KEY_EVENTNAME, KEY_EVENTSTARTTIME,
					KEY_EVENTALERTTIME, KEY_EVENTALERTSTATE, KEY_LOCATION,
					KEY_EVENTSTATE, KEY_DATE ,KEY_ALERTMINUTES};
			// information from a database is read thru a cursor

			cursor = database.query(DATABASE_TABLE, columns, null, null, null,
					null, null);

			int iRow = cursor.getColumnIndex(KEY_ROWID);
			int iIdRemote = cursor.getColumnIndex(KEY_IDFROMREMOTESERVER);
			int iEventState = cursor.getColumnIndex(KEY_EVENTSTATE);
			int iName = cursor.getColumnIndex(KEY_EVENTNAME);
			int iStartTime = cursor.getColumnIndex(KEY_EVENTSTARTTIME);
			int iAlertTime = cursor.getColumnIndex(KEY_EVENTALERTTIME);
			int iAlertState = cursor.getColumnIndex(KEY_EVENTALERTSTATE);
			int iLocation = cursor.getColumnIndex(KEY_LOCATION);
			int iDate = cursor.getColumnIndex(KEY_DATE);
			int iMinutes = cursor.getColumnIndex(KEY_ALERTMINUTES);

			if (cursor != null) {

				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {

					Alerts alertObj = new Alerts();

					alertObj.setRowId(cursor.getInt(iRow));
					alertObj.setRemoteServerId(cursor.getInt(iIdRemote));
					alertObj.setEventState(cursor.getInt(iEventState));
					alertObj.setAlertState(cursor.getInt(iAlertState));
					alertObj.setEventName(cursor.getString(iName));
					alertObj.setLocation(cursor.getString(iLocation));
					alertObj.setStartTime(cursor.getString(iStartTime));
					alertObj.setAlertTime(cursor.getString(iAlertTime));
					alertObj.setDate(cursor.getString(iDate));
					alertObj.setAlertMinutes(cursor.getString(iMinutes));

					String result = cursor.getString(iName);
					
					alertsOfList.add(alertObj);

				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			alertsOfList = null;

		} finally {

			if (cursor != null && !cursor.isClosed()) {

				cursor.close();

			}

		}

		return alertsOfList;
	}

	public String getStartTime(long lRow) {

		String result = "";
		String row= KEY_ROWID + "=" + lRow;

		try {

			String[] columns = new String[] {
					KEY_EVENTSTARTTIME };
			
			
			cursor = database.query(DATABASE_TABLE, columns, row, null, null,
					null, null);

			int iStartTime = cursor.getColumnIndex(KEY_EVENTSTARTTIME);

			if (cursor != null) {
				cursor.moveToFirst();
				result = cursor.getString(iStartTime);
				

			}
		} catch (Exception e) {

			e.printStackTrace();
			// alertsOfList = null;

		} finally {

			if (cursor != null && !cursor.isClosed()) {

				cursor.close();

			}

		}

		return result;

	}
	
	
	


	public String getMinutesForOneShot( long lRow){
		
		String x="";
		
		try{
		
		String[]columns= new String[]{KEY_ALERTMINUTES};		
		cursor=database.query(DATABASE_TABLE, columns, KEY_ROWID+"="+lRow, null, null, null, null);		
		int index= cursor.getColumnIndex(KEY_ALERTMINUTES);
		
		if(cursor.moveToFirst()){
			do{
				
				x=cursor.getString(index);
				
			}while(cursor.moveToNext());
		}
		
		}catch(Exception e){
			
			e.printStackTrace();
		}
		
			finally{
				if (cursor!= null && !cursor.isClosed()) {

					cursor.close();

				}
		
		}
		
		
		
		return x;
		
	
	}
	
	/////////

	public String getEventDate(long lRow) {

		String result = "";
		String row= KEY_ROWID + "=" + lRow;

		try {

			String[] columns = new String[] {
					KEY_DATE };
			
			
			cursor = database.query(DATABASE_TABLE, columns, row, null, null,
					null, null);

			int iDate = cursor.getColumnIndex(KEY_DATE);

			if (cursor != null) {
				cursor.moveToFirst();
				result = cursor.getString(iDate);
			

			}
		} catch (Exception e) {

			e.printStackTrace();
			// alertsOfList = null;

		} finally {

			if (cursor != null && !cursor.isClosed()) {

				cursor.close();

			}

		}
	
		return result;

	}

	public String getAlertTime(long lRow) {

		String result = "";
		String row= KEY_ROWID + "=" + lRow;

		try {

			String[] columns = new String[] {
					KEY_EVENTALERTTIME };
			
			
			cursor = database.query(DATABASE_TABLE, columns, row, null, null,
					null, null);

			int iAlertTime = cursor.getColumnIndex(KEY_EVENTALERTTIME);

			if (cursor != null) {
				cursor.moveToFirst();
				result = cursor.getString(iAlertTime);
				
			}
		} catch (Exception e) {

			e.printStackTrace();
			// alertsOfList = null;

		} finally {

			if (cursor != null && !cursor.isClosed()) {

				cursor.close();

			}

		}
	

		return result;

	}
	
	
	

	public int getAlertState(long lRow) {

		int result = 0;
	//	String row= "WHERE _id ="+KEY_ROWID + "=" + lRow;
		
		String row= KEY_ROWID + "=" + lRow;

		try {

			String[] columns = new String[] {
					KEY_EVENTALERTSTATE };

			// information from a database is read thru a cursor

			cursor = database.query(DATABASE_TABLE, columns, row, null, null,
					null, null);
			
			int iRow = cursor.getColumnIndex(KEY_ROWID);

			int iAlertState = cursor.getColumnIndex(KEY_EVENTALERTSTATE);

		
			
			if (cursor != null) {

				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {

					
					result = result+ cursor.getInt(iAlertState);
					
					
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			// alertsOfList = null;

		} finally {

			if (cursor != null && !cursor.isClosed()) {

				cursor.close();

			}

		}

		return result;
	}

	// ////////////
	public List<Alerts> getAlertsforService() {

	
		alertsOfList = new ArrayList<Alerts>();

		try {

			String[] columns = new String[] { KEY_ROWID, KEY_EVENTNAME,
					KEY_EVENTSTARTTIME, KEY_EVENTALERTTIME,
					KEY_EVENTALERTSTATE, KEY_LOCATION, KEY_EVENTSTATE,
					KEY_DATE, KEY_ORIGINALALALERTDATE, KEY_ALERTMINUTES,
					KEY_ALERTINMILLIS,KEY_ALERTDISMISSED };
			// information from a database is read thru a cursor
			cursor = database.query(DATABASE_TABLE, columns, null, null, null,
					null, null);

			int iRow = cursor.getColumnIndex(KEY_ROWID);
			// int iIdRemote = cursor.getColumnIndex(KEY_IDFROMREMOTESERVER);
			int iEventState = cursor.getColumnIndex(KEY_EVENTSTATE);
			int iName = cursor.getColumnIndex(KEY_EVENTNAME);
			int iStartTime = cursor.getColumnIndex(KEY_EVENTSTARTTIME);
			int iAlertTime = cursor.getColumnIndex(KEY_EVENTALERTTIME);
			int iAlertState = cursor.getColumnIndex(KEY_EVENTALERTSTATE);
			int iLocation = cursor.getColumnIndex(KEY_LOCATION);
			int iDate = cursor.getColumnIndex(KEY_DATE);
			int iOrigAlertDate = cursor.getColumnIndex(KEY_ORIGINALALALERTDATE);
			int iMinutes = cursor.getColumnIndex(KEY_ALERTMINUTES);
			int iAlertInMills = cursor.getColumnIndex(KEY_ALERTINMILLIS);
			int iAlertDismissed= cursor.getColumnIndex(KEY_ALERTDISMISSED);

			if (cursor != null) {

				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					if (cursor.getInt(iAlertState) == 0
							&& cursor.getString(iAlertTime) != "" && cursor.getInt(iAlertDismissed)==0) {
						
						Alerts alertObj = new Alerts();
					
						alertObj.setOrgAlertDate(cursor
								.getString(iOrigAlertDate));
						alertObj.setAlertMinutes(cursor.getString(iMinutes));
						alertObj.setEventState(cursor.getInt(iEventState));
						alertObj.setRowId(cursor.getInt(iRow));
						alertObj.setAlertInMillis(cursor
								.getString(iAlertInMills));
						alertObj.setEventState(cursor.getInt(iEventState));
						alertObj.setAlertState(cursor.getInt(iAlertState));
						alertObj.setEventName(cursor.getString(iName));
						alertObj.setLocation(cursor.getString(iLocation));
						alertObj.setStartTime(cursor.getString(iStartTime));
						alertObj.setAlertTime(cursor.getString(iAlertTime));
						alertObj.setDate(cursor.getString(iDate));

						alertsOfList.add(alertObj);

					}
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			alertsOfList = null;

		} finally {

			if (cursor != null && !cursor.isClosed()) {

				cursor.close();

			}

		}

		return alertsOfList;
	}


public int getEventStateForService( long lRow){
		
		int x=0;
		
		try{
		
		String[]columns= new String[]{KEY_EVENTSTATE};		
		cursor=database.query(DATABASE_TABLE, columns, KEY_ROWID+"="+lRow, null, null, null, null);		
		int index= cursor.getColumnIndex(KEY_EVENTSTATE);
		
		if(cursor.moveToFirst()){
			do{
				
				x=cursor.getInt(index);
				
			}while(cursor.moveToNext());
		}
		
		}catch(Exception e){
			
			e.printStackTrace();
		}
		
			finally{
				if (cursor!= null && !cursor.isClosed()) {

					cursor.close();

				}
		
		}
		
		
		
		return x;
		
	
	}
	
	

	public void updateEventState(long lRow, int eventState) {
		// is Event running or canceled

		ContentValues updateCV = new ContentValues();
		updateCV.put(KEY_EVENTSTATE, eventState);
		database.update(DATABASE_TABLE, updateCV, KEY_ROWID + "=" + lRow, null);

	}

	public void updateEventStartTime(long lRow, int eventStartTime) {

		ContentValues updateCV = new ContentValues();
		updateCV.put(KEY_EVENTSTARTTIME, eventStartTime);
		database.update(DATABASE_TABLE, updateCV, KEY_ROWID + "=" + lRow, null);

	}

	public void updateAlertState(long lRow, int alertState) {


		ContentValues updateCV = new ContentValues();
		updateCV.put(KEY_EVENTALERTSTATE, alertState);
		database.update(DATABASE_TABLE, updateCV, KEY_ROWID + "=" + lRow, null);

	}
	

	public void updateAlertDismissedState(long lRow, int alertDismissed) {

		ContentValues updateCV = new ContentValues();
		updateCV.put(KEY_ALERTDISMISSED, alertDismissed);
		
		
		database.update(DATABASE_TABLE, updateCV, KEY_ROWID + "=" + lRow, null);

	}

	// public void updateEventAlertTime(long lRow, String updateAlertTime) {
	// // time update from notification
	// System.out.println("----updateEventAlert time is "+updateAlertTime);
	// ContentValues updateCV = new ContentValues();
	// updateCV.put(KEY_EVENTALERTTIME, updateAlertTime);
	// database.update(DATABASE_TABLE, updateCV, KEY_ROWID + "=" + lRow, null);
	//
	//
	// }

	public void updateAlertTime(long lRow, String updateAlertTime) {
		// time update from notification

		ContentValues updateCV = new ContentValues();
		updateCV.put(KEY_EVENTALERTTIME, updateAlertTime);
		database.update(DATABASE_TABLE, updateCV, KEY_ROWID + "=" + lRow, null);

	}

	public void updateEventLocation(long lRow, String location) {
		// time update from notification
		ContentValues updateCV = new ContentValues();
		updateCV.put(KEY_LOCATION, location);
		database.update(DATABASE_TABLE, updateCV, KEY_ROWID + "=" + lRow, null);

	}

	public void updateEventState(long lRow, String state) {
		// time update from notification
		ContentValues updateCV = new ContentValues();
		updateCV.put(KEY_EVENTSTATE, state);
		database.update(DATABASE_TABLE, updateCV, KEY_ROWID + "=" + lRow, null);

	}

	public void updateDate(long lRow, String state) {
		// time update from notification
		ContentValues updateCV = new ContentValues();
		updateCV.put(KEY_DATE, state);
		database.update(DATABASE_TABLE, updateCV, KEY_ROWID + "=" + lRow, null);

	}

	public void updateAlertInDB(long lRow, String date, String alertInMillis, String minutes, String alertTime) {
		ContentValues updateCV = new ContentValues();
		updateCV.put(KEY_ORIGINALALALERTDATE, date);
		updateCV.put(KEY_EVENTALERTTIME, alertTime);
		updateCV.put(KEY_ALERTMINUTES, minutes);
		updateCV.put(KEY_ALERTINMILLIS, alertInMillis);

		database.update(DATABASE_TABLE, updateCV, KEY_ROWID + "=" + lRow, null);

	}

	public void updateAlertFromMember(long lRow, String date, String alertInMillis, String minutes, String alertTime, String startTime, String location, int newEventState){
		ContentValues updateCV = new ContentValues();
		updateCV.put(KEY_ORIGINALALALERTDATE, date);
		updateCV.put(KEY_ALERTINMILLIS, alertInMillis);
		updateCV.put(KEY_ALERTMINUTES, minutes);
		updateCV.put(KEY_EVENTALERTTIME, alertTime);
		updateCV.put(KEY_EVENTSTARTTIME, startTime);
		updateCV.put(KEY_LOCATION, location);
		updateCV.put(KEY_EVENTSTATE, newEventState);
		
		
		database.update(DATABASE_TABLE, updateCV, KEY_ROWID + "=" + lRow, null);
		
		
	}
	
	public void deleteEntry(long lRow1) {
		// TODO Auto-generated method stub

		database.delete(DATABASE_TABLE, KEY_ROWID + "=" + lRow1, null);

	}

	

	
}

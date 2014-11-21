package com.sugardefynery.animeconvention.scheduler.Alerts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmsBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = "BootReceiver";



	@Override
	public void onReceive(Context context, Intent intent) {

		
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) 
        {
//		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

		Intent	startServiceIntent = new Intent(context, AlarmsService.class);
			  context.startService(startServiceIntent);
		       
		

		}
		
	

	}

}

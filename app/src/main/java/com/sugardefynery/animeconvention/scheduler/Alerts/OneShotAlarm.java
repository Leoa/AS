package com.sugardefynery.animeconvention.scheduler.Alerts;

import com.sugardefynery.animeconvention.scheduler.R;
import com.sugardefynery.animeconvention.scheduler.R.string;
import com.sugardefynery.animeconvention.scheduler.sqllite.DatabaseSqlite;

import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.widget.Toast;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.


/**
 * This is an example of implement an {@link BroadcastReceiver} for an alarm that
 * should occur once.
 * <p>
 * When the alarm goes off, we show a <i>Toast</i>, a quick message.
 */
public class OneShotAlarm extends BroadcastReceiver
{
	Bundle bundle;
	Context c;
	static final int DIALOG_CANCEL_ID = 0;
	
	
    @Override
    public void onReceive(Context context, Intent intent)
    {
    	
    	bundle =intent.getExtras();
      
        
//       String extra = intent.getStringExtra("name");
//       String event = intent.getStringExtra("text");
//       String id = intent.getStringExtra("id");
       
      String name = bundle.getString("name");
      String text = bundle.getString("text");
      String id = bundle.getString("id");
      
    
       //start activity
       Intent i = new Intent();
       i.addCategory(i.CATEGORY_LAUNCHER);
       i.putExtra("text", text);
       i.putExtra("name", name);
       i.putExtra("id", id);
       i.setClassName("com.sugardefynery.animeconvention.scheduler", "com.sugardefynery.animeconvention.scheduler.Alerts.AlertDialogActivity");
       i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     
       context.startActivity(i);

   
       
       
        
        
    }
}

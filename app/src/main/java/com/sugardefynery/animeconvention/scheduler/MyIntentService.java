package com.sugardefynery.animeconvention.scheduler;


import com.sugardefynery.animeconvention.scheduler.Notifications.NotesView;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MyIntentService extends IntentService{
	 
    public MyIntentService() {
        super("MyIntentService");
        // TODO Auto-generated constructor stub
        System.out.println("sohail,  MyIntentService: Constructor");
        Log.d("sohail","Myintentservice constructor");
    }
 
    @Override
    protected void onHandleIntent(Intent arg0) {
        // TODO Auto-generated method stub
        System.out.println("sohail, MyIntentService: Handle intent");
        Log.d("sohail", "onHandle intent called");
         
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         
        Intent in= new Intent();
        in.setAction("sohail.aziz");
//        
       NotesView nv= new NotesView();
        Log.d("sohail", "onHandleIntent: sending broadcast");
        nv.sendBroadcast(in);
      
     
    }

	
 
}
package com.sugardefynery.animeconvention.scheduler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.sugardefynery.animeconvention.scheduler.R;
import com.sugardefynery.animeconvention.scheduler.Alerts.AlertView;
import com.sugardefynery.animeconvention.scheduler.EventList.DataView;



public class EventsView extends Activity {

	private WebView webView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.graphscheduleview);
		

		if (AppStatus.getInstance(EventsView.this).isOnline(EventsView.this)) {

			

		webView = (WebView) findViewById(R.id.webView1);
		
		
		webView.getSettings().setJavaScriptEnabled(true);
		
		
		webView.getSettings().setBuiltInZoomControls(true);
		// simply, just load an image						
		
		webView.getSettings().setAppCacheEnabled(false);
		
		webView.setInitialScale(70);

		
		final Activity activity = this;
		webView.setWebChromeClient(new WebChromeClient() {
		   public void onProgressChanged(WebView view, int progress) {
		     // Activities and WebViews measure progress with different scales.
		     // The progress meter will automatically disappear when we reach 100%
		     activity.setProgress(progress * 1000);
		   }
		 });
		webView.setWebViewClient(new WebViewClient() {
		   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		     Toast.makeText(activity, "Unable to load Schedule " + description, Toast.LENGTH_SHORT).show();
		   }
		 });
		
		webView.loadUrl("http://www.txtease.com/android/push/login/schedule/mbielan/index.html");
 
		

		} else {
			
			
			Toast.makeText(
					this,
					"Internet connection is unavaiable. Application is unable to get data.",
					Toast.LENGTH_LONG).show();
			Log.v("Home", "Internet connection is unavaiable");
			

		  	}

		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    	Intent intent = new Intent(
					EventsView.this.getApplicationContext(),
					TabBarExample.class);
			startActivity(intent);
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	
	
 
}
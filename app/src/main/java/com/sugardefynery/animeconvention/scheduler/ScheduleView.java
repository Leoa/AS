package com.sugardefynery.animeconvention.scheduler;


import com.sugardefynery.animeconvention.scheduler.R;
import com.sugardefynery.animeconvention.scheduler.Alerts.AlertView;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ZoomControls;

public class ScheduleView extends Activity {
	private static final String TAG = "Chart";
	private ProgressDialog progressBar; 
	private AlertDialog alertDialog;
	private static final String LOG_TAG = "WebViewDemo";
	private WebView mWebView=null;
	private Handler mHandler = new Handler();

	    @Override
	    public void onCreate(Bundle icicle) {
	        super.onCreate(icicle);
	        
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.graphscheduleview);
	        mWebView = (WebView) findViewById(R.id.webView1);
	        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);	        
	        mWebView.setWebChromeClient(new MyWebChromeClient());
 ZoomControls zc=(ZoomControls)findViewById(R.id.zoomControls1);
	        
	        ZoomControls zoomControls = (ZoomControls) findViewById(R.id.zoomControls1);
	        zoomControls.setOnZoomInClickListener(new OnClickListener() {
	                public void onClick(View v) {
	                	mWebView.zoomIn();
	                }
	        });
	        zoomControls.setOnZoomOutClickListener(new OnClickListener() {
	                public void onClick(View v) {
	                	mWebView.zoomOut();
	                }
	        });

	        
	        
	        
	        WebSettings webSettings = mWebView.getSettings();
	        webSettings.setSavePassword(false);
	        webSettings.setSaveFormData(false);
	        webSettings.setJavaScriptEnabled(true);
	        webSettings.setSupportZoom(false);
	        webSettings.setBuiltInZoomControls(true);			
	        webSettings.setAppCacheEnabled(false);
	        

	       alertDialog = new AlertDialog.Builder(this).create();
	       progressBar = ProgressDialog.show( ScheduleView.this, "Anime Convention Schedule", "Loading...");
    
			if (AppStatus.getInstance( ScheduleView.this).isOnline( ScheduleView.this)) {		
			 mWebView.loadUrl("http://www.txtease.com/android/push/login/schedule/mbielan/index.html");
	    
			}
			else {								
				Toast.makeText(this,"Internet connection is unavaiable. Application is unable to get data.",Toast.LENGTH_LONG).show();				

			  	}

			 mWebView.setWebViewClient(new WebViewClient() {

				   public void onPageFinished(WebView view, String url) {
					   
		                if (progressBar.isShowing()) {
		                    progressBar.dismiss();
		                }
				    }
				});

		
			 
			 
	    }

	    final class DemoJavaScriptInterface {

	        DemoJavaScriptInterface() {
	        }

	        /**
	         * This is not called on the UI thread. Post a runnable to 

invoke
	         * loadUrl on the UI thread.
	         */
	        public void clickOnAndroid() {
	            mHandler.post(new Runnable() {
	                public void run() {
	                    mWebView.loadUrl

("http://www.txtease.com/android/push/login/schedule/mbielan/index.html");
	                }
	            });

	        }
	        
	        
	    }

	    /**
	     * Provides a hook for calling "alert" from j avascript. Useful 

for
	     * debugging your javascript.
	     */
	    final class MyWebChromeClient extends WebChromeClient {
	        @Override
	        public boolean onJsAlert(WebView view, String url, String 

message, JsResult result) {
	           
	            result.confirm();
	            return true;
	        }
	        
	        public boolean shouldOverrideUrlLoading(WebView view, 

String url) {
             
              view.loadUrl(url);
              return true;
          }

          public void onPageFinished(WebView view, String url) {
            
              if (progressBar.isShowing()) {
                  progressBar.dismiss();
              }
          }
          
          

          public void onReceivedError(WebView view, int errorCode, String 

description, String failingUrl) {
            
              
              Toast.makeText( ScheduleView.this, "Oh no! " + description, 

Toast.LENGTH_SHORT).show();
              
              alertDialog.setTitle("Error");
              
              alertDialog.setMessage(description);
              
              alertDialog.setButton("OK", new 

DialogInterface.OnClickListener() {
              	
              	
                  public void onClick(DialogInterface dialog, int which) 

{
                      return;
                  }
                  
                  
              });
              alertDialog.show();
          }
      
     // webview.loadUrl("http://www.google.com");
          String 

urlis="http://www.txtease.com/android/push/login/schedule/mbielan/index.html";
         
          
	        
	    }   // end of chrome web view function



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
	        	
	        	Intent i = new Intent(this, TabBarExample.class);
	        	i.putExtra("name", "name");
	        	startActivity(i);
	  	 
	        	return true;
	        default:
	        return super.onOptionsItemSelected(item);
	    }
	 }


	    @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				Intent intent = new Intent(
						ScheduleView.this.getApplicationContext(),
						TabBarExample.class);
				startActivity(intent);
				return true;
			}

			return super.onKeyDown(keyCode, event);
		}





}
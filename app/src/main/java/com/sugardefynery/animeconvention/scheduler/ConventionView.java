package com.sugardefynery.animeconvention.scheduler;

import com.sugardefynery.animeconvention.scheduler.R;
import com.sugardefynery.animeconvention.scheduler.ScheduleView.MyWebChromeClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.widget.ZoomControls;

public class ConventionView extends Activity{
	
	private int mProgressStatus = 0;
	WebView webView = null;
	private ProgressDialog progressBar; 
	private AlertDialog alertDialog;
	private static final String LOG_TAG = "WebViewDemo";
	
	private Handler mHandler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conventionview);
		
//		Task_News_ArticleView tna= new Task_News_ArticleView();
//		tna.execute();

			webView = (WebView) findViewById(R.id.webView);

			webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);	        
			webView.setWebChromeClient(new MyWebChromeClient());
	 ZoomControls zc=(ZoomControls)findViewById(R.id.zoomControls1);
		        
		        ZoomControls zoomControls = (ZoomControls) findViewById(R.id.zoomControls1);
		        zoomControls.setOnZoomInClickListener(new OnClickListener() {
		                public void onClick(View v) {
		                	webView.zoomIn();
		                }
		        });
		        zoomControls.setOnZoomOutClickListener(new OnClickListener() {
		                public void onClick(View v) {
		                	webView.zoomOut();
		                }
		        });

		        
		        
		        
		        WebSettings webSettings = webView.getSettings();
		        webSettings.setSavePassword(false);
		        webSettings.setSaveFormData(false);
		        webSettings.setJavaScriptEnabled(true);
		        webSettings.setSupportZoom(false);
		        webSettings.setBuiltInZoomControls(true);			
		        webSettings.setAppCacheEnabled(false);
		        webView.setInitialScale(70);
		        

		       alertDialog = new AlertDialog.Builder(this).create();
		       progressBar = ProgressDialog.show( ConventionView.this, "Anime Convention Schedule", "Loading...");
	    
				if (AppStatus.getInstance( ConventionView.this).isOnline( ConventionView.this)) {		
				 webView.loadUrl("http://www.txtease.com/android/push/login/schedule/conventionlayout.jpg");

		    
				}
				else {								
					Toast.makeText(this,"Internet connection is unavaiable. Application is unable to get data.",Toast.LENGTH_LONG).show();				

				  	}

				 webView.setWebViewClient(new WebViewClient() {

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
		                    webView.loadUrl

	("http://www.txtease.com/android/push/login/schedule/conventionlayout.jpg");
		                    
		                    //loadUrl("");

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
	            
	              
	              Toast.makeText( ConventionView.this, "Oh no! " + description, 

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
							ConventionView.this.getApplicationContext(),
							TabBarExample.class);
					startActivity(intent);
					return true;
				}

				return super.onKeyDown(keyCode, event);
			}




}

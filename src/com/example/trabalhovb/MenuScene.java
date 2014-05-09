package com.example.trabalhovb;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.gamelogic.MusicArray;
import com.example.utils.GameXMLHandler;
 
public class MenuScene extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.activity_fade_open,R.anim.activity_fade_close);
		setContentView(R.layout.activity_menu_scene);
		TextView menuLabel = (TextView) findViewById(R.id.menuLabel);
		Typeface t = Typeface.createFromAsset(getAssets(), "fonts/BoogieNightsShadowNF.ttf");
		menuLabel.setTypeface(t);
		menuLabel.setTextSize(80);
		Button startGameBtn = (Button) findViewById(R.id.startGameBtn);
		startGameBtn.setOnClickListener(new OnClickListener()
	    {
	      public void onClick(View v)
	      {
//	    	  startGame();
	    	  final ProgressDialog progress;
	    	  progress = ProgressDialog.show(v.getContext(), "Loading Musics","Please Wait", true);
	    	  new Thread(new Runnable() {
	    		  @Override
	    		  public void run()
	    		  {
	    		    // do the thing that takes a long time
	    			  MusicArray.resetMap();
	    			  getGameLevel();
	    		    runOnUiThread(new Runnable() {
	    		      @Override
	    		      public void run()
	    		      {
	    		        progress.dismiss();
	    		      }
	    		    });
	    		  }
	    		}).start();
	      }
	    });
		
	}
	
	public void startGame(){
		ConnectivityManager connMgr = (ConnectivityManager) 
		        getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		    if (networkInfo != null && networkInfo.isConnected()) {
		        // fetch data
		    	//new DownloadWebpageTask().execute("http://192.168.1.9:8080/axis2/services/author/getPackages");
		    	String url = "http://192.168.1.9:8080/axis2/services/author/getPackages";
		    	DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
		    	request.setDescription("Some descrition");
		    	request.setTitle("Some title");
		    	// in order for this if to run, you must use the android 3.2 to compile your app
		    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		    	    request.allowScanningByMediaScanner();
		    	    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		    	}
		    	request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "name-of-the-file.ext");
		    	
		    	System.out.println(Environment.DIRECTORY_DOWNLOADS);

		    	// get download service and enqueue file
		    	DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
		    	manager.enqueue(request);
		    } else {
		    	// 1. Instantiate an AlertDialog.Builder with its constructor
				Builder alertDialog = new AlertDialog.Builder(MenuScene.this);
				// 2. Chain together various setter methods to set the dialog characteristics
				alertDialog.setMessage("No Internet Connection Available")//R.string.dialog_message)
				       .setTitle("Error");//R.string.dialog_title);
				// 3. Get the AlertDialog from create()
				AlertDialog dialog = alertDialog.create();
				dialog.show();
		    }
	}
	
	private String downloadUrl(String myurl) throws IOException {
	    InputStream is = null;
	    // Only display the first 500 characters of the retrieved
	    // web page content.
	    int len = 500;
	        
	    try {
	        URL url = new URL(myurl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(15000 /* milliseconds */);
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        // Starts the query
	        conn.connect();
	        is = conn.getInputStream();

	        // Convert the InputStream into a string
	        String contentAsString = readIt(is, len);
	        return contentAsString;
	        
	    // Makes sure that the InputStream is closed after the app is
	    // finished using it.
	    } finally {
	        if (is != null) {
	            is.close();
	        } 
	    }
	}
	
	public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
	    Reader reader = null;
	    reader = new InputStreamReader(stream, "UTF-8");        
	    char[] buffer = new char[len];
	    reader.read(buffer);
	    return new String(buffer);
	}
	
	public void getGameLevel(){
		File folder = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.example.trabalhovb");
    	if (!folder.isDirectory()) {
    	    folder.mkdir();
    	    MusicArray.loadMusicOnDevice(getAssets());
    	    
    	}
    	else {
    		GameXMLHandler.readMusicFromXML();
    	}
    	
    	Intent intent = new Intent(MenuScene.this, SelectPackageScene.class);
    	startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_scene, menu);
		return true;
	}
	
	
	
	private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
              
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (Exception e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        	System.out.println("SAIDA DO SITE "  + result);
        	if (result.equals("Unable to retrieve web page. URL may be invalid.")){
        		// 1. Instantiate an AlertDialog.Builder with its constructor 
				Builder alertDialog = new AlertDialog.Builder(MenuScene.this);
				// 2. Chain together various setter methods to set the dialog characteristics
				alertDialog.setMessage("No Internet Connection Available")//R.string.dialog_message)
				       .setTitle("Error");//R.string.dialog_title);
				// 3. Get the AlertDialog from create()
				AlertDialog dialog = alertDialog.create();
				dialog.show();
        	}
        	else{
//	        	MusicArray.savePackage(result.split("\\]")[0].split("\\[")[1].split(","));
	        	File folder = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.example.trabalhovb");
	        	if (!folder.isDirectory()) {
	        	    folder.mkdir();
	        	    MusicArray.loadMusicOnDevice(getAssets());
	        	}
	        	else {
	        		GameXMLHandler.readMusicFromXML();
	        	}
	        	getGameLevel();
	        	Intent intent = new Intent(MenuScene.this, SelectPackageScene.class);
	        	startActivity(intent);
        		}
        }
	}
}

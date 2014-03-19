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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.gamelogic.GameLogic;
import com.example.gamelogic.MusicArray;
import com.example.utils.GameXMLHandler;
 
public class MenuScene extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.activity_fade_open,R.anim.activity_fade_close);
		setContentView(R.layout.activity_menu_scene);
		Button startGameBtn = (Button) findViewById(R.id.startGameBtn);
		startGameBtn.setOnClickListener(new OnClickListener()
	    {
	      public void onClick(View v)
	      {
	    	  teste();
	    	  //startGame(v);
	      }
	    });
		
		
		
	}
	
	public void teste(){
		ConnectivityManager connMgr = (ConnectivityManager) 
		        getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		    if (networkInfo != null && networkInfo.isConnected()) {
		        // fetch data
		    	new DownloadWebpageTask().execute("http://192.168.1.10:8080/axis2/services/Teste/getPackages");
		    } else {
		        // display error
		    }
	}
	
	private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
              
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        	MusicArray.savePackage(result.split("\\]")[0].split("\\[")[1].split(","));
        	File folder = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.example.trabalhovb");
        	if (!folder.isDirectory()) {
        	    folder.mkdir();
        	    MusicArray.loadMusicOnDevice(getAssets());
        	}
        	else {
        		System.out.println("to aqui!");
        		GameXMLHandler.readMusicFromXML();
        	}
        	getGameLevel();
        	Intent intent = new Intent(MenuScene.this, SelectPackageScene.class);
        	startActivity(intent);
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
		SharedPreferences prefs = getPreferences(MODE_PRIVATE); 
		String restoredText = prefs.getString("last-level", null);
		if (restoredText == null) {
			SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
			editor.putString("last-level", ""+0);
			editor.putString("current-level", ""+0);
			editor.commit();
		}
		GameLogic.lastLevel = Integer.parseInt(prefs.getString("last-level", null));
		GameLogic.currentLevel = Integer.parseInt(prefs.getString("current-level", null));
		System.out.println("current level e last - > " + GameLogic.lastLevel+ " - " + GameLogic.currentLevel);
		
	}
	
    /** Called when the user clicks the Send button */
    public void startGame(View view) {
//    	
    	
    	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_scene, menu);
		return true;
	}

}

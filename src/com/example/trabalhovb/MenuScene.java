package com.example.trabalhovb;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.gamelogic.MusicArray;
import com.example.utils.ServerHandler;
 
@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class MenuScene extends Activity {

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
	    	  progress = ProgressDialog.show(v.getContext(), "Getting Games","Please Wait", true);
	    	  new Thread(new Runnable() {
	    		  @Override
	    		  public void run()
	    		  {
	    		    // do the thing that takes a long time
	    			  MusicArray.resetMap();
	    			  //getGameLevel();
	    			  startGame();
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
		    	String url = ServerHandler.serverUrl+"getgames";
				MusicArray.setGames(ServerHandler.connect(url));
				
				Intent intent = new Intent(MenuScene.this, SelectPackageScene.class);
		    	startActivity(intent);
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
	
	public void getGameLevel(){
    	Intent intent = new Intent(MenuScene.this, SelectPackageScene.class);
    	startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_scene, menu);
		return true;
	}
	
}
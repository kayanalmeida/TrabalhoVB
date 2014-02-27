package com.example.trabalhovb;
import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
	    	  startGame(v);
	      }
	    });
		
		
		
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
    	Intent intent = new Intent(this, SelectPackageScene.class);
    	startActivity(intent);
    	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_scene, menu);
		return true;
	}

}

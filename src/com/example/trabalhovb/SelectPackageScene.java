package com.example.trabalhovb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.gamelogic.GameLogic;
import com.example.gamelogic.MusicArray;
import com.example.utils.ServerHandler;

@SuppressLint("DefaultLocale") public class SelectPackageScene extends Activity {
	Button updateBtn;
	ListView listview;
	ArrayList<String> list;
	@SuppressLint("DefaultLocale") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.activity_fade_open,R.anim.activity_fade_close);
		setContentView(R.layout.activity_select_package_scene);
	    updateBtn = (Button) findViewById(R.id.updateBtn);
	    listview = (ListView) findViewById(R.id.gameList);
		list = new ArrayList<String>();
	    initGameList();
	    final StableArrayAdapter adapter = new StableArrayAdapter(this,
	        android.R.layout.simple_list_item_1, list);
	    listview.setAdapter(adapter);
	    
	    updateBtn.setOnClickListener(new OnClickListener()
	    {
	      public void onClick(View v)
	      {
	    	  final ProgressDialog progress;
	    	  progress = ProgressDialog.show(v.getContext(), "Getting Games","Please Wait", true);
	    	  new Thread(new Runnable() {
	    		  @Override
	    		  public void run()
	    		  {
	    		    // do the thing that takes a long time
	    			  updateList();
	    		    runOnUiThread(new Runnable() {
	    		      @Override
	    		      public void run()
	    		      {
	    		    	adapter.clear();
	    		    	initGameList();
	    		    	listview.setAdapter(adapter);
	    		    	adapter.UpdateAdapter(android.R.layout.simple_list_item_1, list);
	    		        progress.dismiss();
	    		      }
	    		    });
	    		  }
	    		}).start();
	      }
	    });

	    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	      @Override
	      public void onItemClick(AdapterView<?> parent, final View view,
	          int position, long id) {	
			  GameLogic.currentPackage = adapter.getItem(position).trim().toLowerCase();
			  GameLogic.currentLevel = 0;
	    	  final ProgressDialog progress;
	    	  progress = ProgressDialog.show(view.getContext(), "Loading Musics","Please Wait", true);
	    	  new Thread(new Runnable() {
	    		  @Override
	    		  public void run()
	    		  {
	    		    // do the thing that takes a long time
	    			  startGame(MusicArray.getGameId());
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

	private void initGameList() {
		
		if(MusicArray.gameIds.size() > 0){
			String[] values = new String[(MusicArray.gameIds.size())];
		    
		    int ii = 0;
		    for (String key : MusicArray.gameIds.keySet())
		    {
		    	values[ii] = key.substring(0, 1).toUpperCase() 
		    			+ key.substring(1);
		    	ii++;
		    }
	
		    for (int i = 0; i < values.length; ++i) {
		      list.add(values[i]);
		    }
	    }
	}
	
	public void updateList(){
		ConnectivityManager connMgr = (ConnectivityManager) 
		        getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		    if (networkInfo != null && networkInfo.isConnected()) {
		        // fetch data
		    	String url = ServerHandler.serverUrl+"getgames";
				MusicArray.setGames(ServerHandler.connect(url));
				
		    } else {
		    	// 1. Instantiate an AlertDialog.Builder with its constructor
				Builder alertDialog = new AlertDialog.Builder(SelectPackageScene.this);
				// 2. Chain together various setter methods to set the dialog characteristics
				alertDialog.setMessage("No Internet Connection Available")//R.string.dialog_message)
				       .setTitle("Error");//R.string.dialog_title);
				// 3. Get the AlertDialog from create()
				AlertDialog dialog = alertDialog.create();
				dialog.show();
		    }
	}
	
	public void startGame(int gameId){
		ConnectivityManager connMgr = (ConnectivityManager) 
		        getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		    if (networkInfo != null && networkInfo.isConnected()) {
		        // fetch data
		    	String url = ServerHandler.serverUrl+"getgmids?gameid=" + gameId;
				MusicArray.setGameMusics(ServerHandler.connect(url));
				Intent intent = new Intent(this, GameSceneActivity.class);
		    	startActivity(intent);
		    } else {
		    	// 1. Instantiate an AlertDialog.Builder with its constructor
				Builder alertDialog = new AlertDialog.Builder(SelectPackageScene.this);
				// 2. Chain together various setter methods to set the dialog characteristics
				alertDialog.setMessage("No Internet Connection Available")//R.string.dialog_message)
				       .setTitle("Error");//R.string.dialog_title);
				// 3. Get the AlertDialog from create()
				AlertDialog dialog = alertDialog.create();
				dialog.show();
		    }
	}
	
	  private class StableArrayAdapter extends ArrayAdapter<String> {

	    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

	    public StableArrayAdapter(Context context, int textViewResourceId,
	        List<String> objects) {
	      super(context, textViewResourceId, objects);
	      for (int i = 0; i < objects.size(); ++i) {
	        mIdMap.put(objects.get(i), i);
	      }
	    }
	    public void UpdateAdapter(int textViewResourceId,List<String> objects){
	    	for (int i = 0; i < objects.size(); ++i) {
		        mIdMap.put(objects.get(i), i);
		      }
	    }

	    @Override
	    public long getItemId(int position) {
	      String item = getItem(position);
	      return mIdMap.get(item);
	    }

	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }

	  }
	  @Override
		public void onBackPressed() {
			Intent intent = new Intent(this,MenuScene.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		    overridePendingTransition(R.anim.activity_fade_open,R.anim.activity_fade_close);
		}
	} 
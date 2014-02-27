package com.example.trabalhovb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.gamelogic.GameLogic;
import com.example.gamelogic.MusicArray;

public class SelectPackageScene extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.activity_fade_open,R.anim.activity_fade_close);
		setContentView(R.layout.activity_select_package_scene);
	    final ListView listview = (ListView) findViewById(R.id.gameList);
	    
	    String[] values = new String[(MusicArray.gameMusics.size())];
	    
	    int ii = 0;
	    for (String key : MusicArray.gameMusics.keySet())
	    {
	    	values[ii] = key.substring(0, 1).toUpperCase() 
	    			+ key.substring(1);;
	    	ii++;
	    }

	    final ArrayList<String> list = new ArrayList<String>();
	    for (int i = 0; i < values.length; ++i) {
	      list.add(values[i]);
	    }
	    final StableArrayAdapter adapter = new StableArrayAdapter(this,
	        android.R.layout.simple_list_item_1, list);
	    listview.setAdapter(adapter);

	    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	      @Override
	      public void onItemClick(AdapterView<?> parent, final View view,
	          int position, long id) {	
			  GameLogic.currentPackage = adapter.getItem(position).toLowerCase();
			  GameLogic.currentLevel = 0;
			  startGame();
	      }

	    });

	  }
	
	public void startGame(){
		Intent intent = new Intent(this, GameSceneActivity.class);
    	startActivity(intent);
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
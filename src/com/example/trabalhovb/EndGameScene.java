package com.example.trabalhovb;

import com.example.gamelogic.GameLogic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EndGameScene extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.activity_fade_open,R.anim.activity_fade_close);
		setContentView(R.layout.activity_end_game_scene);
		
		TextView endText = (TextView) findViewById(R.id.endGameMsg);
		
		Typeface t = Typeface.createFromAsset(getAssets(), "fonts/BoogieNightsShadowNF.ttf");
		endText.setTypeface(t);
		endText.setTextSize(40);
		boolean advancedLvl = false;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    endText.setText("Congratulations, Correct Answer!");
		    advancedLvl = extras.getBoolean("ANS_VAL");
		}
		
		Button menuBtn = (Button) findViewById( R.id.menuBtn);
		Button retryBtn = (Button) findViewById( R.id.retryBtn);
		Button nextBtn = (Button) findViewById( R.id.nextBtn);
		nextBtn.setVisibility(View.INVISIBLE);
	menuBtn.setOnClickListener(new OnClickListener(){public void onClick(View v){changeScene("menu");}});
	if (advancedLvl){
		nextBtn.setVisibility(View.VISIBLE);
		nextBtn.setOnClickListener(new OnClickListener(){public void onClick(View v){changeScene("next");}});
	}
	retryBtn.setOnClickListener(new OnClickListener(){public void onClick(View v){changeScene("retry");}});
	}
		
		public void changeScene ( String scene ){
			Intent intent = null;
			if(scene.equals("menu")){
				intent = new Intent(this,MenuScene.class);
			}
			else if (scene.equals("retry")){
				if (GameLogic.currentLevel>0 ){GameLogic.currentLevel--;}
				intent = new Intent(this, GameSceneActivity.class);
			}
			else if (scene.equals("next")){
				intent = new Intent(this, GameSceneActivity.class);
			}
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.end_game_scene, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this,MenuScene.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	    overridePendingTransition(R.anim.activity_fade_open,R.anim.activity_fade_close);
	}

}

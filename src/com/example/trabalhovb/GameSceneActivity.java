package com.example.trabalhovb;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gamelogic.GameLogic;
import com.example.gamelogic.MusicArray;
import com.example.gamelogic.PlayerHandler;
//import android.widget.ProgressBar;
 
@SuppressLint("DefaultLocale")
public class GameSceneActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.activity_fade_open,R.anim.activity_fade_close);
		setContentView(R.layout.activity_game_scene);
		PlayerHandler.prepareSong(
				MusicArray.getMusic(GameLogic.currentLevel, GameLogic.currentPackage ).getPath()
				);
		TextView gameSceneMsg = (TextView) findViewById(R.id.gameSceneMsg);
		Typeface t = Typeface.createFromAsset(getAssets(), "fonts/BoogieNightsShadowNF.ttf");
		gameSceneMsg.setTypeface(t);
		gameSceneMsg.setTextSize(50);
//		PlayerHandler.mp.setOnPreparedListener(new OnPreparedListener(){
//		
//		    public void onPrepared(MediaPlayer mp) {
////		    	updateProcessBar(mp);
//		    }    
//		});
		final Button playMusicBtn = (Button) findViewById( R.id.playMusicBtn );
		playMusicBtn.setOnClickListener(new OnClickListener()
	    {
	      public void onClick(View v)
	      {
	    	  if (PlayerHandler.isPlaying == false){
	    		  System.out.println("---currentlevel - "+GameLogic.currentLevel);
	    		  playMusicBtn.setBackgroundResource(R.drawable.ic_action_stop);
	    		  PlayerHandler.playSong();
	    		  updateProcessBar(PlayerHandler.mp);
	    	  PlayerHandler.mp.setOnCompletionListener(new  MediaPlayer.OnCompletionListener() { 
		            public  void  onCompletion(MediaPlayer mediaPlayer) {
		            	PlayerHandler.stopSong();
		            	playMusicBtn.setBackgroundResource(R.drawable.ic_action_slideshow);
		            } 
		        }); 
	    	  }
	    	  else
	    	  {
	    		  PlayerHandler.stopSong();
	    		  playMusicBtn.setBackgroundResource(R.drawable.ic_action_slideshow);
	    	  }
	      }
	    });
		initializeButtons();
		
	}

	public void endGame(View v, String msg,boolean pass) {
		
	PlayerHandler.releasePlayer();
	  Intent intent = new Intent(this, EndGameScene.class);
	  intent.putExtra("END_SCENE_MESSAGE", msg);
	  intent.putExtra("ANS_VAL", pass);
  	  startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_scene, menu);
		return true;
	}
		@Override
	public void onBackPressed() {

		PlayerHandler.releasePlayer();
	    finish();//go back to the previous Activity
	    overridePendingTransition(R.anim.activity_fade_open,R.anim.activity_fade_close);
	}
		private void updateProcessBar(MediaPlayer mp) {
			final ProgressBar pBar = (ProgressBar) findViewById( R.id.musicProgressBar );
	        final int duration = mp.getDuration();
	        final int amoungToupdate = duration / 100;
	        final Timer mTimer = new Timer();
	        mTimer.schedule(new TimerTask() {

	            @Override
	            public void run() {
	                runOnUiThread(new Runnable() {
	                	
	                    @Override
	                    public void run() {
	                        if (!(amoungToupdate * pBar.getProgress() >= duration) && PlayerHandler.isPlaying ) {
	                            int p = pBar.getProgress();
	                            p += 1;
	                            pBar.setProgress(p);
	                        }
	                        else {
	                        	pBar.setProgress(0);
	                        	mTimer.cancel();
	                            mTimer.purge();
	                        }
	                        
	                    }
	                });
	            };
	        },0 ,amoungToupdate);
		}

		
		public void initializeButtons(){
			List<Button> btnList = new ArrayList<Button>();
			btnList.add((Button) findViewById(R.id.AnswerBtn0));btnList.add((Button) findViewById(R.id.AnswerBtn1));btnList.add((Button) findViewById(R.id.AnswerBtn2));btnList.add((Button) findViewById(R.id.AnswerBtn3));
			List<String> mscNames = new ArrayList<String>();
			mscNames = MusicArray.getAnswers(GameLogic.currentLevel, GameLogic.currentPackage);
			
			Collections.shuffle(btnList);
			for (int i = 0; i < btnList.size();i++){
				final Button btn = btnList.get(i);
				btn.setText(mscNames.get(i));
				
				btn.setOnClickListener(new OnClickListener(){
					public void onClick(View v){
						PlayerHandler.stopSong();
						System.out.println(MusicArray.getMusic(GameLogic.currentLevel,GameLogic.currentPackage).getName());
						String answerString = btn.getText().toString();
						String msg = "Game Over, You Missed!";
						boolean pass = false;
						if (GameLogic.advanceLevel(MusicArray.getMusic(GameLogic.currentLevel,GameLogic.currentPackage).getName(),answerString)){ 
							//retorna true se o jogador acertar
							 SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
							 SharedPreferences.Editor editor = sharedPref.edit();
							 editor.putString(getString(R.string.cLvl),""+ GameLogic.currentLevel );
							 editor.putString(getString(R.string.lLvl), ""+GameLogic.lastLevel );
							 editor.commit();
							 msg = "Congratulations, Correct Answer!";
							 pass = true;
							 }
						endGame(v,msg, pass);
						}
					});
			}
			
			
			}
		}
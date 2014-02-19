package com.example.trabalhovb;



import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
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

		PlayerHandler.prepareSong(MusicArray.getMusic(GameLogic.currentLevel, GameLogic.currentPackage ).getPath());
		PlayerHandler.mp.setOnPreparedListener(new OnPreparedListener(){

		    public void onPrepared(MediaPlayer mp) {
//		    	updateProcessBar(mp);
		    }    
		});
		final Button playMusicBtn = (Button) findViewById( R.id.playMusicBtn );
		playMusicBtn.setOnClickListener(new OnClickListener()
	    {
	      public void onClick(View v)
	      {
	    	  if (PlayerHandler.isPlaying == false){
	    		  System.out.println("---currentlevel - "+GameLogic.currentLevel);
	    		  playMusicBtn.setText("Stop");
	    		  PlayerHandler.playSong();
	    		  updateProcessBar(PlayerHandler.mp);
	    	  PlayerHandler.mp.setOnCompletionListener(new  MediaPlayer.OnCompletionListener() { 
		            public  void  onCompletion(MediaPlayer mediaPlayer) {
		            	PlayerHandler.stopSong();
		            	playMusicBtn.setText("Play");
		            } 
		        }); 
	    	  }
	    	  else
	    	  {
	    		  playMusicBtn.setText("Play");
	    		  PlayerHandler.stopSong();
	    	  }
	      }
	    });
		
		final Button sendAnswerBtn = (Button) findViewById( R.id.sendAnswerBtn );
		sendAnswerBtn.setOnClickListener(new OnClickListener()
	    {
	      public void onClick(View v)
	      {
	    	  PlayerHandler.stopSong();
	    	  TextView answerView = (TextView)findViewById(R.id.answerField) ;  
	    	  System.out.println(MusicArray.getMusic(GameLogic.currentLevel,GameLogic.currentPackage).getName());
	    	  String answerString = answerView.getText().toString();
	    	 if (GameLogic.advanceLevel(MusicArray.getMusic(GameLogic.currentLevel,GameLogic.currentPackage).getName(), 
	    			 answerString)){ //retorna true se o jogador acertar
	  			SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
	  			editor.putString("last-level", ""+GameLogic.lastLevel);
	  			editor.putString("current-level",""+GameLogic.currentLevel);
	  			editor.commit();
	  		}
	    	  endGame(v);
	      }
	    });
		
	}

	public void endGame(View v) {
	  Intent intent = new Intent(this, EndGameScene.class);
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
	            	System.out.println("passei no run  " );
	                runOnUiThread(new Runnable() {
	                	
	                    @Override
	                    public void run() {
	                    	System.out.println("passei aqui   " +(amoungToupdate * pBar.getProgress()) + "  duration  " +  duration );
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
}
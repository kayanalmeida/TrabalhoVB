package com.example.trabalhovb;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamelogic.GameLogic;
import com.example.gamelogic.MusicArray;
import com.example.utils.ServerHandler;
 
@SuppressLint("DefaultLocale")
public class GameSceneActivity extends Activity implements OnClickListener, OnTouchListener, OnCompletionListener, OnBufferingUpdateListener {
	Button playMusicBtn;
	private SeekBar seekBarProgress;
	MediaPlayer mediaPlayer;
	int mediaFileLengthInMilliseconds;
	String musicUrl;
	private final Handler handler = new Handler();
	private List<String> mscNames = new ArrayList<String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.activity_fade_open,R.anim.activity_fade_close);
		setContentView(R.layout.activity_game_scene);
		TextView gameSceneMsg = (TextView) findViewById(R.id.gameSceneMsg);
		Typeface t = Typeface.createFromAsset(getAssets(), "fonts/BoogieNightsShadowNF.ttf");
		gameSceneMsg.setTypeface(t);
		gameSceneMsg.setTextSize(50);
		startGame((View) findViewById(R.id.playMusicBtn));
		
	}
	
	
	
    /** This method initialize all the views in project*/
    private void initView() {
		playMusicBtn = (Button)findViewById(R.id.playMusicBtn);
		playMusicBtn.setOnClickListener(this);
		initializeButtons();
		
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnCompletionListener(this);
		
		seekBarProgress = (SeekBar) findViewById(R.id.musicProgressBar);
	}
	/** Method which updates the SeekBar primary progress by current song playing position*/
    private void primarySeekBarProgressUpdater() {
			seekBarProgress.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaFileLengthInMilliseconds)*100)); // This math construction give a percentage of "was playing"/"song length"
			if (mediaPlayer.isPlaying()) {
				Runnable notification = new Runnable() {
			        public void run() {
			        	primarySeekBarProgressUpdater();
					}
			    };
			    handler.postDelayed(notification,1000);
			}
    }
    
    @Override
	public void onClick(View v) {
		if(v.getId() == R.id.playMusicBtn){
			 /** ImageButton onClick event handler. Method which start/pause mediaplayer playing */
			try {
				mediaPlayer.setDataSource(musicUrl); // setup song from http://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
				mediaPlayer.prepare(); // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer. 
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL
			
			if(!mediaPlayer.isPlaying()){
				mediaPlayer.start();
				playMusicBtn.setBackgroundResource(R.drawable.ic_action_stop);
			}else {
				mediaPlayer.pause();
				playMusicBtn.setBackgroundResource(R.drawable.ic_action_slideshow);
			}
			
			primarySeekBarProgressUpdater();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v.getId() == R.id.musicProgressBar){
			/** Seek bar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
			if(mediaPlayer.isPlaying()){
		    	SeekBar sb = (SeekBar)v;
				int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
				mediaPlayer.seekTo(playPositionInMillisecconds);
			}
		}
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		 /** MediaPlayer onCompletion event handler. Method which calls then song playing is complete*/
		playMusicBtn.setBackgroundResource(R.drawable.ic_action_slideshow);
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		/** Method which updates the SeekBar secondary progress by current song loading from URL position*/
		seekBarProgress.setSecondaryProgress(percent);
	}
		
	public void startGame(View v){
		ConnectivityManager connMgr = (ConnectivityManager) 
		        getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		    if (networkInfo != null && networkInfo.isConnected()) {
		        // fetch data
		    	final ProgressDialog progress;
		    	  progress = ProgressDialog.show(v.getContext(), "Loading Musics","Please Wait", true);
		    	  new Thread(new Runnable() {
		    		  @Override
		    		  public void run()
		    		  {
		    		    // do the thing that takes a long time
		    			  getAnswers();
		    			  runOnUiThread(new Runnable() {
			    		      @Override
			    		      public void run()
		    		      {
			    		    //Do the rest..
		    		  		musicUrl = ServerHandler.serverUrl+"getmusic?gid="+ MusicArray.getGameId() + "&mid=" + 
		    		  				MusicArray.getCurrentMusicId();
		    		  		System.out.println(musicUrl);
    		  				initView();
		    		        progress.dismiss();
		    		      }
		    		    });
		    		  }
		    		}).start();
		    } else {
		    	// 1. Instantiate an AlertDialog.Builder with its constructor
				Builder alertDialog = new AlertDialog.Builder(GameSceneActivity.this);
				// 2. Chain together various setter methods to set the dialog characteristics
				alertDialog.setMessage("No Internet Connection Available")//R.string.dialog_message)
				       .setTitle("Error");//R.string.dialog_title);
				// 3. Get the AlertDialog from create()
				AlertDialog dialog = alertDialog.create();
				dialog.show();
		    }
	}

	public void endGame(View v, boolean pass) {

	Intent intent = new Intent(this, EndGameScene.class);
  	intent.putExtra("ANS_VAL", pass);
  	startActivity(intent);
	}
	public void getAnswers() {
		Random rand = new Random();
		int r1,r2,r3;
		String url = ServerHandler.serverUrl + "getrandmusic?mid=";
		r1 = rand.nextInt(3332)  + 1 ;
		r2 = 3333 + rand.nextInt(3332)  + 1 ;
		r3 = 6666 + rand.nextInt(3332)  + 1 ;
		
		url += r1 + "," + r2 + "," + r3;
		String resp = ServerHandler.connect(url);
		String[] respArray = resp.split("/");
		for(int i = 0; i< respArray.length ;i++ ) {
			
			mscNames.add(respArray[i]);
		}

		//getting the correct music name
		resp = ServerHandler.connect(ServerHandler.serverUrl + "getmname?mid=" + MusicArray.getCurrentMusicId());
		GameLogic.correctAns = resp;
		mscNames.add(resp);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_scene, menu);
		return true;
	}
		@Override
	public void onBackPressed() {
		mediaPlayer.stop();
	    finish();//go back to the previous Activity
	    overridePendingTransition(R.anim.activity_fade_open,R.anim.activity_fade_close);
	}
		
		public void initializeButtons(){
			List<Button> btnList = new ArrayList<Button>();
			btnList.add((Button) findViewById(R.id.AnswerBtn0));btnList.add((Button) findViewById(R.id.AnswerBtn1));btnList.add((Button) findViewById(R.id.AnswerBtn2));btnList.add((Button) findViewById(R.id.AnswerBtn3));
//			mscNames = MusicArray.getAnswers(GameLogic.currentLevel, GameLogic.currentPackage);
			Collections.shuffle(btnList);
			for (int i = 0; i < btnList.size();i++){
				final Button btn = btnList.get(i);
				btn.setTextSize(14);
				btn.setText(mscNames.get(i).trim());
				
				btn.setOnClickListener(new OnClickListener(){
					public void onClick(View v){
						String answerString = btn.getText().toString();
						boolean pass = false;
						pass = GameLogic.advanceLevel(answerString); 
//						if (){
						if (pass){
							//retorna true se o jogador acertar
							 mediaPlayer.stop();
							 SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
							 SharedPreferences.Editor editor = sharedPref.edit();
							 editor.putString(getString(R.string.cLvl),""+ GameLogic.currentLevel );
							 editor.putString(getString(R.string.lLvl), ""+GameLogic.lastLevel );
							 editor.commit();
							 pass = true;
							 endGame(v,pass);
							 }
						else{
							Context context = getApplicationContext();
							CharSequence text = "WROOOOOONG!";
							int duration = Toast.LENGTH_SHORT;

							Toast toast = Toast.makeText(context, text, duration);
							toast.show();
						}
						}
					});
			}
			
			
			}
		}
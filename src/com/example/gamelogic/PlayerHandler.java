package com.example.gamelogic;
import java.io.IOException;

import android.media.MediaPlayer;

public class PlayerHandler {
	
	public static MediaPlayer mp;
	public static boolean isPlaying = false;
	public static String mPath = "";
	

	public static void prepareSong(String musicPath)
	{
		if (mp==null){mp = new MediaPlayer();mPath = musicPath;};
		try {
			mp.reset();
			mp.setDataSource(musicPath);
			mp.prepare();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void playSong(){
		if (mp != null){prepareSong(mPath);};
		isPlaying = true;
		mp.start();
	}
	
	public static void stopSong(){
		mp.stop();
		isPlaying = false;
	}
	
	public static void releasePlayer(){
		mp.release();
		mp=null;
		isPlaying = false;
	}
	
	

//	public void audioPlayer(String path, String fileName) throws FileNotFoundException{
//        //set up MediaPlayer    
//        
////        InputStream inputStream = getAssets().open("");
//    	
//        try {
//        	if (mp.isPlaying()){
//            	mp.reset();
//            	}
//        	
//            mp.setDataSource(getAssets().openFd("t.mp3").getFileDescriptor());
//            mp.prepare();
//            mp.start();
//            
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
}

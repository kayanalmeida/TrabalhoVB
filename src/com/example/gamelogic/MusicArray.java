package com.example.gamelogic;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.util.ByteArrayBuffer;

import android.content.res.AssetManager;


public class MusicArray {
	public static Map<String, Integer > gameIds;
	public static Map<String, ArrayList<Integer> > gameMusicsIds;
	
	public static void resetMap(){
		if (gameMusicsIds != null ){
			gameMusicsIds.clear();
		}
		if (gameIds != null ){
			gameIds.clear();
		}
	}
	
	public static ByteArrayBuffer openAudioFromFolder(AssetManager mngr, String musicName)
	{
		ByteArrayBuffer baf = new ByteArrayBuffer(1024); 
    	InputStream path = null;
		try {
			path = mngr.open("music/"+musicName);
			BufferedInputStream bis = new BufferedInputStream(path,1024);

            //get the bytes one by one
            int current = 0;

            while ((current = bis.read()) != -1) {

                baf.append((byte) current);
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baf;
	}
	
//	functions after the node js migration....
	public static void setGames(String games) {
		// Gets from cloud the available games...
		if (gameIds == null){
			gameIds = new HashMap<String, Integer >() ;
		}
		else{
			gameIds.clear();
		}
		if (!games.trim().isEmpty())
		{
			String[] games_array = games.split("/");
			for (int i = 0; i < games_array.length ; i += 2) {
//				System.out.println(games_array[i+1]);
				gameIds.put(games_array[i].trim().toLowerCase(), Integer.parseInt(games_array[i+1].trim()));
			};
		}
	}
	public static int getGameId() {
		// TODO Auto-generated method stub
		int id = 0;
		id = gameIds.get(GameLogic.currentPackage);
		return id;
		
	}
	
	public static int getCurrentMusicId() {
		// TODO Auto-generated method stub
		int id = 0;
		id = gameMusicsIds.get(GameLogic.currentPackage).get(GameLogic.currentLevel);
		return id;
	}
	
	public static void setGameMusics(String musicIds) {
		// TODO Auto-generated method stub
		String[] mId = musicIds.split("/");
		if (gameMusicsIds == null){
			gameMusicsIds = new HashMap<String, ArrayList<Integer>>() ;
		}
		if (gameMusicsIds.get(GameLogic.currentPackage) == null) {
			gameMusicsIds.put(GameLogic.currentPackage, new ArrayList<Integer>() );
		}
		for (int i = 0; i < mId.length ; i++) {
			gameMusicsIds.get(GameLogic.currentPackage).add(Integer.parseInt(mId[i].trim()));
		};
	}
}


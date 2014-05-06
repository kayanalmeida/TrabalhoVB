package com.example.gamelogic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.ByteArrayBuffer;

import android.content.res.AssetManager;
import android.os.Environment;

import com.example.utils.GameXMLHandler;

public class MusicArray {
	public static Map<String, ArrayList<Music>> gameMusics;
//	public static Music[] musicsOfGame;
	
	public static Music getMusic(int level, String gamePackage){
		if (level < gameMusics.get(gamePackage).size() &&  gameMusics.containsKey(gamePackage)  )  {
			return gameMusics.get(gamePackage).get(level);
		}
		else{
			return null;
		}
	}
	public static List<String> getAnswers(int level, String gamePackage){
		List<String> list = new ArrayList<String>();
		String name = gameMusics.get(gamePackage).get(level).name ;
//		name = name.substring(0, name.length() - 4);
		list.add(name);
		
		List<Integer> indexList = new ArrayList<Integer>();
		for(int i = 0; i < gameMusics.get(gamePackage).size() ; i++) {if (i!=level){indexList.add(i);}};
		Collections.shuffle(indexList);
		
		for(int i = 0; i < 3; i++) {
			name = gameMusics.get(gamePackage).get(indexList.get(i)).name;
//			name = name.substring(0, name.length() - 4);
			list.add(name);
		}
		Collections.shuffle(list);Collections.shuffle(list);
		return list;
	}
	public static void saveToSDCard(byte[] bitmapdata, String musicName,int i){
		File file = new File(Environment.getExternalStorageDirectory(),
				"/Android/data/com.example.trabalhovb/" + musicName);
		FileOutputStream fos;
		
		//create music java object
		//int nameSeparator = musicName.indexOf("-");
		
		addMusicToMap(musicName);

		try {
		    fos = new FileOutputStream(file);
		    fos.write(bitmapdata);
		    fos.flush();
		    fos.close();
		} catch (IOException e) {
		    // handle exception
		}
	}
	
	public static void savePackage(String pckgName) {
			if (gameMusics == null){
				gameMusics = new HashMap<String, ArrayList<Music>>() ;
			}
			if (gameMusics.get(pckgName.trim()) == null) {
				gameMusics.put(pckgName.trim(), new ArrayList<Music>() );
			}
	}

	public static void addMusicToMap(String musicName) {
		String[] tempMusicData = musicName.split("_");
		savePackage(tempMusicData[0]);
		String name = tempMusicData[2].substring(0, tempMusicData[2].length() - 4);
		name = name.substring(0, 1).toUpperCase() + 
				name.substring(1);
		
		for (int index = name.indexOf("-");
			     index >= 0;
			     index = name.indexOf("-", index + 1))
			{
			name = name.substring(0,index) + " "+ name.substring(index+1,index+2)
					.toUpperCase() + name.substring(index+2,name.length()); 
			}
//		name.replace("-", " ");
//		System.out.println("nome da musica------  "+ name);
		gameMusics.get(tempMusicData[0]).add(new Music(name,tempMusicData[1],
				Environment.getExternalStorageDirectory().getAbsolutePath()+
				"/Android/data/com.example.trabalhovb/" + musicName));
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
	
	public static void openAudioFromXML(){
		GameXMLHandler.readMusicFromXML();
	}

	public static void loadMusicOnDevice(AssetManager mngr) {
		//lists all the files into an array
		 String[] musics;
		try {
			musics = mngr.list("music");
//			musicsOfGame = new Music[musics.length] ;
		   for (int ii = 0; ii < musics.length; ii++) {
		        String fileOutput = musics[ii];
				ByteArrayBuffer baf = openAudioFromFolder(mngr, fileOutput);
				saveToSDCard(baf.toByteArray(),fileOutput,ii); 
		        }
//		   System.out.println("TAMANHO FINAL ->" + musicsOfGame.length);
		   GameXMLHandler.saveMusicToXML(gameMusics);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


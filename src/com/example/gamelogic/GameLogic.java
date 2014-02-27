package com.example.gamelogic;

import android.annotation.SuppressLint;

@SuppressLint("DefaultLocale")
public class GameLogic {
	public static int lastLevel;
	public static int currentLevel;
	public static String currentPackage;
	
	//Verifies if artist/song name is valid
	public static boolean advanceLevel(String correctName,String input ) {
		input = input.toLowerCase().replaceAll("\\s+","");
		correctName = correctName.toLowerCase().replaceAll("\\s+","");
		if ((input+".mp3").equals(correctName)) {
			currentLevel++;
			if ((lastLevel < currentLevel) && 
					(currentLevel <
							MusicArray.gameMusics.get(currentPackage).size())){
				lastLevel++;
				System.out.println("Passou de fase");
			}
			else if (currentLevel == 
					MusicArray.gameMusics.get(currentPackage).size()) {
				currentLevel--;
				System.out.println("Zerou o Jogo!");
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	
	
//	public static Music getCurrentSong()
//	{
//		Music music = MusicArray.getMusic(lastLevel);
//		return music;
//	}
	
}

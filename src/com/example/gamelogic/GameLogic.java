package com.example.gamelogic;

import android.annotation.SuppressLint;

@SuppressLint("DefaultLocale")
public class GameLogic {
	public static int lastLevel;
	public static int currentLevel;
	public static String currentPackage;
	public static String correctAns = "";
	
	//Verifies if artist/song name is valid
	public static boolean advanceLevel(String input ) {
		input = input.toLowerCase().replaceAll("\\s+","");
		correctAns = correctAns.toLowerCase().replaceAll("\\s+","");
		if ((input).equals(correctAns)) {
			currentLevel++;
			if ((lastLevel < currentLevel) && 
					(currentLevel <
							MusicArray.gameMusicsIds.get(currentPackage).size())){
				lastLevel++;
			}
			else if (currentLevel == 
					MusicArray.gameMusicsIds.get(currentPackage).size()) {
				currentLevel--;
				System.out.println("Zerou o Jogo!");
			}
			return true;
		}
		else {
			return false;
		}
	}
	
}

package com.example.gamelogic;

import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.app.Activity;
import com.example.trabalhovb.R;

public class KeyboardHandler {
	
	public static void getCorrectButtons (Activity a, String musicName){
		LinearLayout row = new LinearLayout(a);
        row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		for (int i = 0; i < musicName.length() ; i++)
		{
			String character = String.valueOf(musicName.charAt(i));
			if (character != "") {
				Button btnTag = new Button(a);
		        btnTag.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		        btnTag.setText(character);
		        btnTag.setId(i);
		        row.addView(btnTag);
		        System.out.println(character);
			}
		}
//		LinearLayout layout = (LinearLayout) a.findViewById(R.id.buttonSpace);
//		layout.addView(row);
	}
}

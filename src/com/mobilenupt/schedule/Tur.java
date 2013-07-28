package com.mobilenupt.schedule;

import java.util.ArrayList;

import com.mobilenupt.sliding.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Tur extends Activity{
	
	TextView[] text = new TextView[8];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_thurlayout);
		
		text[0] = (TextView)findViewById(R.id.turone);
		text[1] = (TextView)findViewById(R.id.turthird);
		text[2] = (TextView)findViewById(R.id.turfive);
		text[3] = (TextView)findViewById(R.id.tursix);
		text[4] = (TextView)findViewById(R.id.tureight);
		text[5] = (TextView)findViewById(R.id.turnine);
		text[6] = (TextView)findViewById(R.id.turten);
		
		ArrayList<String> list = new ArrayList<String>();
		list = this.getIntent().getStringArrayListExtra("list");
		for (int i=0; i<7; i++) {
			String temp = list.get(i);
			if (!temp.equals("#")) {
				text[i].setText(temp);
				text[i].setTextColor(android.graphics.Color.BLACK);
			}
		}		
	}

}

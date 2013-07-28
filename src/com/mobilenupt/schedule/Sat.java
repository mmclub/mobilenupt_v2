package com.mobilenupt.schedule;

import java.util.ArrayList;

import com.mobilenupt.sliding.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Sat extends Activity{
	
	TextView[] text = new TextView[8];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_satlayout);
		
		text[0] = (TextView)findViewById(R.id.satone);
		text[1] = (TextView)findViewById(R.id.satthird);
		text[2] = (TextView)findViewById(R.id.satfive);
		text[3] = (TextView)findViewById(R.id.satsix);
		text[4] = (TextView)findViewById(R.id.sateight);
		text[5] = (TextView)findViewById(R.id.satnine);
		text[6] = (TextView)findViewById(R.id.satten);
		
		ArrayList<String> list;
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
package com.mobilenupt.schedule;

import java.util.ArrayList;

import com.mobilenupt.sliding.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Wed extends Activity{
	
	TextView[] text = new TextView[8];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_wedlayout);
		

		text[0] = (TextView)findViewById(R.id.wedone);
		text[1] = (TextView)findViewById(R.id.wedthird);
		text[2] = (TextView)findViewById(R.id.wedfive);
		text[3] = (TextView)findViewById(R.id.wedsix);
		text[4] = (TextView)findViewById(R.id.wedeight);
		text[5] = (TextView)findViewById(R.id.wednine);
		text[6] = (TextView)findViewById(R.id.wedten);
		
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

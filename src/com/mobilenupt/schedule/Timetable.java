package com.mobilenupt.schedule;

import java.util.ArrayList;
import java.util.Calendar;

import com.mobilenupt.sliding.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Timetable extends TabActivity {
    // private MenuHorizontalScrollView scrollView;
    // private ListView menuList;
    // private View timetable;
    // private Button menuBtn;
    // private MenuListAdapter menuListAdapter;

    // -----------------------------------------------------------------

    Calendar c = Calendar.getInstance();

    String linshitext = "";
    RadioGroup weektab;
    TabHost tabhost;

    private String res;
    private ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.schedule_table_page);
	// ----------------------------------------------------
	 /*
	LayoutInflater inflater = LayoutInflater.from(this);
	setContentView(inflater.inflate(R.layout.menu_scroll_view, null));
	this.scrollView = (MenuHorizontalScrollView) findViewById(R.id.mScrollView);
	this.menuListAdapter = new MenuListAdapter(this, 1);
	this.menuList = (ListView) findViewById(R.id.menuList);
	this.menuList.setAdapter(menuListAdapter);

	this.timetable = inflater.inflate(R.layout.schedule_table_page, null);
	this.menuBtn = (Button) this.timetable
		.findViewById(R.id.table_menuBtn);
	this.menuBtn.setOnClickListener(onClickListener);

	View leftView = new View(this);
	leftView.setBackgroundColor(Color.TRANSPARENT);
	final View[] children = new View[] { leftView, timetable };
	this.scrollView.initViews(children, new SizeCallBackForMenu(
		this.menuBtn), this.menuList);
	this.scrollView.setMenuBtn(this.menuBtn);
	*/
	// ------------------------------------------------------
	weektab = (RadioGroup) findViewById(R.id.weektab);
	RadioButton monradio = (RadioButton) findViewById(R.id.monradio);
	RadioButton tueradio = (RadioButton) findViewById(R.id.tueradio);
	RadioButton wedradio = (RadioButton) findViewById(R.id.wedradio);
	RadioButton turradio = (RadioButton) findViewById(R.id.turradio);
	RadioButton friradio = (RadioButton) findViewById(R.id.friradio);
	RadioButton satradio = (RadioButton) findViewById(R.id.satradio);
	int week = c.get(Calendar.DAY_OF_WEEK);

	ArrayList<String> list1 = new ArrayList<String>();
	ArrayList<String> list2 = new ArrayList<String>();
	ArrayList<String> list3 = new ArrayList<String>();
	ArrayList<String> list4 = new ArrayList<String>();
	ArrayList<String> list5 = new ArrayList<String>();
	ArrayList<String> list6 = new ArrayList<String>();

	list.add(list1);
	list.add(list2);
	list.add(list3);
	list.add(list4);
	list.add(list5);
	list.add(list6);

	Intent intent = this.getIntent();
	res = intent.getExtras().getString("html");
	String[] reslist = res.split("@");
	// Log.v("res", res);
	int size = reslist.length;
	int index = 0;
	Log.v("size", String.valueOf(size));
	for (int i = 0; i < 6; i++) {
	    for (int j = 0; j < 7; j++) {
	    	list.get(i).add(reslist[index++]);
	    }
	}

	weektab.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.monradio:
		    tabhost.setCurrentTabByTag("mon");
		    break;
		case R.id.tueradio:
		    tabhost.setCurrentTabByTag("tue");
		    break;
		case R.id.wedradio:
		    tabhost.setCurrentTabByTag("wed");
		    break;
		case R.id.turradio:
		    tabhost.setCurrentTabByTag("tur");
		    break;
		case R.id.friradio:
		    tabhost.setCurrentTabByTag("fri");
		    break;
		case R.id.satradio:
		    tabhost.setCurrentTabByTag("sat");
		    break;

		}
	    }
	});
	tabhost = getTabHost();
	TabSpec mon, tue, wed, tur, fri, sat;

	Intent intent1 = new Intent(this, Mon.class);
	intent1.putStringArrayListExtra("list", list.get(0));
	Intent intent2 = new Intent(this, Tue.class);
	intent2.putStringArrayListExtra("list", list.get(1));
	Intent intent3 = new Intent(this, Wed.class);
	intent3.putStringArrayListExtra("list", list.get(2));
	Intent intent4 = new Intent(this, Tur.class);
	intent4.putStringArrayListExtra("list", list.get(3));
	Intent intent5 = new Intent(this, Fri.class);
	intent5.putStringArrayListExtra("list", list.get(4));
	Intent intent6 = new Intent(this, Sat.class);
	intent6.putStringArrayListExtra("list", list.get(5));

	mon = tabhost.newTabSpec("mon").setIndicator("mon").setContent(intent1);
	tue = tabhost.newTabSpec("tue").setIndicator("tue").setContent(intent2);
	wed = tabhost.newTabSpec("wed").setIndicator("wed").setContent(intent3);
	tur = tabhost.newTabSpec("tur").setIndicator("tur").setContent(intent4);
	fri = tabhost.newTabSpec("fri").setIndicator("fri").setContent(intent5);
	sat = tabhost.newTabSpec("sat").setIndicator("sat").setContent(intent6);

	tabhost.addTab(sat);
	tabhost.addTab(tur);
	tabhost.addTab(fri);
	tabhost.addTab(wed);
	tabhost.addTab(mon);
	tabhost.addTab(tue);

	switch (week) {
		case 1:
		    tabhost.setCurrentTabByTag("sat");
		    satradio.setChecked(true);
		    break;
		case 2:
		    tabhost.setCurrentTabByTag("mon");
		    monradio.setChecked(true);
		    break;
		case 3:
		    tabhost.setCurrentTabByTag("tue");
		    tueradio.setChecked(true);
		    break;
		case 4:
		    tabhost.setCurrentTabByTag("wed");
		    wedradio.setChecked(true);
		    break;
		case 5:
		    tabhost.setCurrentTabByTag("tur");
		    turradio.setChecked(true);
		    break;
		case 6:
		    tabhost.setCurrentTabByTag("fri");
		    friradio.setChecked(true);
		    break;
		case 7:
		    tabhost.setCurrentTabByTag("sat");
		    satradio.setChecked(true);
		    break;
		}
    }
    
}

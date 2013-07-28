package com.mobilenupt.sliding;

import com.mobilenupt.adapter.MenuListAdapter;
import com.mobilenupt.ui.MenuHorizontalScrollView;
import com.mobilenupt.ui.SizeCallBackForMenu;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;

/**
 * 生活秘书 界面
 *
 */
public class Secretary extends BaseActivity {
    private ListView menuList;
    private View secretary;
    private Button menuBtn;
    private MenuListAdapter menuListAdapter;
    TabHost life_tab;
	RadioGroup life_radio;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		setContentView(inflater.inflate(R.layout.menu_scroll_view, null));
		this.scrollView = (MenuHorizontalScrollView) findViewById(R.id.mScrollView);
		this.menuListAdapter = new MenuListAdapter(this, 3);
		this.menuList = (ListView) findViewById(R.id.menuList);
		this.menuList.setAdapter(menuListAdapter);
	
		this.secretary = inflater.inflate(R.layout.secretary_page, null);
		this.menuBtn = (Button) this.secretary
			.findViewById(R.id.secretary_menuBtn);
		this.menuBtn.setOnClickListener(onClickListener);
	
		View leftView = new View(this);
		leftView.setBackgroundColor(Color.TRANSPARENT);
		final View[] children = new View[] { leftView, secretary };
		this.scrollView.initViews(children, new SizeCallBackForMenu(
			this.menuBtn), this.menuList);
		this.scrollView.setMenuBtn(this.menuBtn);
		
		life_radio = (RadioGroup) findViewById(R.id.life_radio);
		life_tab = (TabHost) findViewById(R.id.life_tab);
	
		life_tab.setup();
		TabSpec life_bianli, life_zuoxi, life_campus,life_busroute;
		// 9-7 add by MT
		life_campus = life_tab.newTabSpec("life_campus")
				.setIndicator("life_campus").setContent(R.id.life_campus);
		life_tab.addTab(life_campus);
		// 2012-9-9 add by lixin
		life_busroute = life_tab.newTabSpec("life_busroute")
				.setIndicator("life_bus").setContent(R.id.life_busroute);
		life_tab.addTab(life_busroute);
		
	
		life_bianli = life_tab.newTabSpec("life_bianli")
				.setIndicator("life_bianli").setContent(R.id.life_bianli);
		life_tab.addTab(life_bianli);
		life_zuoxi = life_tab.newTabSpec("life_zuoxi")
				.setIndicator("life_zuoxi").setContent(R.id.life_zuoxi);
		life_tab.addTab(life_zuoxi);
	
		life_radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.life_one:
						life_tab.setCurrentTabByTag("life_bianli");
						break;
					case R.id.life_two:
						life_tab.setCurrentTabByTag("life_zuoxi");
						break;
					case R.id.life_three:
						life_tab.setCurrentTabByTag("life_campus");
						break;
					case R.id.life_four:
						life_tab.setCurrentTabByTag("life_busroute");
						break;
					}
				}
			}
		);
    }

    private OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View arg0) {
		    scrollView.clickMenuBtn();
		}
    };
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	return super.onKeyDown(keyCode, event);
    }
    
}

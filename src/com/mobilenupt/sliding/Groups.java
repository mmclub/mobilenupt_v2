package com.mobilenupt.sliding;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mobilenupt.adapter.MenuListAdapter;
import com.mobilenupt.service.CheckNet;
import com.mobilenupt.ui.MenuHorizontalScrollView;
import com.mobilenupt.ui.SizeCallBackForMenu;

/*
 * 
 * 社团模块 一级菜单
 */
public class Groups extends BaseActivity implements Runnable {

	private ListView menuList;
    private View groups;
    private Button menuBtn;
    private MenuListAdapter menuListAdapter;
    /*---------------------------------------------------------------------*/
    private Intent chooseIntent;
    private Bundle idBundle;
    private boolean check;
    private Thread checknetThread;

    private ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
    private SimpleAdapter simple = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		setContentView(inflater.inflate(R.layout.menu_scroll_view, null));
		
		this.scrollView = (MenuHorizontalScrollView) findViewById(R.id.mScrollView);
		this.menuListAdapter = new MenuListAdapter(this, 5);
		this.menuList = (ListView) findViewById(R.id.menuList);
		this.menuList.setAdapter(menuListAdapter);
	
		this.groups = inflater.inflate(R.layout.groups_page, null);
		this.menuBtn = (Button) this.groups.findViewById(R.id.groups_menuBtn);
		this.menuBtn.setOnClickListener(onClickListener);
	
		View leftView = new View(this);
		leftView.setBackgroundColor(Color.TRANSPARENT);
		final View[] children = new View[] { leftView, groups };
		this.scrollView.initViews(children, new SizeCallBackForMenu(
			this.menuBtn), this.menuList);
		this.scrollView.setMenuBtn(this.menuBtn);
		/*---------------------------------------------------------------------*/
		chooseIntent = new Intent();
		chooseIntent.setClass(Groups.this, GroupsMainActivity.class);
		idBundle = new Bundle();
		
		checknetThread = new Thread(Groups.this);
		checknetThread.start();
		
		// 生成动态数组，并且传入数据
		setAssociations();
		// 实例化GridView
		GridView mGridView = (GridView) findViewById(R.id.gridview);
		// 构建一个适配器
		simple = new SimpleAdapter(Groups.this, lstImageItem, R.layout.groups_image,
			new String[] { "ItemImage", "ItemText" }, new int[] {
				R.id.ItemImage, R.id.ItemText });
		mGridView.setAdapter(simple);
		mGridView.setOnItemClickListener(onItemClickListener);
    }/*---------------------------------------------------------------------*/

    private OnClickListener onClickListener = new OnClickListener() {
    	public void onClick(View arg0) {
    		scrollView.clickMenuBtn();
		}
    };
    
    private GridView.OnItemClickListener onItemClickListener = new GridView.OnItemClickListener() {
    	public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
    		long page_id = -1;
			switch (position) {
			case 0:
			    page_id = 600907477;
			    break;
			case 1:
			    page_id = 601003549;
			    break;
			case 2:
			    page_id = 600889745;
			    break;
			case 3:
			    page_id = 601017224;
			    break;
			case 4:
			    page_id = 600490284;
			    break;
			default:
			    break;
			}
			
			if (check) btndown(page_id);
		    else newToast("目前没有网络连接", true);
    	}
	};

    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	return super.onKeyDown(keyCode, event);
    }
    
    private void btndown(long id) {
		idBundle.putLong("page_id", id);
		chooseIntent.putExtras(idBundle);
		startActivity(chooseIntent);
    }

    public void run() {
		while (true) {
		    check = CheckNet.checkNetwork(this);
		    try {
		    	Thread.sleep(1000);
		    } catch (InterruptedException e) {
		    	e.printStackTrace();
		    }
		}
    }

    public void setAssociations() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.img);// 添加图像资源的ID
		map.put("ItemText", "青春南邮");// 按序号做ItemText
		lstImageItem.add(map);
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("ItemImage", R.drawable.img1);
		map1.put("ItemText", "青志联");
		lstImageItem.add(map1);
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("ItemImage", R.drawable.img2);
		map2.put("ItemText", "社团联合会");
		lstImageItem.add(map2);
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("ItemImage", R.drawable.img3);
		map3.put("ItemText", "校科学技术协会");
		lstImageItem.add(map3);
		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("ItemImage", R.drawable.img4);
		map4.put("ItemText", "校学生会");
		lstImageItem.add(map4);
    }

}

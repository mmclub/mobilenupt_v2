package com.mobilenupt.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobilenupt.sliding.*;
import com.mobilenupt.sliding.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * MenuList的适配器
 * 
 */
public class MenuListAdapter extends BaseAdapter {

    private Activity context;
    private List<Map<String, Object>> listItems;
    private int itemCount;
    private LayoutInflater listInflater;
    private boolean isPressed[];
    // private int imageId = R.drawable.star_icon;
    private final int COUNT = 9;
    private int pressedId;

    public final class ListItemsView {
		// public ImageView menuIcon;
		public TextView menuText;
    }

    public MenuListAdapter(Activity context, int pressedId) {
		this.context = context;
		this.pressedId = pressedId;
		this.init();
    }

    public int getCount() {
		return this.itemCount;
    }

    public Object getItem(int position) {
		return position;
    }

    public long getItemId(int position) {
		return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
		final int po = position;
		ListItemsView listItemsView;
		if (convertView == null) {
		    listItemsView = new ListItemsView();
		    convertView = this.listInflater.inflate(R.layout.menu_list_item,
			    null);
		    // listItemsView.menuIcon = (ImageView)
		    // convertView.findViewById(R.id.menuIcon);
		    listItemsView.menuText = (TextView) convertView
			    .findViewById(R.id.menuText);
		    convertView.setTag(listItemsView);
		} else {
		    listItemsView = (ListItemsView) convertView.getTag();
		}
	
		// listItemsView.menuIcon.setBackgroundResource((Integer)
		// listItems.get(position).get("menuIcon"));
		listItemsView.menuText.setText((String) listItems.get(position).get(
			"menuText"));
		
		if (this.isPressed[position] == true && position!=0)
		    convertView.setBackgroundResource(R.drawable.menu_item_bg_sel);
		else
		    convertView.setBackgroundColor(Color.TRANSPARENT);
	
		convertView.setOnClickListener(new OnClickListener() {
	
		    public void onClick(View view) {
			changeState(po);
			gotoActivity(po);
			notifyDataSetInvalidated();
			new Handler().post(new Runnable() {
	
			    public void run() {
	
			    }
	
			});
		    }
		});
	
		return convertView;
    }

    private void gotoActivity(int position) {
		Intent intent = new Intent();
		switch (position) {
		case 1:
	
		    if (this.pressedId == 1) {
				Library activity = (Library) context;
				activity.getScrollView().clickMenuBtn();
		    } else {
				intent.setClass(context, Library.class);
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.push_in,
					R.anim.push_out);
				context.finish();
		    }
		    break;
		/*----------------------------------------------------*/
		case 2:
		    if (this.pressedId == 2) {
				Schedule activity = (Schedule) context;
				activity.getScrollView().clickMenuBtn();
		    } else {
				intent.setClass(context, Schedule.class);
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.push_in,
					R.anim.push_out);
				context.finish();
		    }
		    break;
		/*----------------------------------------------------*/
		case 3:
		    if (this.pressedId == 3) {
				Secretary activity = (Secretary) context;
				activity.getScrollView().clickMenuBtn();
		    } else {
				intent.setClass(context, Secretary.class);
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.push_in,
					R.anim.push_out);
				context.finish();
		    }
		    break;
		/*------------------------------------------------------*/
		case 4:
		    if (this.pressedId == 4) {
				News activity = (News) context;
				activity.getScrollView().clickMenuBtn();
		    } else {
				intent.setClass(context, News.class);
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.push_in,
					R.anim.push_out);
				context.finish();
		    }
		    break;
		/*------------------------------------------------------*/
		case 5:
		    if (this.pressedId == 5) {
				Groups activity = (Groups) context;
				activity.getScrollView().clickMenuBtn();
		    } else {
				intent.setClass(context, Groups.class);
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.push_in,
					R.anim.push_out);
				context.finish();
		    }
		    break;
		/*------------------------------------------------------*/
		case 6:
		    if (this.pressedId == 6) {
				Feedback activity = (Feedback) context;
				activity.getScrollView().clickMenuBtn();
		    } else {
				intent.setClass(context, Feedback.class);
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.push_in,
					R.anim.push_out);
				context.finish();
		    }
		    break;
		/*------------------------------------------------------*/
		case 7:
		    if (this.pressedId == 7) {
				ShareActivity activity = (ShareActivity) context;
				activity.getScrollView().clickMenuBtn();
		    } else {
				intent.setClass(context, ShareActivity.class);
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.push_in,
					R.anim.push_out);
				context.finish();
		    }
		    break;
		/*------------------------------------------------------*/
		case 8:
		    if (this.pressedId == 8) {
				About activity = (About) context;
				activity.getScrollView().clickMenuBtn();
		    } else {
				intent.setClass(context, About.class);
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.push_in,
					R.anim.push_out);
				context.finish();
		    }
		    break;
		/*----------------------------------------------------*/
		default:
		}
    }

    private void changeState(int position) {

		for (int i = 0; i < this.itemCount; i++) {
		    isPressed[i] = false;
		}
		isPressed[position] = true;
	    }
	
	    private void init() {
	
		this.itemCount = this.COUNT;
		this.listItems = new ArrayList<Map<String, Object>>();
		this.isPressed = new boolean[this.itemCount];
		for (int i = 0; i < this.itemCount; i++) {
		    Map<String, Object> map = new HashMap<String, Object>();
		    // map.put("menuIcon", imageId);
		    if (i == 0) {
		    	map.put("menuText", "NUPTer");
		    } else if (i == 1) {
		    	map.put("menuText", "掌上图书馆");
		    } else if (i == 2) {
		    	map.put("menuText", "口袋课程表");
		    } else if (i == 3) {
		    	map.put("menuText", "生活秘书");
		    } else if (i == 4) {
		    	map.put("menuText", "南邮新闻");
		    } else if (i == 5) {
		    	map.put("menuText", "七彩社团");
		    } else if (i == 6) {
		    	map.put("menuText", "反馈");
		    } else if (i == 7) {
		    	map.put("menuText", "分享");
		    } else if (i == 8) {
		    	map.put("menuText", "关于");
		    } 
	
		    this.listItems.add(map);
		    this.isPressed[i] = false;
		}
		this.isPressed[this.pressedId] = true;
		this.listInflater = LayoutInflater.from(context);
	    }
	}

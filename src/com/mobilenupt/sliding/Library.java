package com.mobilenupt.sliding;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mobilenupt.adapter.*;
import com.mobilenupt.library.*;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.mobilenupt.library.Data;
import com.mobilenupt.service.CheckNet;
import com.mobilenupt.ui.SizeCallBackForMenu;
import com.mobilenupt.ui.MenuHorizontalScrollView;

/**
 * 
 * 图书馆收搜索页
 *
 */
public class Library extends BaseActivity implements Runnable,OnScrollListener{
    private boolean checkNet;
    private int temp;
    private Thread thread;

    private String input;
    private String input02;
    private int n; // 页号
    private int N;// 总页数
    private String m; // 数目总数
    private double M;

    private Data data = new Data("http://202.119.224.6:8080/opac/openlink.php");

    private String resultStr;
    private int lastItem;
    private int listsize;

    private ImageButton search;
    private EditText search_edit;
    private LinearLayout footerProgressBarLayout;

    private ListView listbook;
    private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    private SimpleAdapter listItemAdapter;

    // _----------------------------------------
    private ListView menuList;
    private View library;
    private Button menuBtn;
    private MenuListAdapter menuListAdapter;
    private View[] children;
    private LayoutInflater inflater;

    private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
		    if (msg.what == 1) {
				listItemAdapter.notifyDataSetChanged(); // 更新适配器
				listbook.setSelection(0); // 焦点置顶
		    } else if (msg.what == 2) {
				listItemAdapter.notifyDataSetChanged(); // 更新适配器
				listbook.setSelection(lastItem - 4);// 每次加载完数据都能让页面保持在新内容之上
				footerProgressBarLayout.setVisibility(View.GONE);
		    } else if (msg.what == 3) {
		    	footerProgressBarLayout.setVisibility(View.VISIBLE);
		    } else if (msg.what == 4) {
		    	footerProgressBarLayout.setVisibility(View.GONE);
		    }
		}
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(this);
	
		setContentView(inflater.inflate(R.layout.menu_scroll_view, null));
		scrollView = (MenuHorizontalScrollView) findViewById(R.id.mScrollView);
		menuListAdapter = new MenuListAdapter(this, 1);
		menuList = (ListView) findViewById(R.id.menuList);
		menuList.setAdapter(menuListAdapter);
	
		library = inflater.inflate(R.layout.library_page, null);
		menuBtn = (Button) this.library.findViewById(R.id.library_menuBtn);
		menuBtn.setOnClickListener(onClickListener);
	
		View leftView = new View(this);
		leftView.setBackgroundColor(Color.TRANSPARENT);
		children = new View[] { leftView, library };
		scrollView.initViews(children, new SizeCallBackForMenu(this.menuBtn),
			this.menuList);
		scrollView.setMenuBtn(this.menuBtn);
	
		// --------------------------------------------------------------
		footerProgressBarLayout = (LinearLayout) findViewById(R.id.linearlayout);
		footerProgressBarLayout.setVisibility(View.GONE);
		listbook = (ListView) findViewById(android.R.id.list);
		search = (ImageButton) findViewById(R.id.search);
		search_edit = (EditText) findViewById(R.id.search_edit);
	
		newProgressdialog("掌上图书馆提示您", "每天上自习，必成高富帅");
	
		search.setOnClickListener(new searchImageButtonClickListener());
	
		listItemAdapter = new SimpleAdapter(this, listItem,
			R.layout.library_booklist, new String[] { "BookName",
				"Sysnumber", "Type", "Press", "Lending", "Href" },
			new int[] { R.id.bookname, R.id.sysnumber, R.id.type,
				R.id.press, R.id.lending });
		listbook.setOnItemClickListener(new booklistItemCliclListener());
		listbook.setAdapter(listItemAdapter);
		listbook.setOnScrollListener(this);
	
		// 软键盘设置
		getWindow().setSoftInputMode(
			WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	
		// 检查更新传入的变量，判断是否需要弹出更新对话框，默认为1
		if(this.getIntent().getIntExtra("flag", 1)==0){
			ShowUpdateDialog();
		}
		
		newToast("", false);
		thread = new Thread(Library.this);
		thread.start();
    }
    
    // -------------------------------------------------------------------------
    // 事件监听
    private OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View arg0) {
		    scrollView.clickMenuBtn();
		}
    };
    
    class booklistItemCliclListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		    String url = "";
		    url += listItem.get(arg2).get("Href");
	
		    Intent intent = new Intent();
		    intent.setAction("android.intent.action.VIEW");
		    intent.setData(Uri.parse(url));
		    startActivity(intent);
		}
    }

    class searchImageButtonClickListener implements OnClickListener {
		public void onClick(View v) {
		    Log.i("network", String.valueOf(checkNet));
		    if (checkNet == false) {
				showToast("目前没有网络连接");
		    } else {
				n = 1; // 每次点击重新搜索，页号置为1
				input = search_edit.getText().toString(); // 获取输入内容
				input02 = "";
				input02 = new String(URLEncoder.encode(input));// 对输入内容进行重新编码
				Log.v("input", input02);
				if (input02.equals("") || input02.equals(null)) {// 关键字输入为空，不进行搜索操作
				    showToast("请输入要查询的内容");
				} else {
				    progressdialog.show();
				    data.setWord(input02);
				    data.setPage("1");
				    GetSearchThread thread = new GetSearchThread();
				    thread.start();
				}
		    }
		}
    }
    
    
    void ShowUpdateDialog() {
		Builder check_dialog = new Builder(Library.this);
		check_dialog.setPositiveButton("立即更新", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Uri uri = Uri.parse("http://mobilenupt.sinaapp.com/download.php");  
				Intent ittent_uri = new Intent(Intent.ACTION_VIEW, uri);  
				startActivity(ittent_uri);
			}
		});
		check_dialog.setTitle("发现新版本 v"+this.getIntent().getStringExtra("new_vcode"));
		check_dialog.setMessage(this.getIntent().getStringExtra("str_result"));
		check_dialog.setNegativeButton("下次再说", null);
		check_dialog.create().show();
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	return super.onKeyDown(keyCode, event);
    }
    
    // ----------------------------------------------------------------------------
    public void run() {
		while (true) {
		    checkNet = CheckNet.checkNetwork(this);
		    try {
		    	Thread.sleep(1000);
		    } catch (InterruptedException e) {
		    	e.printStackTrace();
		    }
		}
    }

    private void PrepareData() {
		try {
		    List<ListBook> bookArray = Spliter.getBookList(resultStr);
		    m = bookArray.get(0).getN();
		    M = Double.parseDouble(m);
		    N = (int) Math.ceil(M / 20);
		    // log the total page
		    Log.i("totale-page", String.valueOf(N));
		    HashMap<String, Object> map;
		    for (int i = 0; i < bookArray.size(); i++) {
			map = new HashMap<String, Object>();
			map.put("BookName", bookArray.get(i).getBookname());
			map.put("Sysnumber", bookArray.get(i).getSysnubmer());
			map.put("Type", bookArray.get(i).getType());
			map.put("Press", bookArray.get(i).getPress());
			map.put("Lending", bookArray.get(i).getLending());
			map.put("Href", bookArray.get(i).getHref());
			listItem.add(map);
			listsize = listItem.size();
			temp = listsize;
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
    }

    public void onScroll(AbsListView view, int firstVisibleItem,
	    int visibleItemCount, int totalItemCount) {
    	lastItem = firstVisibleItem + visibleItemCount;
    	// Log.v("lastItem", String.valueOf(lastItem));
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {

		if (checkNet == false) {
		    showToast("目前没有网络连接");
		} else if (lastItem >= temp
			    && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
	
			handler.sendEmptyMessage(3);
	
			GetNextThread thread = new GetNextThread();
			thread.start();
		}
    }
    
    class GetSearchThread extends Thread {
    	public void run() {
		    // when creat thread in a thread is needed
		    try {
		    	resultStr = data.post();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    	showToast("网络连接超时了*_*");
			} finally {
			    progressdialog.dismiss();
			}

			if (resultStr != null && resultStr != "") {
				// 搜索结果不为空
				listItem.clear();
				PrepareData();
				handler.sendEmptyMessage(1);
		    } else {
				showToast("木有找到*_*试试更完整的描述呢^_^");
		    }
		}
    }
    
    class GetNextThread extends Thread {
    	 public void run() {
			if (n < N) {
			    n++; // 若当前页号小于总页数每次加载数据页数+1
			    String m = String.valueOf(n);
			    data.setPage(m);
			    try {
					resultStr = data.post();
					Log.v("resutlt", resultStr);
				} catch (IOException e) {
					showToast("网络连接超时了*_*");
					e.printStackTrace();
				}
			    
				PrepareData();
				handler.sendEmptyMessage(2);
			} else {
			    temp = 0;
				handler.sendEmptyMessage(4);
				showToast("已到最后一页");
			}
    	}
    }
}

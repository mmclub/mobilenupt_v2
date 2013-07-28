package com.mobilenupt.sliding;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mobilenupt.adapter.MenuListAdapter;
import com.mobilenupt.ui.MenuHorizontalScrollView;
import com.mobilenupt.ui.SizeCallBackForMenu;

public class News extends BaseActivity implements OnScrollListener{
	private Button m_button_1;
	private Button m_button_4;
	
	String sessionId = ""; 
	public static final String url_jwc = "http://jwc.njupt.edu.cn/";
	public static final String url_njupt = "http://www.njupt.edu.cn/";
	private HttpURLConnection connection;
    
    private ListView listview;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String,Object>> msgs;
    
    private int type_id = 1;    
    private int total_page;		// 首页新闻，公告会用到
    private int page;
    
	private HashMap<String,Object> map;
	
	private int lastItem;
	
	private LinearLayout footerLayout;
	
	private String res;
	private InputStream in;
	 /*-------------------------------------------------------------*/
    private ListView menuList;
    private View news;
    private Button menuBtn;
    private MenuListAdapter menuListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
		 // set no title
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = LayoutInflater.from(this);
		setContentView(inflater.inflate(R.layout.menu_scroll_view, null));
		
		this.scrollView = (MenuHorizontalScrollView) findViewById(R.id.mScrollView);
		this.menuListAdapter = new MenuListAdapter(this, 4);
		this.menuList = (ListView) findViewById(R.id.menuList);
		this.menuList.setAdapter(menuListAdapter);
	
		this.news = inflater.inflate(R.layout.news_page, null);
		this.menuBtn = (Button) this.news
			.findViewById(R.id.news_menuBtn);
		this.menuBtn.setOnClickListener(onClickListener);
	
		View leftView = new View(this);
		leftView.setBackgroundColor(Color.TRANSPARENT);
		final View[] children = new View[] { leftView, news };	 
		this.scrollView.initViews(children, new SizeCallBackForMenu(
			this.menuBtn), this.menuList);
		this.scrollView.setMenuBtn(this.menuBtn);
		/*-------------------------------------------------------------*/   
	    m_button_1 = (Button)findViewById(R.id.button11);
	    m_button_1.setOnClickListener(onClickListener);
	    m_button_4 = (Button)findViewById(R.id.button44);
	    m_button_4.setOnClickListener(onClickListener);
	    
	    newToast("", false);
	    newProgressdialog("教学快递，一键早知道~", "加载中...");
	            
	    listview = (ListView)findViewById(R.id.listview1);
	    msgs = new ArrayList<HashMap<String,Object>>();
	    adapter = new SimpleAdapter(this, msgs, R.layout.news_listview_news, 
	    		new String[]{"artical_title","artical_time","artical_link"},
				new int[]{R.id.artical_title, R.id.artical_time});
	    listview.setAdapter(adapter);
	    listview.setOnScrollListener(this);
	    listview.setOnItemClickListener(onItemClickListener);
	    
	    footerLayout = (LinearLayout)findViewById(R.id.lodinglayout1);
	    footerLayout.setVisibility(View.GONE);
	    
		init();
    }
    /*-------------------------------------------------------------*/
    
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
    	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
    		String url = "";
    		if (type_id == 1){
    			url = url_jwc;
    		}
    		else if(type_id == 4){
    			url = url_njupt;
    		}
    		url += msgs.get(arg2).get("artical_link");
    		
    		Intent intent = new Intent();
    	    intent.setAction("android.intent.action.VIEW");
    	    intent.setData(Uri.parse(url));
    	    startActivity(intent);
    	}
    };
    
    private OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View arg0) {
			if (arg0 == menuBtn) {
				scrollView.clickMenuBtn();
			}
			else if (arg0 == m_button_1) {
				if (type_id != 1){        			
	    			type_id = 1;
	    			m_button_1.setBackgroundResource(R.drawable.news_noticetap_click);
	    			m_button_4.setBackgroundResource(R.drawable.news_newstap_normal);
	    			init();
	    		}
			}
			else if (arg0 == m_button_4) {
				if (type_id != 4){
	    			type_id = 4;
	    			m_button_1.setBackgroundResource(R.drawable.news_noticetap_normal);
	    			m_button_4.setBackgroundResource(R.drawable.news_newstap_click);
	    			init();
	    		}
			}
		}
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	return super.onKeyDown(keyCode, event);
    }
    
    /*-------------------------------------------------------------*/
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount;
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if ( lastItem >= adapter.getCount() && scrollState == OnScrollListener.SCROLL_STATE_IDLE ){
    		footerLayout.setVisibility(View.VISIBLE);
    		page += 1;
    		try {
    			doGet(page);
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
	}
    
	// 初次显示一栏
	public void init()
    {
    	msgs.clear();
    	page = 0;
    	progressdialog.show();
    	listview.setSelection(0);
    	
    	try {
    		doGet(0);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
   
    public void doGet(int page) throws IOException
	{	
    	String url = "";
    	if (type_id == 1) {			// 教务公告
    		if(page == 0)
    			url = url_jwc + "s/24/t/923/p/21/list.jspy";
    		else
    			url = url_jwc + "s/24/t/923/p/21/i/" + String.valueOf(page) + "/list.jspy";
    	}
    	else if (type_id == 4) {	// 首页新闻
    		if (page == 0)
    			url = url_njupt + "s/1/t/1/p/21/list.htm";
    		else
    			 url = url_njupt + "s/1/t/1/p/21/i/" + String.valueOf(total_page - page) + "/list.htm";
    	}
        initConnect(url);
        new getThread().start();

	}
    
    private void initConnect(String url) throws IOException {
    	URL getUrl = new URL(url);
        connection = (HttpURLConnection) getUrl.openConnection();
        connection.setConnectTimeout(5000);
        connection.setRequestProperty("Accept-Charset", "utf-8");
        connection.setDoInput(true);
    }
    
    // 网络资源读取线程
	class getThread extends Thread {
		public void run() {
			try {
				
				connection.connect();
				in = connection.getInputStream();
				handler.sendEmptyMessage(2);

			} catch (Exception e) {
				e.printStackTrace();
				// 发送网络获取失败消息
				handler.sendEmptyMessage(-1);
			} finally {
				// connection.disconnect();
			}
		}
	}
    

    // parse jwc
    public void parse_jwc()
    {	
    	Document doc = null;
		try {
			doc = Jsoup.parse(res);
			Log.v("jwc_res",res);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// get the entire block
		
		Elements blacktab = doc.getElementsByClass("columnStyle");//("downloadListItem");
		String bt1 = String.valueOf(blacktab.size());
		Log.v("blacktab", bt1);
		for(int i = 0; i < blacktab.size(); i++)
		{	
					
			String artical_title = blacktab.get(i).select("a").text();
			String artical_time = blacktab.get(i).select("td").get(1).text();
			String artical_link = blacktab.get(i).select("a").attr("href");
			
			map = new HashMap<String,Object>();
			map.put("artical_title", artical_title);
			map.put("artical_time", artical_time);
			map.put("artical_link", artical_link);
			msgs.add(map);
		}
		if (blacktab.size() == 0) {
			handler.sendEmptyMessage(-1);
		}
		// updated listview
		handler.sendEmptyMessage(100);
    }
    // parse njupt
    public void parse_njupt()
    {
    	Document doc = null;
    	try {
			doc = Jsoup.parse(res);
			Log.v("njupt_res",res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	if(type_id==4)
    	{
    		String content=doc.toString();
    		String regex ="'pageIndex_PDT',[0-9]{3}";//"_currentPageIndex =[0-9]{3}";
    		Pattern p=Pattern.compile(regex); 
    		Matcher m;
    		m=p.matcher(content);
    		if(m.find())
    			System.out.println(m.group().substring(16,19));
    		total_page=Integer.parseInt(m.group().substring(16,19));
    	}
		
		 
		Elements tableblock = doc.getElementsByClass("columnStyle");
		for (int i=0; i<tableblock.size(); i++) {
			String artical_title = tableblock.get(i).select("a").text();
			String artical_time = tableblock.get(i).select("td").get(1).text();
			String artical_link = tableblock.get(i).select("a").attr("href");

			map = new HashMap<String,Object>();
			map.put("artical_title", artical_title);
			map.put("artical_time", artical_time);
			map.put("artical_link", artical_link);
			msgs.add(map);
		}
		// updated listview
		handler.sendEmptyMessage(100);
    }

    private void parse() {
    	// 读取网络流
		ByteBuffer bf = null;

		byte[] buffer = new byte[1024];;
		int chByte, total = 0;
		ArrayList<byte[]> bufferList = new ArrayList<byte[]>();
		ArrayList<Integer> intList = new ArrayList<Integer>();
		    			
		try {
			chByte = in.read(buffer, 0, 1024);

			while (chByte != -1) {
				
				bufferList.add(buffer);
				intList.add(chByte);
				total += chByte;
				
				buffer = new byte[1024];
				chByte = in.read(buffer, 0, 1024);
			}
			in.close();
			
			bf = ByteBuffer.allocate(total);
			int size = bufferList.size();
			for (int i=0; i<size; i++) {
				bf.put(bufferList.get(i), 0, intList.get(i));
			}
			res = new String(bf.array(), 0, total, "utf-8");
			
		}catch (IOException e){
			e.printStackTrace();
		}
		
		Log.v("parse begin", String.valueOf(type_id));
		if (type_id == 1 || type_id == 2)
			parse_jwc();
		else
			parse_njupt();
    }
    
	private Handler handler = new Handler() {
    	public void handleMessage(Message msg) {

    		if( msg.what == -1 ){	//  网络资源获取失败
    			showToast("网络超时");
    			progressdialog.dismiss();
    		}
    		else if( msg.what == 2 ){	// 获取网络资源成功，开始解析
    			parse();
    		}
    		else if( msg.what == 100 ){
    			adapter.notifyDataSetChanged();
    			
    			if (page == 0)
    				progressdialog.dismiss();
    			else
    				footerLayout.setVisibility(View.GONE);
    		}
    	}
    };
}

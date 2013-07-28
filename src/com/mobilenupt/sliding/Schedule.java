package com.mobilenupt.sliding;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mobilenupt.adapter.*;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mobilenupt.ui.MenuHorizontalScrollView;
import com.mobilenupt.ui.SizeCallBackForMenu;
import com.mobilenupt.schedule.*;
import com.mobilenupt.service.FileService;

public class Schedule extends BaseActivity {
    private final int ERR_NET = -1;
    private final int ERR_PASS = -2;
    private final int ERR_SDCARD = -3;
    private final int MSG_GETSTREAM = 100;
    private final int MSG_START = 1;

    String sessionId = "";
    private HttpURLConnection connection;
    private HttpURLConnection connection2;
    String GET_URL = "http://202.119.225.35/xskbcx.aspx?xh=";
    String POST_URL = "http://202.119.225.35/default3.aspx";
    String cookie = "";

    EditText name;
    EditText pass;
    Button login;
    Button local;

    int flaglogin;
    String xh = "";
    String pw = "";

    private String html;
    private InputStream inStream;
    private FileService fileService;

    // -----------------------------------------------------------------
    private ListView menuList;
    private View schedule;
    private Button menuBtn;
    private MenuListAdapter menuListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		setContentView(inflater.inflate(R.layout.menu_scroll_view, null));
		this.scrollView = (MenuHorizontalScrollView) findViewById(R.id.mScrollView);
		this.menuListAdapter = new MenuListAdapter(this, 2);
		this.menuList = (ListView) findViewById(R.id.menuList);
		this.menuList.setAdapter(menuListAdapter);
	
		this.schedule = inflater.inflate(R.layout.schedule_page, null);
		this.menuBtn = (Button) this.schedule
			.findViewById(R.id.schedule_menuBtn);
		this.menuBtn.setOnClickListener(onClickListener);
	
		View leftView = new View(this);
		leftView.setBackgroundColor(Color.TRANSPARENT);
		final View[] children = new View[] { leftView, schedule };
		this.scrollView.initViews(children, new SizeCallBackForMenu(
			this.menuBtn), this.menuList);
		this.scrollView.setMenuBtn(this.menuBtn);
	
		// ------------------------------------------------------------------------------
		name = (EditText) findViewById(R.id.name);
		pass = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login);
		local = (Button) findViewById(R.id.notnetlogin);
	
		fileService = new FileService(this);
		newToast("", false);
		newProgressdialog(null, "打个洞，钻进教务处正方~");
		
		login.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    
			progressdialog.show();
	
			xh = name.getText().toString();
			pw = pass.getText().toString();
	
			GET_URL += xh;
			// 组织cookie
			cookie = "__VIEWSTATE=dDwtMTM2MTgxNTk4OTs7Pk98dA%2Bv1ST5wEtYHAe8g%2BONFpAH&TextBox1="
				+ xh
				+ "&TextBox2="
				+ pw
				+ "&ddl_js=%D1%A7%C9%FA&Button1=+%B5%C7+%C2%BC+";
	
			new GetTableThread().start();
			// flaghandler.sendEmptyMessage(MSG_START);
		    }
		});
		local.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
				String res = null;
				
				try {
				    res = fileService.readDateFile("timetable.txt");
				    Intent intent = new Intent(Schedule.this, Timetable.class);
				    Bundle bundle = new Bundle();
				    bundle.putString("html", res);
				    intent.putExtras(bundle);
				    Schedule.this.startActivity(intent);
		
				} catch (IOException e) {
				    e.printStackTrace();
				    showToast("离线课程表不存在，去登陆吧~~");
				}
		    }
		});
    }

    private OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View arg0) {
		    scrollView.clickMenuBtn();
		}
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	return super.onKeyDown(keyCode, event);
    }
    // ---------------------------------------------------------------------

    class GetTableThread extends Thread {
	public void run() {
	    URL loginUrl;
	    try {
			loginUrl = new URL(POST_URL);
			connection = (HttpURLConnection) loginUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			connection.setUseCaches(false);

	    } catch (MalformedURLException e) {
	    	e.printStackTrace();
	    	flaghandler.sendEmptyMessage(ERR_NET);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	flaghandler.sendEmptyMessage(ERR_NET);
	    }
	    try {
		connection.connect();
		DataOutputStream out = new DataOutputStream(
			connection.getOutputStream());
		out.writeBytes(cookie);

		int chByte1 = 0;
		byte[] bin = new byte[512];
		InputStream inpost = connection.getInputStream();

		chByte1 = inpost.read(bin);
		String s = new String(bin, 0, chByte1, "gbk");
		Pattern patpost = Pattern.compile("xs_main");
		Matcher matpost = patpost.matcher(s);
		// 登陆失败
		if (!matpost.find()) {
		    flaghandler.sendEmptyMessage(ERR_PASS);
		    return;
		}
		String key = "";
		if (connection != null) {
		    for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
			if (key.equalsIgnoreCase("set-cookie")) {
			    sessionId = connection.getHeaderField(key);
			    sessionId = sessionId.substring(0,
				    sessionId.indexOf(";"));
			}
		    }
		}
		out.flush();
		inpost.close();
		out.close(); // flush and close
		connection.disconnect();

		URL getUrl = new URL(GET_URL);
		connection2 = (HttpURLConnection) getUrl.openConnection();
		connection2.setRequestProperty("Cookie", sessionId);
		connection2.setRequestProperty("Accept-Charset", "gbk");
		connection2.connect();

		inStream = connection2.getInputStream();
		// 正确获取到了网络文件
		flaghandler.sendEmptyMessage(MSG_GETSTREAM);

	    } catch (IOException e) {
		e.printStackTrace();
		flaghandler.sendEmptyMessage(ERR_NET);
	    }
	}
    }

    private void init() {
		GET_URL = "http://202.119.225.35/xskbcx.aspx?xh=";
		cookie = "";
		progressdialog.dismiss();
    }

    Handler flaghandler = new Handler() {
	public void handleMessage(Message msg) {
	    if (msg.what == MSG_START) {
	    	init();

	    	// 10-4 by qu 更改为在login时解�?
	    	String res = JsoupTest2.parse(html);
			try {
			    fileService.writeDateFile("timetable.txt", res.getBytes());
			} catch (Exception e) {
			    e.printStackTrace();
			}
	
			Intent intent = new Intent(Schedule.this, Timetable.class);
			Bundle bundle = new Bundle();
			bundle.putString("html", res);
			intent.putExtras(bundle);
			Schedule.this.startActivity(intent);

	    } else if (msg.what == MSG_GETSTREAM) { // 读取网络文件
	
			html = "";
			try {
			    int buffersize;
			    byte buffer[] = new byte[1024];
			    buffersize = inStream.read(buffer, 0, 1024);
			    while (buffersize != -1) {
				String res = new String(buffer, 0, buffersize, "gbk");
				// Log.e("size", String.valueOf(buffersize));
				// Log.v("html", res);
				html += res;
				buffersize = inStream.read(buffer, 0, 1024);
			    }
			    inStream.close();
			    flaghandler.sendEmptyMessage(MSG_START);
			} catch (IOException e) {
			    e.printStackTrace();
			    Log.i("Exception", "IO");
			    flaghandler.sendEmptyMessage(ERR_NET);
			} finally {
			    connection2.disconnect();
			}
	    } else if (msg.what == ERR_NET) {
			init();
			showToast("亲，网络出错了~~");
	    } else if (msg.what == ERR_PASS) {
			init();
			showToast("转动你聪明的大脑再想想密码呗~~");
	    } else if (msg.what == ERR_SDCARD) {
			init();
			showToast("很不幸，读写错误了！�?��下sd卡吧~~");
	    }

	}
    };
}

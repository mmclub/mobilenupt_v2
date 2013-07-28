package com.mobilenupt.sliding;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.mobilenupt.adapter.MenuListAdapter;
import com.mobilenupt.service.CheckNet;
import com.mobilenupt.ui.MenuHorizontalScrollView;
import com.mobilenupt.ui.SizeCallBackForMenu;

public class Feedback extends BaseActivity implements Runnable{
    private boolean checkNet = true;
    private Thread thread;

    private EditText m_content;
    private ImageButton m_submit;
    private Spinner m_spinner_module;
    
    private final String ip = "http://mobilenupt.sinaapp.com/do2.php";
    private String moduleString = "请选择异常模块";

    private HttpClient httpClient;

    private static final int[] module = { R.string.item_module,
	    R.string.item01, R.string.item02, R.string.item03, R.string.item04,
	    R.string.item05, R.string.item06 };

    private int width, height;
    private String exception_type;
    private String content;
    private String module_type, model, release, phonenumber;

    /*-------------------------------------------------------------*/
    private ListView menuList;
    private View feedback;
    private Button menuBtn;
    private MenuListAdapter menuListAdapter;
    
    private Handler handler = new Handler() {
    	public void handleMessage(Message msg) {
    		switch(msg.what) {
    		case 1:
    			progressdialog.show();
    			break;
    		case 2:
    			progressdialog.dismiss();
    			break;
    		default:
    			break;
    		}
    	}
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		setContentView(inflater.inflate(R.layout.menu_scroll_view, null));
		
		this.scrollView = (MenuHorizontalScrollView) findViewById(R.id.mScrollView);
		this.menuListAdapter = new MenuListAdapter(this, 6);
		this.menuList = (ListView) findViewById(R.id.menuList);
		this.menuList.setAdapter(menuListAdapter);
	
		this.feedback = inflater.inflate(R.layout.feedback_page, null);
		this.menuBtn = (Button) this.feedback.findViewById(R.id.feedback_menuBtn);
		this.menuBtn.setOnClickListener(onClickListener);
	
		View leftView = new View(this);
		leftView.setBackgroundColor(Color.TRANSPARENT);
		final View[] children = new View[] { leftView, feedback };
		this.scrollView.initViews(children, new SizeCallBackForMenu(
			this.menuBtn), this.menuList);
		this.scrollView.setMenuBtn(this.menuBtn);
		/*---------------------------------------------------------------*/
		
		//软键盘设置
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		m_content = (EditText) findViewById(R.id.EditText_content);
		m_submit = (ImageButton) findViewById(R.id.ImageButton_submit);
		m_submit.setOnClickListener(onClickListener);
		m_spinner_module = (Spinner) findViewById(R.id.Spinner_module);
		m_spinner_module.setAdapter(module_adapter);
		m_spinner_module.setOnItemSelectedListener(onItemSelectedListener);
	
		
		thread = new Thread(this);
		thread.start();

		newToast("", false);
		newProgressdialog(null, "提交中...");
		getPhoneInfo();
		getHttpClient();
		
    }
    
    private OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {
    	public void onItemSelected(AdapterView<?> parent,View view, int positon, long id) 
    	{
			LinearLayout ll = (LinearLayout) view;
			TextView tvn = (TextView) ll.getChildAt(0);
			StringBuilder sb = new StringBuilder();
			sb.append(tvn.getText());
			moduleString = sb.toString();
			Log.e("tag", moduleString);
		}
		public void onNothingSelected(AdapterView<?> parent) {}
    };

    private OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View arg0) {
			if (arg0 == menuBtn)
				scrollView.clickMenuBtn();
			else {
				exception_type = "反馈";
				module_type = moduleString.trim();
				content = m_content.getText().toString().trim();
				Log.v("exception_type", exception_type);
				Log.v("module_type", module_type);
				Log.v("content", content);
				
				if (checkNet == false) {
				    showToast("目前没有网络连接");
				} else if (module_type.equals("请选择异常模块")) {
					showToast("请选择模块");
				} else if (content.length() == 0) {
					showToast("请简单描述一下吧，您的反馈是我们的改进的动力~");
				} else {
					new Thread() {
						public void run() {
							handler.sendEmptyMessage(1);
							doPost(module_type, exception_type, content);
						}
					}.start();
				}
			}
		}
    };

    // 获取手机信息
    public void getPhoneInfo() {
		DisplayMetrics dm = new DisplayMetrics(); // 屏幕分辨率容器
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
		model = android.os.Build.MODEL;
		release = android.os.Build.VERSION.RELEASE;
		
		TelephonyManager phoneMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
    	phonenumber = phoneMgr.getLine1Number();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	return super.onKeyDown(keyCode, event);
    }

    /*************************************************************************/
    public void run() {
		while (true) {
		    checkNet = CheckNet.checkNetwork(this);
		    Log.v("net", String.valueOf(checkNet));
		    try {
		    	Thread.sleep(1000);
		    } catch (InterruptedException e) {
		    	e.printStackTrace();
		    }
		}
    }

    private BaseAdapter module_adapter = new BaseAdapter() {

		public int getCount() {
		    return module.length;
		}
		public Object getItem(int position) {
		    return module[position];
		}
		public long getItemId(int position) {
		    return position;
		}
	
		public View getView(int position, View convertView, ViewGroup parent) {
		    LinearLayout ll = new LinearLayout(Feedback.this);
		    ll.setOrientation(LinearLayout.HORIZONTAL);
	
		    TextView tv = new TextView(Feedback.this);
		    tv.setPadding(0, 13, 0, 13);
		    tv.setText(module[position]);// 设置内容
		    tv.setTextSize(22);
		    tv.setTextColor(Color.BLACK);
		    ll.addView(tv);
		    return ll;
		}
    };

    public void doPost(String module, String exception_type, String content) {

		HttpPost httpRequest = new HttpPost(ip);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("module", module));
		params.add(new BasicNameValuePair("exception", exception_type));
		params.add(new BasicNameValuePair("content", content));
		params.add(new BasicNameValuePair("screen", String.valueOf(height)
			+ "," + String.valueOf(width)));
		params.add(new BasicNameValuePair("model", model));
		params.add(new BasicNameValuePair("release", release));
		params.add(new BasicNameValuePair("telephone", phonenumber));
	
		String strResult = "提交失败";
	
		try {
		    httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		    /* 发送请求并等待响应 */
		    HttpResponse httpResponse = httpClient.execute(httpRequest);
		    /* 若状态码为200 ok */
		    if (httpResponse.getStatusLine().getStatusCode() == 200) {
		    	// 读返回数据 
				strResult = EntityUtils.toString(httpResponse.getEntity());
				if (strResult.equals("1")) {
				    strResult = "提交成功";
				}
		    }
		} catch (ClientProtocolException e) {
		    e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		handler.sendEmptyMessage(2);
		showToast(strResult);
    }

    private void getHttpClient() {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000);
		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
		HttpClientParams.setRedirecting(httpParams, true);
		HttpProtocolParams.setUserAgent(httpParams, "Android");
	
		this.httpClient = new DefaultHttpClient(httpParams);
    }

}

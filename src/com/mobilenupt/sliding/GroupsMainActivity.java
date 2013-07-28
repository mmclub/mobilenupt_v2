package com.mobilenupt.sliding;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.renren.api.RenrenApiClient;

/*
 * 
 * 社团模块 二级菜单
 */
public class GroupsMainActivity extends Activity implements OnScrollListener{
    /** Called when the activity is first created. */	
	private RenrenApiClient apiClient;
	
	private JSONObject jsonOb;
	private JSONArray jsonAy,jsonAy2;
	// lixin 2012-7-29
	private long page_id = 601003549;
	private ListView listview;
	// private TextView activity;
	// private TextView lecture;
	private SimpleAdapter adapter = null;	
	// 2012-8-2
	private String  mode = "【动态】";
	private int mode2;
	private int Gpage = 1;
	private int numofmsg = 0;
	ArrayList<HashMap<String,Object>> msgs;	
	String Judge = new String();
	String root_message = "0";
	String message = "0";
	// 2012-8-8
	private Bundle Getid;
	private Intent GetStatus;
	private String time;
	private HashMap<String,Object> map;
	private int headimg;
	
	private int lastItem;
	private int scrollState;
	private Thread mThread;
	private LinearLayout footerLayout;
    private View view;
    
    // 8-23 add by qu
    private final String Err_outtime = "outtime";    
    private ProgressDialog progressDialog;    
    private Toast toast;    
    // private int scroll_flag = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups_main);
        
        // 3-29 change by qu 
        // activity = (TextView)findViewById(R.id.activity);
        // lecture = (TextView)findViewById(R.id.lecture);        
        listview = (ListView)findViewById(R.id.listview);
        
        jsonOb = new JSONObject();
        jsonAy = new JSONArray();
        jsonAy2 = new JSONArray();
        msgs = new ArrayList<HashMap<String,Object>>();
        
        
        // ��ȡIntent����
        GetStatus = this.getIntent();
        // ��ȡIntent�е�Bundle����
        Getid = GetStatus.getExtras();
        // ��ȡBundle�����е�
        page_id = Getid.getLong("page_id");  	
		// ����ͷ��ͼƬ
		setheadimg();
		// ��ArrayList����ֵ		
        msgsget(0);
        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.groups_footerview, null);
        footerLayout = (LinearLayout) view.findViewById(R.id.footerlayout);
        footerLayout.setVisibility(View.GONE);
        
        // 8-23 add by qu
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
     
        adapter = new SimpleAdapter(GroupsMainActivity.this, msgs, R.layout.groups_listview,
        									new String[]{"headimg","msgscontent","msgstime"},
        										new int[]{R.id.headimg,R.id.msgcontent,R.id.msgtime});
        listview.addFooterView(view);
	    listview.setAdapter(adapter);
	    listview.setOnScrollListener(this);
	    
	    
	    progressDialog = new ProgressDialog(this);
	    progressDialog.setTitle("努力加载ing");
	    progressDialog.setMessage("人人网API调皮了。。。");
	    progressDialog.setCanceledOnTouchOutside(false);
	    progressDialog.show();
	    
	    new Thread() {
	    	public void run() {
	    		
	    		try {
	    			Thread.sleep(10);
	    		}catch(Exception e){
	    			Log.i("thread", "sleep");
	    		}
	    		
	    	    try {
	            	jsonAy = apiClient.getInstance().getStatusService().getStatuses(page_id,1,10);
	            	if ( jsonAy == null ){
	            		throw new RuntimeException("outtime");
	            	}
	            	jsonAy2 = jsonAy;
	            	msgsget(0);
	            	
	    	    	Message message = new Message();
        		  	message.what = 3;
        		  	handler.sendMessage(message);
        		  	
	            }catch(Exception e){
	            	e.printStackTrace();
	            	Log.i("Exception", e.getMessage());
	            	if ( e.getMessage().equals(Err_outtime) ){
	            		toast.setText("网络超时了...T^T");
	            		toast.show();	            		
	            	}
	            }finally{
	            	handler.sendEmptyMessage(101);
	            }
	    	}
	    }.start();
	    
        /*
        // 活动和讲座的解析
	    activity.setOnClickListener(new TextView.OnClickListener(){
        	public void onClick(View v){
        		// ״ֵ̬ ���������˻��ť
        		mode = "【活动】"; mode2 = 1;
        		Log.i("button", "activity");
        		// ���ArrayList���� 
        		msgs.clear();
        		
        		Updateitem(0,mode);
        		// ��̬����ListView
        		adapter.notifyDataSetChanged();        		        	
        		if(msgs.size() == 0) {// ArrayList�����СΪ0 ����ǰListViewû������
        			toast.setText("目前没有活动");
        			toast.show();
        		}
        	}
        });
        
        // lectureimgbtn.setOnClickListener(new Button.OnClickListener(){
	    lecture.setOnClickListener(new TextView.OnClickListener(){
        	public void onClick(View v){ 
        		mode = "【讲座】"; mode2 = 2;
        		Log.i("button", "lecture");
        		msgs.clear();
        		Updateitem(0,mode);
        		adapter.notifyDataSetChanged();     		        	
        		if(msgs.size() == 0){
        			toast.setText("目前没有讲座");
        			toast.show();
        		}
        	}
        });
	    */
	    
    }
    
    private void Updateitem(int start,String arg){
    	for(int i = start;i < jsonAy2.size();i++){
    		// �õ�JSONArray�е�Ԫ�� ��ת��ΪJSONObject����
			jsonOb = (JSONObject)jsonAy2.get(i);
			// ��JSONObject����ת��ΪString����
			message = (String)jsonOb.get("message");
			// ���String��ǰ�ĸ��ַ�
			Judge = message.substring(0, 4);
			
			if(Judge.equalsIgnoreCase(arg)){
				// �����������String�ַ��е�4�������ַ���뵽ArrayList������
				map = new HashMap<String,Object>();
				map.put("headimg", headimg);
				map.put("msgscontent",message.substring(4));
				map.put("msgstime", time);
				msgs.add(map);
			}
		}
    	
    	
    }
    private void msgsget(int start){
		for(numofmsg = start; numofmsg < jsonAy2.size(); numofmsg++){
			// �õ�JSONArray�е�Ԫ�� ����ת��ΪJSONObject���� �ٽ�JSONObject����ת��ΪString����
			root_message = (String)((JSONObject)jsonAy2.get(numofmsg)).get("root_message");
			time = (String)((JSONObject)jsonAy2.get(numofmsg)).get("time");
			map = new HashMap<String,Object>();
			map.put("headimg", headimg);
			// root_message == null ˵������ת����״̬ 
			if(root_message == null){
				message = (String)((JSONObject)jsonAy2.get(numofmsg)).get("message");
				// 2012-9-3 changed by lixin		
					map.put("msgscontent",message);				
			}
			else 
				map.put("msgscontent","[ת]" + root_message);
			map.put("msgstime", time);
			msgs.add(map);
		}

	} 
	private void setheadimg(){
		// 8-23 change by qu
		if(page_id == 600490284)
			headimg = R.drawable.shetuan_xxsh;
		else if(page_id == 600889745)
			headimg = R.drawable.shetuan_sl;
		else if(page_id == 601003549)
			headimg = R.drawable.shetuan_qzl;
		else if(page_id == 601017224)
			headimg = R.drawable.shetuan_xkx;
		else if(page_id == 600907477)
			headimg = R.drawable.shetuan_qcny;
	}

	// 8-23 change by qu
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
        lastItem = firstVisibleItem + visibleItemCount;
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
		
		// 8-23 add by qu
		// ���ڵ��ڣ�����Ϊϵͳ��footviewҲ�������ڣ�ʵ�ʱ�adapter��һ��
		if ( lastItem >= adapter.getCount() && scrollState == OnScrollListener.SCROLL_STATE_IDLE ){
			
			Log.i("count", String.valueOf(lastItem)+" "+String.valueOf(adapter.getCount()));
			
			footerLayout.setVisibility(View.VISIBLE);	        	
        	//���߳�ȥ�����������
        	if (mThread == null || !mThread.isAlive()){
        		mThread = new Thread(){
        			public void run() {
        				try {
						jsonAy = apiClient.getInstance().getStatusService().getStatuses(page_id,Gpage+1,10);
        				}catch(Exception e){
        					Log.i("Exception", e.getMessage());
        					if ( e.getMessage().equals(Err_outtime) ){
        						toast.setText("网络超时了...T^T");
        						toast.show();
        					}
        				}
						if ( jsonAy.size() == 0 ){
							toast.setText("木有更多了...");
							toast.show();
						}
						else {
							jsonAy2.addAll(jsonAy);
						}
					
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
        	     	}
        		 };
        		mThread.start();               
            }
		}
		
	}

	private Handler handler = new Handler() {
    	public void handleMessage(Message msg) {
	    	if(msg.what == 1)
	    	{
	    		
	    		switch(mode2){
		    	case 0:
		    		msgsget(Gpage*10);
		    		break;
		    	case 1:
		    		Updateitem(Gpage*10,mode);
		    		break;
		    	case 2:
		    		Updateitem(Gpage*10,mode);
		    		break;
		    	default:
		    		break;
	    		}
	    		Gpage++;
	    		Log.i("Gpage", String.valueOf(Gpage));
	    		// listview.removeFooterView(view);
	    		footerLayout.setVisibility(View.INVISIBLE);
	    		//����ˢ��Listview��adapter�������
	    		adapter.notifyDataSetChanged();  
	    	}
	    	else if(msg.what == 2){		// ����Ϣ��ʽ���ص�һ�����
	    		try {
	            	jsonAy = apiClient.getInstance().getStatusService().getStatuses(page_id,1,10);
	            	jsonAy2 = jsonAy;
	            }catch(Exception e){
	            	Log.i("Exception", e.getMessage());
	            	if ( e.getMessage().equals(Err_outtime) ){
	            		// ���ӳ�ʱ
	            		
	            		toast.setText("网络超时了...T^T");	            		
	            		toast.show();
	            	}
	            }
	    		//����ˢ��Listview��adapter�������
	    		adapter.notifyDataSetChanged();
	    	}
	    	else if(msg.what == 3){		// ����listview�����
	    		adapter.notifyDataSetChanged();
	    	}
	    	else if(msg.what == 101){	// ������ʾ����˳�
	    		progressDialog.dismiss();
	    	}
    	}
    	
    };
}



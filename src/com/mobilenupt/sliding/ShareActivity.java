package com.mobilenupt.sliding;

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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.imax.vmall.sdk.android.common.adapter.ServiceCallback;
import com.imax.vmall.sdk.android.entry.CommonService;
import com.imax.vmall.sdk.android.huawei.share.ShareStatus;
import com.mobilenupt.adapter.MenuListAdapter;
import com.mobilenupt.ui.MenuHorizontalScrollView;
import com.mobilenupt.ui.SizeCallBackForMenu;

public class ShareActivity extends BaseActivity{
	
	private static final String TAG = "ShareActivity";
	//SDK的公共服务类
	public CommonService mCommon;
	//SDK分享类
	public ShareStatus mShare;

	private Button button_share;
	private ImageButton imagebutton_share;
    // ----------------------------------------------
    private ListView menuList;
    private View update;
    private Button menuBtn;
    private MenuListAdapter menuListAdapter;

    //跳转到文件选择页面和鉴权页面的标识
    // private static final int REQUEST_CODE_FILE = 0;
    private static final int REQUEST_CODE_AUTH = 1;
    
    //API调用结果标识
    private static final int TYPE_INIT_SUCCESS = 0;
    private static final int TYPE_INIT_FAILD = 1;
    private static final int TYPE_SHARE_SUCCESS = 2;
    private static final int TYPE_SHARE_FAILD = 3;
    private static final int TYPE_OAUTH_SUCCESS = 4;
    private static final int TYPE_OAUTH_FAILD = 5;
    private static final int TYPE_OAUTH_CANCEL = 6;
    
  //处理异步请求的响应
    Handler MessageHandler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch(msg.what){
    		case TYPE_INIT_SUCCESS:
    			// Toast.makeText(ShareActivity.this, "SDK 初始化成功!", Toast.LENGTH_SHORT).show();
    			break;
    		case TYPE_INIT_FAILD:
    			// Toast.makeText(ShareActivity.this, "SDK 初始化失败!", Toast.LENGTH_SHORT).show();
    			break;
    		case TYPE_SHARE_SUCCESS:
    			Toast.makeText(ShareActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
    			break;
    		case TYPE_SHARE_FAILD:
    			Toast.makeText(ShareActivity.this, "分享出错!", Toast.LENGTH_SHORT).show();
    			break;
    		case TYPE_OAUTH_SUCCESS:
				Toast.makeText(ShareActivity.this, "用户授权成功!", Toast.LENGTH_SHORT).show();
				// 授权成功后分享状态
				// statusShare("renren");
				break;
    		case TYPE_OAUTH_FAILD:
				Toast.makeText(ShareActivity.this, "用户授权出错!", Toast.LENGTH_SHORT).show();
    			break;
    		case TYPE_OAUTH_CANCEL:
				Toast.makeText(ShareActivity.this, "用户取消授权!", Toast.LENGTH_SHORT).show();
    			break;
    			default:break;
    		}
    	};
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		setContentView(inflater.inflate(R.layout.menu_scroll_view, null));
		this.scrollView = (MenuHorizontalScrollView) findViewById(R.id.mScrollView);
		this.menuListAdapter = new MenuListAdapter(this, 7);
		this.menuList = (ListView) findViewById(R.id.menuList);
		this.menuList.setAdapter(menuListAdapter);
	
		this.update = inflater.inflate(R.layout.update_page, null);
		this.menuBtn = (Button) this.update.findViewById(R.id.update_menuBtn);
		this.menuBtn.setOnClickListener(onClickListener);
	
		View leftView = new View(this);
		leftView.setBackgroundColor(Color.TRANSPARENT);
		final View[] children = new View[] { leftView, update };
		this.scrollView.initViews(children, new SizeCallBackForMenu(
			this.menuBtn), this.menuList);
		this.scrollView.setMenuBtn(this.menuBtn);
		newToast("", false);
		//-------------------------
		// button_share = (Button) findViewById(R.id.update_check);
		// button_share.setOnClickListener(onClickListener);
		imagebutton_share = (ImageButton)findViewById(R.id.imgbutton_sharetoqqzone);
		imagebutton_share.setOnClickListener(onClickListener);
    }
    
    private OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View arg0) {
			if (arg0 == menuBtn)
				scrollView.clickMenuBtn();
			else if (arg0 == button_share) {
				sdkInit();
				oauth("renren");				
			}else if (arg0 == imagebutton_share) {
				sdkInit();
				oauth("renren");
			}
		}
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	return super.onKeyDown(keyCode, event);
    }
    protected void onDestroy() {
    	super.onDestroy();
    	//销毁SDK，释放资源
    	CommonService.getInstance().destory();
    }
    
  //第一步：初始化SDK
    public void sdkInit(){
        //获取SDK公共服务类实例
        mCommon = CommonService.getInstance();
        
        //初始化SDK,初始化成功后再调用其他接口
        //appID:APP在IMAX注册应用时分配的APP ID
        //appKey:APP在IMAX注册应用时分配的APP KEY
        mCommon.init(this, "7906882764", "83dc1df391d682897b8b867f09374879", new ServiceCallback(){
        	public void onComplete(String result) {
        		
        		Log.v(TAG,"-----sdk init success-----");
        		//初始化SDK成功，result是IMAX返回的token
        		String token = result; //或者使用//String token = CommonService.getImaxToken();
        		Log.v(TAG,"imax_token = "+token);
        		//通知主线程初始化成功，不要在回调方法中执行会阻塞线程的操作
        		Message msg = new Message();
        		msg.what = TYPE_INIT_SUCCESS;
        		MessageHandler.sendMessage(msg);
        	}
        	public void onError(String message) {
        		//发生网络错误或者获取IMAX TOKEN失败，message包含错误码
        		//提示SDK初始化失败
        		Log.v(TAG,"-----sdk init faild-----");
        		Log.v(TAG,"error message = "+message);
        		//通知主线程初始化失败，不要在回调方法中执行会阻塞线程的操作
        		Message msg = new Message();
        		msg.what = TYPE_INIT_FAILD;
        		MessageHandler.sendMessage(msg);
        	}
        });	
    }
    
    //第二步：OAuth授权
    //以下参数需要APP自己到第三方平台注册自己的应用获取
    public void oauth(String ep_id){
    	//用WebView控件打开OAuth鉴权页面
    	Intent it = new Intent(ShareActivity.this, AuthActivity.class);
    	it.putExtra("ep_id", ep_id);
    	//APP在新浪微博开放平台注册应用时分配的参数
    	if(ep_id.equals("sina")){
    		it.putExtra("app_key", "125298474");
    		it.putExtra("app_secret", "0b45b03c76b9e84e401ecf7135beede1");
    		it.putExtra("redirect_url", "http://mobilenupt.sinaapp.com/");
    	}
    	//APP在腾讯微博开放平台注册应用时分配的参数
    	else if(ep_id.equals("tencent")){
    		it.putExtra("app_key", "100431367");
    		it.putExtra("app_secret", "8cdebca23c3177b201c17d1321a2a93c");
    		it.putExtra("redirect_url", "http://mobilenupt.sinaapp.com/");
    	}
    	//APP在人人网开放平台注册应用时分配的参数
    	else if(ep_id.equals("renren")){
    		it.putExtra("app_key", "c89b77564dc34996bb1d1c45ea97a4fc");
    		it.putExtra("app_secret", "d74184211506445b9dd3bdb1ac9a7944");
    		it.putExtra("redirect_url", "http://mobilenupt.sinaapp.com/");
    	}
    	//APP在豆瓣开放平台注册应用时分配的参数
    	else if(ep_id.equals("douban")){
    		it.putExtra("app_key", "06da0ba739b966ae148e22f5ad2c340a");
    		it.putExtra("app_secret", "83a7d621037944fa");
    		it.putExtra("redirect_url", "http://mobilenupt.sinaapp.com/");
    	}
    	//APP在开心网开放平台注册应用时分配的参数
    	else if(ep_id.equals("kaixin")){
    		it.putExtra("app_key", "689920286369b6fcdd10fd4e56ef55c9");
    		it.putExtra("app_secret", "a02ac05cb710b3daea1a09fd3585ae12");
    		it.putExtra("redirect_url", "http://mobilenupt.sinaapp.com/");
    	}
    	
    	startActivityForResult(it, REQUEST_CODE_AUTH);
    }
    
    // 选取图片的回调函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if(requestCode == REQUEST_CODE_AUTH){
    		if(data != null){
	    		String state = data.getStringExtra("auth_state");
	    		if(state != null){
	    			if(state.equals("ok")){//用户授权成功
	    				String access_token = data.getStringExtra("access_token");
	    				long expires_in = data.getLongExtra("expires_in", 0);
	    				String uid = data.getStringExtra("uid");
	    				Log.v(TAG,"access_token = "+access_token+", expires_in = "+expires_in+", uid = "+uid);
	    				MessageHandler.sendEmptyMessage(TYPE_OAUTH_SUCCESS);
	    			}else if(state.equals("error")){//用户授权失败
	    				String error_code = data.getStringExtra("error_code");
	    				String error_msg = data.getStringExtra("error_msg");
	    				Log.v(TAG,"error_code = "+error_code+", error_msg = "+error_msg);
	    				MessageHandler.sendEmptyMessage(TYPE_OAUTH_FAILD);
	    			}else if(state.equals("cancel")){//用户取消了授权
	    				MessageHandler.sendEmptyMessage(TYPE_OAUTH_CANCEL);
	    			}	
	    		}
    		}
    	}
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    //第三步：分享文字或图片到第三方平台
    public void statusShare(String ep_id){
        //获取分享类的实例
        mShare = ShareStatus.getInstance();
		/**
		 * 分享的内容不能为空，否则会分享失败；相同的内容重复分享也会失败
		 */
		String content = "#掌上南邮#查课表，搜图书，看通知，一切尽在手中，Nupter，学子必备!....南邮在手，天下我有。还等什么，猛戳下载！！http://mobilenupt.sinaapp.com/download.php";
		
		
		/**
		 * 一次分享到多家平台，之间用逗号隔开，至少需要填写一家
		 * 分享的内容不能为空，否则会分享失败，分享内容的最大长度请参考第三方平台的说明
		 * 分享的图片最大不能超过3M，APP可在上传前对图片进行裁剪；如果只分享文字微博则此参数传入null
		 * 请确保在调用SDK初始化接口成功后再调用其他接口！！！
		 */
        mShare.share(ep_id, content, null, new ServiceCallback(){
        	public void onComplete(String result) {
        		/**
        		 * 请求正常响应，服务器返回JSON格式的响应
        		 * APP需要解析result，判断分享结果
        		 * 如果请求的参数错误则会返回形式为：{"ret":"2","msg":"缺失必选参数(content),请参考API文档"}
        		 * 如果分享成功或分享失败则返回形式为：{"renren":{"ret":"1","msg":"失败"},"sina":{"ret":"5","msg":"接口调用频率超过上限"},"kaixin":{"ret":"17","msg":"开心账号绑定已经过期，请重新绑定"},"douban":{"ret":"0","msg":"成功"},"tencent":{"ret":"17","msg":"腾讯微博账号绑定已经过期，请重新绑定"}}
        		 */
        		Log.v(TAG,"-----statusShare success-----");
        		Log.v(TAG,"response = "+result);
        		//提示分享结果
        		//通知主线程分享成功，不要在回调方法中执行会阻塞线程的操作
        		Message msg = new Message();
        		msg.what = TYPE_SHARE_SUCCESS;
        		MessageHandler.sendMessage(msg);
        	}
        	public void onError(String message) {
        		//HTTP请求发生异常错误
        		//通知主线程分享失败，不要在回调方法中执行会阻塞线程的操作
        		Message msg = new Message();
        		msg.what = TYPE_SHARE_FAILD;
        		MessageHandler.sendMessage(msg);
        	}
        });
    }
	
}

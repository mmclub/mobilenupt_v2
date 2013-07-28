package com.mobilenupt.sliding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebChromeClient;
import android.widget.ProgressBar;

import com.mobilenupt.sliding.R;

import com.imax.vmall.sdk.android.oauthv2.OAuth2;
import com.imax.vmall.sdk.android.oauthv2.OAuth2Listener;


public class AuthActivity extends Activity{
	private static final String TAG = "AuthActivity";
	public WebView webView;
	ProgressBar progressBar;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//APP在第三方平台注册应用时分配的参数
		String ep_id = getIntent().getStringExtra("ep_id");
		String app_key = getIntent().getStringExtra("app_key");
		String app_secret = getIntent().getStringExtra("app_secret");
		String redirect_url = getIntent().getStringExtra("redirect_url");
		final OAuth2 oauth = OAuth2.create(ep_id, app_key, app_secret, redirect_url);
		
		//APP控制WebView的布局和外观    
        setContentView(R.layout.auth_activity);
        webView = (WebView)findViewById(R.id.webview);
        
        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int position) {
            	progressBar.setProgress(position);
                if (position == 100) {
                	progressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, position);
            }

        });
        
        
        //OAuth 2.0 鉴权
		oauth.authorize(webView, new OAuth2Listener(){
			public void onCancel() {
				
				Log.v(TAG,"onCancel");
				//关闭WebView控件
				webView.destroy();
				//通知主线程用户取消了授权
				Intent it = new Intent();
				it.putExtra("auth_state", "cancel");
				setResult(RESULT_OK, it);
				//返回MainActivity
				finish();
			}
			public void onComplete() {
				Log.v(TAG,"onComplete");
				String access_token = oauth.getToken().getAccessToken();
				long expires_in = oauth.getToken().getExpiresIn();
				String uid = oauth.getUid();
				Log.v(TAG,"access_token = "+access_token+", expires_in = "+expires_in+", uid = "+uid);
				
				//关闭WebView控件
				webView.destroy();
				//通知主线程用户授权成功
				Intent it = new Intent();
				it.putExtra("auth_state", "ok");
				it.putExtra("access_token", access_token);
				it.putExtra("expires_in", expires_in);
				it.putExtra("uid", uid);
				setResult(RESULT_OK, it);
				//返回MainActivity
				finish();
			}
			public void onError() {
				Log.v(TAG,"onError");

				String error_code = oauth.getLastError(); //获取错误码
				String error_msg = oauth.getLastErrorMsg(); //获取错误描述信息
				Log.v(TAG,"error_code = "+error_code+", error_msg = "+error_msg);
				
				//关闭WebView控件
				webView.destroy();
				//通知主线程用户授权出错
				Intent it = new Intent();
				it.putExtra("auth_state", "error");
				it.putExtra("error_code", error_code);
				it.putExtra("error_msg", error_msg);
				setResult(RESULT_OK, it);
				//返回MainActivity
				finish();
			}
		});		
	}
}
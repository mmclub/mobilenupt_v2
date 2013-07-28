package com.mobilenupt.welcome;

import com.mobilenupt.service.*;
import com.mobilenupt.sliding.Library;
import com.mobilenupt.sliding.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/*
 * 
 * 欢迎界面，第一屏
 */
public class Welcome extends Activity {
	
    int flag = 1;
    CheckUpdate version_check;

    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		version_check = new CheckUpdate();
		version_check.GetLocalVersion(this);
		
		new Thread() {
		    public void run() {
				flag = version_check.checkVersion();
				flaghandler.sendEmptyMessage(flag);
		    }
		}.start();

    }

    Handler flaghandler = new Handler() {
		public void handleMessage(Message msg) {
			
			Intent intentToLibrary = new Intent(Welcome.this, Library.class);
			
		    if (msg.what == 0) {	// 有更新
		    	
		    	String str_result = version_check.getpostresult();
				String new_vcode = version_check.getNewVcode();
		    	 
				intentToLibrary.putExtra("flag", 0);
				intentToLibrary.putExtra("str_result", str_result);
				intentToLibrary.putExtra("new_vcode", new_vcode);
				
		    } else if (msg.what == 1) {
				// 检查更新，没有新版本
		    } else if (msg.what == -1){
		    	// 没有网络或其他异常时主动sleep 1s，防止欢迎界面一闪而过
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }
		    
		    startActivity(intentToLibrary);
			Welcome.this.finish();
		}
    };

}

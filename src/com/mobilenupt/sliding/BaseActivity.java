package com.mobilenupt.sliding;

import com.mobilenupt.ui.MenuHorizontalScrollView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

public class BaseActivity extends Activity{

	MenuHorizontalScrollView scrollView;
    ProgressDialog progressdialog;
    private Toast toast;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    }
	
	public MenuHorizontalScrollView getScrollView() {
    	return scrollView;
    }

    public void setScrollView(MenuHorizontalScrollView scrollView) {
    	this.scrollView = scrollView;
    }
	
    /**
     * @param msg The msg to display
     * @param flag whether to show immediately
     */
    public void newToast(String msg, boolean flag) {
    	toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
    	if (flag) toast.show();
    }
    public void showToast(String msg) {
    	toast.setText(msg);
    	toast.show();
    }
    /**
     * @param title the dialog's title
     * @param msg the dialog's msg
     */
    public void newProgressdialog(String title, String msg) {
    	progressdialog = new ProgressDialog(this);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setMessage(msg);
		progressdialog.setIndeterminate(false);
		progressdialog.setCanceledOnTouchOutside(false);
		if (title != null && title != "" ){
			progressdialog.setTitle(title);
		}
    }
    
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		    if (MenuHorizontalScrollView.menuOut == false)
		    	this.scrollView.clickMenuBtn();
		    else{
		    	Builder exit_dialog = new Builder(this);
		    	exit_dialog.setTitle("Nupter");
		    	exit_dialog.setMessage("你确定要退出了么？");
		    	exit_dialog.setPositiveButton("不玩了", new android.content.DialogInterface.OnClickListener() {
				
		    		public void onClick(DialogInterface dialog, int which) {
		    			finish();
		    		}
		    	});
		    	exit_dialog.setNegativeButton("再玩会", null);
		    	exit_dialog.create().show();
		    }
		    return true;
		}
		return super.onKeyDown(keyCode, event);
    }
	
}

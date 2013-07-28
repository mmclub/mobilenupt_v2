package com.mobilenupt.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class CheckUpdate {
	
	HttpURLConnection check_connection;
	String v_code;
	String v_str = "";
	String postresult = "";
	
	private void initConnect()
	{
		try {
			URL check_url = new URL("http://mobilenupt.sinaapp.com/version_check.php");
			check_connection = (HttpURLConnection)check_url.openConnection();
			check_connection.setRequestMethod("POST");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		check_connection.setDoOutput(true);
		check_connection.setDoInput(true);
		check_connection.setConnectTimeout(3000);
		check_connection.setReadTimeout(3000);
		check_connection.setUseCaches(false);
	}
	
	public int checkVersion()
	{
		try {
			initConnect();
			check_connection.connect();
			// send
			DataOutputStream check_out = new DataOutputStream(check_connection.getOutputStream());
			check_out.writeBytes(v_str);
			// recv
			InputStream check_in = check_connection.getInputStream();
			BufferedReader buffercheck_in = new BufferedReader(new InputStreamReader(check_in));
			
			// 检查网页返回，如出错直接返回
			String line = buffercheck_in.readLine();
			if (!line.equals("version")) {
				return -1;
			}
			
			// 检查最新版本
			String latestVcode = buffercheck_in.readLine();
			if (v_code.equals(latestVcode))
				return 1;
			else {
				v_code = latestVcode;
				line = buffercheck_in.readLine();
				while(line != null)
				{
					postresult += line;
					postresult += "\n";
					line = buffercheck_in.readLine();	
				}
				return 0;
			}
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
		
	}
	
	public String getNewVcode() {
		return v_code;
	}
	
	public void GetLocalVersion (Context context)
	{
		PackageManager check_pm = context.getPackageManager();
		try {
			PackageInfo check_pi = check_pm.getPackageInfo(context.getPackageName(), 0);
			v_code = check_pi.versionName;
			v_str = "vcode="+v_code;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String getpostresult ()
	{
		return postresult;
	}
}

package com.mobilenupt.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import android.content.Context;

/**
 * 读取和保存文件的基础组件类
 * @author tianshan
 * @version 1
 */
public class FileService {
	private Context context;
	
	public FileService(Context c){
		this.context = c;
	}
	// stream read
	private String streamRead(InputStream is) throws IOException
	{
		int buffersize = is.available();
		byte buffer[] = new byte[buffersize];
		is.read(buffer);
		is.close();
		String result = EncodingUtils.getString(buffer, "utf-8");
		return result;
	}
	
	// read from sdcard
	public String readSDCardFile(String path) throws IOException
	{
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		String result = streamRead(fis);
		return result;
	}
		
	// read from date dir
	public String readDateFile(String filename) throws IOException
	{
		FileInputStream fis = context.openFileInput(filename);
		String result = streamRead(fis);
		return result;
	}
	
	// read from assets
	public String readAssetsFile(String filename) throws IOException
	{
		InputStream is = context.getResources().getAssets().open(filename);
		String result = streamRead(is);
		return result;
	}
	
	// read from raw
	public String reeadRawFile(int fileId) throws IOException
	{
		InputStream is = context.getResources().openRawResource(fileId);
		String result = streamRead(is);
		return result;
	}
	
	// write to sdcard
	public void writeSDCardFile(String path, byte[] buffer) throws IOException
	{
		File file = new File(path);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(buffer);
		fos.close();
	}
	
	// write to date dir
	public void writeDateFile(String filename, byte[] buffer) throws IOException
	{
		FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
		fos.write(buffer);
		fos.close();
	}
}

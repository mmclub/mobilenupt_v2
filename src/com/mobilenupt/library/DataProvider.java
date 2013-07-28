package com.mobilenupt.library;

import android.util.Log;

public class DataProvider {
	private String postData;
	
	public String getData() {
		return postData;
	}
	
	public String setData(String keywords,String page) {
		String str1 = "location=ALL&title=";
		String str2 = "&doctype=ALL&lang_code=ALL&match_flag=forward&displaypg=20&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=no&page=";
		postData = str1 + keywords + str2 + page;
		Log.v("page", page);
		return postData;
	}
}	

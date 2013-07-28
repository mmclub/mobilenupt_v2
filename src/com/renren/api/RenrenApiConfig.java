package com.renren.api;


public class RenrenApiConfig {
	
	// api 请求预定义 字符串
	public static String renrenApiUrl     = "http://api.renren.com/restserver.do";
    public static String renrenApiVersion = "1.0";
    public static String renrenApiKey     = "e4e12cd61ab542f3a6e45fee619c46f3";
    public static String renrenApiSecret  = "1e7a17db78e74ed6964601ab89ea6444";
    public static String renrenAppID      = "204763";
	
    // http请求固定字符串
    public static final String USER_AGENT = "Renren Java SDK v2.0";
    // 没发现这货是干嘛用的。。。
	public static final String MD5_USER_AGENT = "49e70de73da2f12a8edf5427201089f8";//Renren Java SDK v2.0��md5
}

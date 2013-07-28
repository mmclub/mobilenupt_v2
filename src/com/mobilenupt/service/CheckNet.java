package com.mobilenupt.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNet {
    public static boolean checkNetwork(Context context) {
	try {
	    ConnectivityManager connectivity = (ConnectivityManager) context
		    .getSystemService(Context.CONNECTIVITY_SERVICE);

	    if (connectivity != null) {

		NetworkInfo info = connectivity.getActiveNetworkInfo();

		if (info == null || !info.isAvailable())
		    return false;
		else
		    return true;
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return false;
    }
}

package com.dvr.mailpush;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



public class Utilidades {

	public static boolean App(Context context, String app) {
	    boolean ok = false;
	    PackageManager pm = context.getPackageManager();
	 
	    Intent intent = new Intent(Intent.ACTION_MAIN, null);
	    intent.addCategory(Intent.CATEGORY_LAUNCHER);
	 
	    List<ResolveInfo> list = pm.queryIntentActivities(intent,
	            PackageManager.PERMISSION_GRANTED);
	    
	    for (ResolveInfo rInfo : list) {
	        if (rInfo.activityInfo.applicationInfo.packageName.toString()
	                .equalsIgnoreCase(app))
	            ok = true;
	    }
	    return ok;
	}
	
	
	public static String fecha() {
		Calendar hoy = Calendar.getInstance();
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
		String c = formato.format(hoy.getTime()); 
		return c;
	}
	
	public static boolean EmailValid(String email) {
		boolean isValid = false;

		String expression = "[a-zA-Z0-9._-]+@gmail.com";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}
	
	public static boolean Internet(Context context) {
		boolean bConectado = false;
		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] redes = connec.getAllNetworkInfo();
		for (int i = 0; i < 2; i++) {
			if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
				bConectado = true;
			}
		}
		return bConectado;
	}
}

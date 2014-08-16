package com.dvr.mailpush;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;



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

}

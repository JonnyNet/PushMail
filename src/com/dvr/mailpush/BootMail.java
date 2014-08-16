/**
 * 
 */
package com.dvr.mailpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

 
public class BootMail extends BroadcastReceiver {
	
	@Override
    public void onReceive(Context context, Intent intent) 
    {
		Intent service = new Intent(context, Servicio_Gmail.class);
		
		service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(service);
    }
	
}

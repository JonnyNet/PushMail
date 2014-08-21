package com.dvr.mailpush;

import java.util.List;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.CalendarContract.Events;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;


public class Servicio_Gmail extends Service implements EstadoGmail {
	AsynGmail gmail;
	private NotificationManager nm;
	int id = 0;

	@Override
	public void onCreate() {
		if (gmail != null) {
			gmail=null;
		}
		gmail = new AsynGmail(Servicio_Gmail.this, this);
		gmail.execute();
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_REDELIVER_INTENT;
	}

	@Override
	public void onDestroy() {
		gmail.cancel(true);
		gmail = null;
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	public void Alerta(String Alert) {
		Notification notif;
		Intent i = new Intent(this, Acciones.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(Acciones.class);
		stackBuilder.addNextIntent(i);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.alarma)
				.setContentTitle(Alert)
				.setContentIntent(resultPendingIntent)
				.setContentText("Revise sus camaras porfavor")
				.setVibrate(new long[] { 100, 250, 100, 250 })
				.setAutoCancel(true)
				.setOngoing(false)
				.setSound(
						RingtoneManager
								.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.setLights(Color.RED, 0, 1)
				.setTicker("**Notification Arrived!**")
				.setSound(
						Uri.parse("android.resource://" + getPackageName()
								+ "/" + R.raw.alarma))
				.setLargeIcon(
						(((BitmapDrawable) getResources().getDrawable(
								R.drawable.alarma)).getBitmap()));
		notif = mBuilder.build();
		notif.flags |= Notification.FLAG_INSISTENT;
		notif.flags |= Notification.FLAG_AUTO_CANCEL;
		notif.flags |= Notification.FLAG_SHOW_LIGHTS;

		nm.notify(id++, notif);
	}

	@Override
	public void OnEvento(String evento) {
		sendBroadcastMessage(evento);
	}
	
	@Override
	public void Login(boolean login) {
		//si falla manda true
		if (login) {
			sendBroadcastMessage("Login incorrecto");
			gmail.onCancelled();
			stopSelf();
			onDestroy();
		}
		
	}
	
	
	
	@Override
	public void onTaskRemoved(Intent rootIntent) {
		// TODO Auto-generated method stub
		super.onTaskRemoved(rootIntent);
	}
	

	private void sendBroadcastMessage(String arg1) {
		if (ActivityALaVista("com.dvr.mailpush.Acciones")) {
			Intent intent = new Intent(Events._ID);
			intent.putExtra("evento", arg1);
			sendBroadcast(intent);
		}

	}

	public boolean ActivityALaVista(String nombreClase) {
		ActivityManager am = (ActivityManager) Servicio_Gmail.this
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
		String nombreClaseActual = null;
		try {
			ComponentName componentName = null;
			if (taskInfo != null && taskInfo.get(0) != null) {
				componentName = taskInfo.get(0).topActivity;
			}
			if (componentName != null) {
				nombreClaseActual = componentName.getClassName();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
		return nombreClase.equals(nombreClaseActual);
	}

}
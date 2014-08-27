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
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.IBinder;
import android.provider.CalendarContract.Events;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;


public class Servicio_Gmail extends Service implements EstadoGmail {
	private AsynGmail gmail;
	private NotificationManager nm;
	int id = 0;

	@Override
	public void onCreate() {
		SharedPreferences Config = Preferencias();
		boolean save = Config.getBoolean("save", false);
		if (save) {
			String nombre = Config.getString("usuario", "");
			String pass = Config.getString("pass", "");
			String asunto = Config.getString("subject", "");
			gmail = new AsynGmail(Servicio_Gmail.this, this, nombre, pass,
					asunto);
			gmail.execute();

		} else {

		}
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_REDELIVER_INTENT;
	}

	private SharedPreferences Preferencias() {
		SharedPreferences ConfigPreferent = getSharedPreferences(
				"configuraciones", MODE_PRIVATE);
		return ConfigPreferent;
	}

	@Override
	public void onDestroy() {
		gmail.cancel(true);
		gmail = null;
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void Alerta(String Alert) {
		NotificacionBarra();
	}
	
	private void NotificacionBarra(){
		
		SharedPreferences op = Preferencias();
		 boolean vibracion = op.getBoolean("vibra", false);
		 boolean alarma = op.getBoolean("son", false);
		
		Notification notif;
		Intent i = new Intent(this, Acciones.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(Acciones.class);
		stackBuilder.addNextIntent(i);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.logoalertap)
				.setContentTitle("Alerta")
				.setContentIntent(resultPendingIntent)
				.setContentText("Revise sus camaras porfavor")
				.setAutoCancel(true)
				.setOngoing(false)
				.setLights(Color.RED, 0, 1)
				.setTicker("**Notification Arrived!**")
				.setLargeIcon(
						((((BitmapDrawable) getResources().getDrawable(
								R.drawable.logoalertap)).getBitmap())));
		
		if(alarma){
			mBuilder.setSound(
					Uri.parse("android.resource://" + getPackageName()
							+ "/" + R.raw.alert));
		}
		
		if (vibracion) {
			mBuilder.setVibrate(new long[] { 100, 250, 100, 250 });
			
		}
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
		// si falla manda true
		if (login) {
			sendBroadcastMessage("Login incorrecto");
			gmail.onCancelled();
			stopSelf();
			onDestroy();
		}

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
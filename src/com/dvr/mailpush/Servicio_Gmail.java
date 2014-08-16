package com.dvr.mailpush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class Servicio_Gmail extends Service implements EstadoGmail {

	Gmail_Imbox gmail;
	private NotificationManager nm;
	int id = 0;

	@Override
	public void onCreate() {
		SharedPreferences datos = getSharedPreferences("configuraciones",
				Context.MODE_PRIVATE);
		if (datos.getBoolean("save", false)) {
			String usuario = datos.getString("usuario", "");
			String password = datos.getString("pass", "");
			String asunto = datos.getString("subject", "");
			Log.v("servicio iniciando", usuario + " " + password + " " + asunto);
			gmail = new Gmail_Imbox(Servicio_Gmail.this, this, usuario,
					password, asunto);
			gmail.Start();

			nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		} else {
			onDestroy();
		}

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		gmail.Parar();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void Alerta(String Alert) {
		Intent i = new Intent(this, Acciones.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,
				(int) System.currentTimeMillis());
		CharSequence ticker = "Nuevo evento en el dvr";
		CharSequence contentTitle = "ALERTA";
		CharSequence contentText = "Revise sus camaras porfavor";
		Uri defaultSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_ALARM);
		Notification noti = new NotificationCompat.Builder(this)
				.setContentIntent(pendingIntent)
				.setTicker(ticker)
				.setContentTitle(contentTitle)
				.setContentText(contentText)
				.setSmallIcon(R.drawable.alarma)
				.addAction(R.drawable.alarma, ticker, pendingIntent)
				.setVibrate(
						new long[] { 100, 250, 100, 500, 50, 300, 100, 100, 200 })
				.setSound(defaultSound).setLights(Color.RED, 0, 1).build();

		noti.flags = noti.flags | Notification.FLAG_INSISTENT;
		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		nm.notify(id++, noti);

	}

	@Override
	public void Internet(boolean Internet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void Login(boolean login) {
		if (!login) {
			onDestroy();
		}

	}
}

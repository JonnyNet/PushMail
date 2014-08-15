package com.dvr.mailpush;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class Servicio_Gmail extends Service implements EstadoGmail{
	
	Gmail_Imbox gmail;
	
	@Override
	public void onCreate() {
		gmail = new Gmail_Imbox(Servicio_Gmail.this, this, "", "");
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void Alerta(String Alert) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Internet(boolean Internet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Login(boolean login) {
		// TODO Auto-generated method stub
		
	}
	
	

	
}

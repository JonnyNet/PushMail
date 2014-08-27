package com.dvr.mailpush;

import com.dvr.mailpush.sqlite.Admin_db;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

	private final int DURACION_SPLASH = 3000;
	private Admin_db bd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		bd = new Admin_db(this);

		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent intent = new Intent(Splash.this, Acciones.class);
				startActivity(intent);
				finish();
			};
		}, DURACION_SPLASH);
	}

	@Override
	public void onDestroy() {
		bd.Cerrar();
		super.onDestroy();
	}

}

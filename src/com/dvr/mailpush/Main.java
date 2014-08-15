package com.dvr.mailpush;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Main extends ActionBarActivity implements EstadoGmail {
	Gmail_Imbox u;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		u = new Gmail_Imbox(Main.this, this, "jrr1047@gmail.com",
				"jonny300");
		u.Start();

	}

	@Override
	public void Alerta(String Alert) {
		Toast.makeText(this, Alert, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void Internet(boolean Internet) {
		if (!Internet) {
			Toast.makeText(this, "Noy acceso a datos", Toast.LENGTH_SHORT)
					.show();
		}

	}

	@Override
	public void Login(boolean login) {
		if (login) {
			Toast.makeText(this, "login exitozo", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "fallo en el login", Toast.LENGTH_SHORT)
					.show();
			u.Parar();
		}

	}

}

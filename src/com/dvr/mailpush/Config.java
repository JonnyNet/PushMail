package com.dvr.mailpush;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Config extends ActionBarActivity {
	EditText email, password, asunto;
	CheckBox vibracion, alarma;
	Button Guardar;

	private SharedPreferences ConfigPreferent;
	private SharedPreferences.Editor Configeditor;
	private Boolean save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferencias);

		email = (EditText) findViewById(R.id.user);
		password = (EditText) findViewById(R.id.password);
		asunto = (EditText) findViewById(R.id.asunto);
		vibracion = (CheckBox) findViewById(R.id.vir);
		alarma = (CheckBox) findViewById(R.id.sou);
		Guardar = (Button) findViewById(R.id.atras);

		ConfigPreferent = getSharedPreferences("configuraciones", MODE_PRIVATE);
		Configeditor = ConfigPreferent.edit();
		save = ConfigPreferent.getBoolean("save", false);
		CargarPrefes();

		Guardar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Configeditor.putBoolean("save", true);
				Configeditor.putBoolean("vibra", vibracion.isChecked());
				Configeditor.putBoolean("son", alarma.isChecked());
				Configeditor.putString("usuario", email.getText().toString());
				Configeditor.putString("subject", asunto.getText().toString());
				Configeditor.putString("pass", password.getText().toString());
				Configeditor.commit();
				
				InputMethodManager tecladoVirtual = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				tecladoVirtual.hideSoftInputFromWindow(v.getWindowToken(), 0);
				
				Intent intent = new Intent("android.intent.action.ACCIONES");
				startActivity(intent);
				
				Intent s = new Intent(Config.this, Servicio_Gmail.class);
				startService (s);

			}
		});
	}

	private void CargarPrefes() {

		if (save)
			email.setText(ConfigPreferent.getString("usuario", ""));
		password.setText(ConfigPreferent.getString("pass", ""));
		asunto.setText(ConfigPreferent.getString("subject", ""));
		vibracion.setChecked(ConfigPreferent.getBoolean("vibra", false));
		alarma.setChecked(ConfigPreferent.getBoolean("son", false));
	}

}

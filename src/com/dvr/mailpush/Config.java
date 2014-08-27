package com.dvr.mailpush;

import javax.mail.MessagingException;
import javax.mail.Store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class Config extends Activity implements OnFocusChangeListener,OnClickListener {
	EditText email, password, asunto;
	CheckBox vibracion, alarma;
	Button Guardar, menu, iniciar;
	TextView mensaje;
	GMailReader validar ;

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
		mensaje = (TextView) findViewById(R.id.mensaje);
		Guardar = (Button) findViewById(R.id.guardar);
		menu = (Button) findViewById(R.id.menu);
		iniciar = (Button) findViewById(R.id.iniciar);
		
		ConfigPreferent = getSharedPreferences("configuraciones", MODE_PRIVATE);
		Configeditor = ConfigPreferent.edit();
		save = ConfigPreferent.getBoolean("save", false);
		CargarPrefes();
		
		password.setOnFocusChangeListener(this);
		email.setOnFocusChangeListener(this);
		asunto.setOnFocusChangeListener(this);
		iniciar.setEnabled(false);
		Guardar.setOnClickListener(this);
		menu.setOnClickListener(this);
		iniciar.setOnClickListener(this);
		
		Vibrator v =  (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); 
		if (!v.hasVibrator()) {
			vibracion.setEnabled(false);
		}
	}

	private void CargarPrefes() {

		if (save)
			email.setText(ConfigPreferent.getString("usuario", ""));
		password.setText(ConfigPreferent.getString("pass", ""));
		asunto.setText(ConfigPreferent.getString("subject", ""));
		vibracion.setChecked(ConfigPreferent.getBoolean("vibra", false));
		alarma.setChecked(ConfigPreferent.getBoolean("son", false));
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

		if (v == email && hasFocus == false
				&& Utilidades.EmailValid(email.getText().toString())) {
			Mensaje("");
			email.setBackgroundResource(R.drawable.edit_true);
			Conectar();
		} else if (v == email && hasFocus == false
				&& email.getText().toString().equals("")) {
			email.setBackgroundResource(R.drawable.edit_error);
			Mensaje("Email Campo Requerido");
		}else if(!Utilidades.EmailValid(email.getText().toString())){
			email.setBackgroundResource(R.drawable.edit_error);
			Mensaje("Formato de Correo Invalido");
			iniciar.setEnabled(false);
		}

		if (v == password && hasFocus == false
				&& password.getText().toString().length() > 7) {
			password.setBackgroundResource(R.drawable.edit_true);
			Conectar();
		} else if (v == password && hasFocus == false
				&& password.getText().toString().length() < 8) {
			password.setBackgroundResource(R.drawable.edit_error);
			Mensaje("Contraseña Invalida");
			iniciar.setEnabled(false);
		}

		if (v == asunto && hasFocus == false
				&& !asunto.getText().toString().equals("")) {
			asunto.setBackgroundResource(R.drawable.edit_true);
			OcultarTeclado(v);
		} else if (v == asunto  && hasFocus == false
				&& asunto.getText().toString().equals("")) {
			asunto.setBackgroundResource(R.drawable.edit_error);
			Mensaje("Asunto Campo requerido");
			OcultarTeclado(v);
		}

	}
	
	private void Mensaje(String ms){
		mensaje.setText(ms);
	}
	
	
	
	private void Conectar() {
		if (Utilidades.EmailValid(email.getText().toString()) && password.getText().length() > 7) {
			ValidarCuenta();
		}
		
	}

	
	private void ValidarCuenta(){
		new AsyncTask<Void, String, Boolean>(){

			@Override
			protected void onPreExecute() {
				validar = new GMailReader(null, null, null);
				publishProgress("Comprovando Cuenta...");
			}
			@Override
			protected Boolean doInBackground(Void... params) {
				Store st;
				try {
					st = validar.Conectar(email.getText().toString(), password.getText().toString());
					if (st.isConnected()) {
						publishProgress("Cuenta Valida");
						return true;
					}
				} catch (MessagingException e) {
					if (e.getMessage().equals("[AUTHENTICATIONFAILED] Invalid credentials (Failure)")) {
						publishProgress("Login Incorrecto");
					}else{
						publishProgress("Error al Comprovar Credenciales");
					}
					e.printStackTrace();
				}
				return false;
			}
			
			
			@Override
			protected void onProgressUpdate(String... values) {
				mensaje.setText(values[0]);
			}
			@Override
			protected void onPostExecute(Boolean result) {
				if (!result) {
					email.setBackgroundResource(R.drawable.edit_error);
					password.setBackgroundResource(R.drawable.edit_error);
					iniciar.setEnabled(false);
					
				}else{
					iniciar.setEnabled(true);
					email.setBackgroundResource(R.drawable.edit_true);
					password.setBackgroundResource(R.drawable.edit_true);
				}
			}

			
			}.execute();
	}

	@Override
	public void onClick(View v) {
		if (v == Guardar) {
			Configeditor.putBoolean("save", true);
			Configeditor.putBoolean("vibra", vibracion.isChecked());
			Configeditor.putBoolean("son", alarma.isChecked());
			Configeditor.putString("usuario", email.getText().toString());
			Configeditor.putString("subject", asunto.getText().toString());
			Configeditor.putString("pass", password.getText().toString());
			Configeditor.commit();
			OcultarTeclado(v);
			Mensaje("Preferencias Guardadas");
		}
		
		if (v == menu) {
			Intent intent = new Intent("android.intent.action.ACCIONES");
			startActivity(intent);
			OcultarTeclado(v);
		}
		
		if (v == iniciar) {
			Intent s = new Intent(Config.this, Servicio_Gmail.class);
			startService(s);
			
			Mensaje("Se Ha Iniciado Servicio");
			
			Configeditor.putBoolean("save", true);
			Configeditor.putBoolean("vibra", vibracion.isChecked());
			Configeditor.putBoolean("son", alarma.isChecked());
			Configeditor.putString("usuario", email.getText().toString());
			Configeditor.putString("subject", asunto.getText().toString());
			Configeditor.putString("pass", password.getText().toString());
			Configeditor.commit();
			OcultarTeclado(v);
			Guardar.setEnabled(false);
		}
	}
	
	private void  OcultarTeclado(View v) {
		InputMethodManager tecladoVirtual = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		tecladoVirtual.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
}

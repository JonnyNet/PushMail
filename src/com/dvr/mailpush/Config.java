package com.dvr.mailpush;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.Store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
	Button Guardar;
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
		Guardar = (Button) findViewById(R.id.atras);

		ConfigPreferent = getSharedPreferences("configuraciones", MODE_PRIVATE);
		Configeditor = ConfigPreferent.edit();
		save = ConfigPreferent.getBoolean("save", false);
		CargarPrefes();
		
		vibracion.setOnClickListener(this);
		alarma.setOnClickListener(this);
		password.setOnFocusChangeListener(this);
		email.setOnFocusChangeListener(this);
		asunto.setOnFocusChangeListener(this);
		Guardar.setEnabled(false);
		Guardar.setOnClickListener(this);
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
				&& EmailValid(email.getText().toString())) {
			email.setBackgroundResource(R.drawable.edit_true);
			Conectar();
		} else if (v == email && hasFocus == false
				&& email.getText().toString().equals("")) {
			email.setBackgroundResource(R.drawable.edit_error);
			mensaje.setText("Email Campo Requerido");
		}else if(!EmailValid(email.getText().toString())){
			email.setBackgroundResource(R.drawable.edit_error);
			mensaje.setText("Formato de Correo Invalido");			
		}

		if (v == password && hasFocus == false
				&& password.getText().toString().length() > 7) {
			password.setBackgroundResource(R.drawable.edit_true);
			Conectar();
		} else if (v == email && hasFocus == false
				&& password.getText().toString().length() < 8) {
			password.setBackgroundResource(R.drawable.edit_error);
			mensaje.setText("Contraseña Invalida");
		}

		if (v == asunto && hasFocus == false
				&& !asunto.getText().toString().equals("")) {
			asunto.setBackgroundResource(R.drawable.edit_true);
			OcultarTeclado(v);
		} else if (v == asunto  && hasFocus == false
				&& asunto.getText().toString().equals("")) {
			asunto.setBackgroundResource(R.drawable.edit_error);
			mensaje.setText("Asunto Campo requerido");
			OcultarTeclado(v);
		}

	}
	
	private static boolean EmailValid(String email) {
		boolean isValid = false;

		String expression = "[a-zA-Z0-9._-]+@gmail.com";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}
	
	private void Conectar() {
		if (EmailValid(email.getText().toString()) && password.getText().length() > 7) {
			ValidarCuenta();
		}
		
	}

	
	private void ValidarCuenta(){
		new AsyncTask<Void, String, Boolean>(){

			@Override
			protected void onPreExecute() {
				validar = new GMailReader(null, null, null);
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
					
				}else{
					Guardar.setEnabled(true);
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

			InputMethodManager tecladoVirtual = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			tecladoVirtual.hideSoftInputFromWindow(v.getWindowToken(), 0);

			Intent intent = new Intent("android.intent.action.ACCIONES");
			startActivity(intent);

			Intent s = new Intent(Config.this, Servicio_Gmail.class);
			startService(s);
		}
		
		if (v == vibracion || v == alarma) {
			if (asunto.hasFocus()) 
				Guardar.requestFocus();
		}
	}
	
	
	private void  OcultarTeclado(View v) {
		InputMethodManager tecladoVirtual = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		tecladoVirtual.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	

}

package com.dvr.mailpush;

import javax.mail.MessagingException;
import javax.mail.Store;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class AsynGmail extends AsyncTask<Void, Integer, Void> {

	GMailReader gmail;
	String email;
	String pass;
	EstadoGmail Gm;
	Store st = null;
	boolean sw = false;
	boolean logn = true;
	Context context;

	public AsynGmail(Context c, EstadoGmail l) {
		Gm = l;
		context = c;
		CargarPreferencias();
		
	}

	private void CargarPreferencias() {

		SharedPreferences datos = context.getSharedPreferences(
				"configuraciones", Context.MODE_PRIVATE);
		if (datos.getBoolean("save", false)) {
			email = datos.getString("usuario", "");
			pass = datos.getString("pass", "");
			String asunto = datos.getString("subject", "");
			gmail = new GMailReader(context, Gm, asunto);
			datos = null;
		} else {
			Gm.Login(true);
		}
	}

	@Override
	protected Void doInBackground(Void... params) {

		while (!isCancelled()) {
			Gm.OnEvento("Estableciendo Conexion");
			Pause(1000);
			if (Internet()) {
				if (st == null) {
					UpGm();
				}

				if (sw) {
					try {
						Gm.OnEvento("Buscando Alertas");
						gmail.readMail(st);
					} catch (MessagingException e) {
						Gm.OnEvento("Error al Leer");
						Pause(1000);
						e.printStackTrace();
						if (st.isConnected()) {
							try {
								st.close();
								st = null;
								sw = false;
							} catch (MessagingException e1) {
								e1.printStackTrace();
								Gm.OnEvento("Cerrando Conexion");
								Pause(1000);
							}
						}
					}
				}
			} else {
				Gm.OnEvento("No hay Conexion");
			}

			if (Internet() && logn) {
				Gm.OnEvento("Durmiendo...");
				Pause(20000);
			}
			Pause(30000);
		}
		return null;
	}
	
	

	@Override
	protected void onCancelled() {
		try {
			if (st != null) {
				st.close();
				Gm.OnEvento("Coneccion cerrada");
			}
		} catch (MessagingException e) {
			Gm.OnEvento("Error cerrar Conexion");
			e.printStackTrace();
		}
		super.onCancelled();
	}

	private void Pause(long time) {
		try {
			Thread.sleep(time);  
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void UpGm() {
		try {
			
			SharedPreferences datos = context.getSharedPreferences(
					"configuraciones", Context.MODE_PRIVATE);
			
			Log.w("login", datos.getString("usuario", "")+" "+datos.getString("pass", ""));
			Log.w("login", email+" "+pass);
			st = gmail.Conectar(email, pass);
			if (st.isConnected()) {
				sw = true;
				Gm.OnEvento("Coneccion Exitoza");
				Pause(1000);
			}
		} catch (MessagingException e) {
			st = null;
			String error = e.getMessage().toString();
			if (error.equals("[AUTHENTICATIONFAILED] Invalid credentials (Failure)")) {
				Gm.OnEvento("Login Incorrecto");
				logn = false;
				Gm.Login(true);
			}else{
				Gm.OnEvento("Fallo en la Coneccion");
			}
			
			Pause(1000);
			e.printStackTrace();
		}
	}

	private boolean Internet() {
		boolean bConectado = false;
		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] redes = connec.getAllNetworkInfo();
		for (int i = 0; i < 2; i++) {
			if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
				bConectado = true;
			}
		}
		return bConectado;
	}

}

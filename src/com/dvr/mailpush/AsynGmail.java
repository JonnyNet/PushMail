package com.dvr.mailpush;

import javax.mail.MessagingException;
import javax.mail.Store;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsynGmail extends AsyncTask<Void, Integer, Void> {

	private GMailReader gmail;
	private String email;
	private String pass;
	private EstadoGmail Gm;
	private Store st = null;
	boolean sw = false;
	boolean logn = true;
	private Context context;

	public AsynGmail(Context c, EstadoGmail l,String nombre, String passw, String asunto) {
		Gm = l;
		context = c;
		gmail = new GMailReader(c, Gm, asunto);
		email = nombre;
		pass = passw;
	}

	@Override
	protected Void doInBackground(Void... params) {

		while (!isCancelled()) {
			Gm.OnEvento("Estableciendo Conexion");
			Pause(1000);
			if (Utilidades.Internet(context)) {
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

			if (Utilidades.Internet(context) && logn) {
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
				if (st.isConnected()) {
					st.close();
				}
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
}

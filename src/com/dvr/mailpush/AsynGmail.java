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
	Context context;
	String[] Estado = {"No hay Coneccion","Coneccion Exitoza","Fallo en la Coneccion","Buscando Alertas","Error al Leer","Cerrando Coneccion","Durmiendo...","Estableciendo"};

	public AsynGmail(Context c, EstadoGmail l) {
		Gm = l;
		context = c;
		SharedPreferences datos = context.getSharedPreferences(
				"configuraciones", Context.MODE_PRIVATE);
		if (datos.getBoolean("save", false)) {
			email = datos.getString("usuario", "");
			pass = datos.getString("pass", "");
			String asunto = datos.getString("subject", "");
			Log.v("servicio iniciando", email + " " + pass + " " + asunto);

			gmail = new GMailReader(context, Gm, asunto);
		}
	}

	@Override
	protected Void doInBackground(Void... params) {

		while (true) {
			
			onProgressUpdate(7);
			Pause(1000);
			if (Internet()) {
				if (st == null) {
					UpGm();
				}

				if (sw) {
					try {
						onProgressUpdate(3);
						gmail.readMail(st);
					} catch (MessagingException e) {
						onProgressUpdate(4);
						Pause(1000);
						e.printStackTrace();
						if (st.isConnected()) {
							try {
								st.close();
								st = null;
								sw = false;
							} catch (MessagingException e1) {
								e1.printStackTrace();
								onProgressUpdate(5);
								Pause(1000);
							}
						}
					}
				}
			} else {
				onProgressUpdate(0);
			}
			
			if (Internet()) {
				onProgressUpdate(6);
				Pause(20000);
			}
			Pause(30000);
		}
	}
	
	private void Pause(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		Gm.OnEvento(Estado[(int)values[0]]);
	}

	private void UpGm() {
		try {
			st = gmail.Conectar(email, pass);
			if (st.isConnected()) {
				sw = true;
				onProgressUpdate(1);
				Pause(1000);
			}
		} catch (MessagingException e) {
			e.printStackTrace();
			st = null;
			onProgressUpdate(2);
			Pause(1000);
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

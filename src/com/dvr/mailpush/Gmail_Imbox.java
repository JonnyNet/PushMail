package com.dvr.mailpush;

import javax.mail.Store;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class Gmail_Imbox {

	private String email;
	private String password;
	private Store st;
	private GMailReader gmail;
	Context ctx;
	public Thread hilo;
	boolean sw = true;
	EstadoGmail mn;
	private boolean parar = true;
	

	public Gmail_Imbox(Context c, EstadoGmail m, String mail, String pass,String sub) {
		email = mail;
		password = pass;
		gmail = new GMailReader(m, sub);
		ctx = c;
		mn = m;
	}
	
	

	public void Parar() {
		parar = false;
	}

	public void Start() {
		parar = true;
		hilo = new Thread(new Runnable() {
			@Override
			public void run() {

				while (parar) {
					if (Internet()) {
						if (sw) {
							Log.d("coneccion", "conectando...");
							st = Up();
							Send(true);
							if (st.isConnected()) {
								sw = false;
								Log.d("coneccion", "Coneccion exitoza");
								Log.d("coneccion", "Mirando mensajes...");
								gmail.readMail(st);
							}

						} else {
							Log.d("coneccion", "Mirando mensajes...");
							gmail.readMail(st);
						}
						Log.d("coneccion", "Dormir...");
						Dormir(30000);
					} else {
						Send(false);
						Log.d("coneccion", "no hay internet");
						Dormir(60000);
					}

				}
			}

			private void Dormir(long min) {
				try {
					Thread.sleep(min);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		});

		hilo.start();

	}

	private Store Up() {
		return gmail.Conectar(email, password);
	}

	private boolean Internet() {
		boolean bConectado = false;
		ConnectivityManager connec = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] redes = connec.getAllNetworkInfo();
		for (int i = 0; i < 2; i++) {
			if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
				bConectado = true;
			}
		}
		return bConectado;
	}

	private void Send(final boolean ssw) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				mn.Internet(ssw);
			}
		});
	}

}

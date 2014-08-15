/**
 * 
 */
package com.dvr.mailpush;

import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class GMailReader extends javax.mail.Authenticator {
	private String mailhost = "imap.gmail.com";
	private Session session;
	public Store store;
	Properties props;
	public int msg;
	EstadoGmail mn;

	public GMailReader(EstadoGmail m) {
		mn = m;
		props = System.getProperties();
		msg = 0;
		session = Session.getDefaultInstance(props, null);
		try {
			store = session.getStore("imaps");
		} catch (NoSuchProviderException e1) {
			e1.printStackTrace();
		}
	}

	public Store Conectar(String user, String password) {
		try {
			store.connect(mailhost, user, password);
			Send(3, null, true);
		} catch (MessagingException e) {
			e.printStackTrace();
			Log.d("hhhhhhhhhhhhhhhhhhhhhhhhhhhhh", e.getMessage()+ "");
			if (e.getMessage().equals("[AUTHENTICATIONFAILED] Invalid credentials (Failure)")) {
				Send(3, null, false);
			} else {

			}
			

		}
		return store;
	}

	public synchronized void readMail(Store s) {
		try {
			Folder folder = s.getFolder("Inbox");
			folder.open(Folder.READ_WRITE);
			int count = folder.getUnreadMessageCount();
			if (count > 0) {
				int num = folder.getMessageCount();
				Log.d("mensaje", "Hay " + count + " Mensajes sin leer");
				Message[] mensajes = folder.getMessages((num - count), num);

				for (int i = 0; i < mensajes.length; i++) {
					String subj = mensajes[i].getSubject().toString();

					if (subj.equals("alerta")
							&& !mensajes[i].getFlags()
									.contains(Flags.Flag.SEEN)) {
						Log.d("holaaaaaaaaaaaaaaaa", subj + " " + i);
						mensajes[i].setFlag(Flags.Flag.SEEN, true);
						Send(1,subj,false);
					}
				}
				folder.close(true);

			} else {
				Log.d("mensaje", "No hay mensajes nuevos");
			}
		} catch (Exception e) {
			Log.e("leer", e.getMessage(), e);

		}
	}

	private void Send(final int metodo, final String dato, final boolean ssw) {
		Handler h = new Handler(Looper.getMainLooper());
		h.post(new Runnable() {
			public void run() {
				if (metodo == 1) {
					mn.Alerta(dato);
				} else if (metodo == 2) {
					mn.Internet(ssw);
				} else {
					mn.Login(ssw);
				}
			}
		});
	}
}

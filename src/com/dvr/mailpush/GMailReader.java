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

import com.dvr.mailpush.sqlite.Admin_db;

import android.content.Context;
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
	private String asunto;
	Context context;

	public GMailReader(Context c, EstadoGmail m, String sub) {
		mn = m;
		asunto = sub;
		context = c;
		props = System.getProperties();
		msg = 0;
		session = Session.getDefaultInstance(props, null);
		try {
			store = session.getStore("imaps");
		} catch (NoSuchProviderException e1) {
			e1.printStackTrace();
		}
	}

	public Store Conectar(String user, String password)
			throws MessagingException {
		store.connect(mailhost, user, password);
		return store;
	}

	public synchronized void readMail(Store s) throws MessagingException {

		Folder folder = s.getFolder("Inbox");
		folder.open(Folder.READ_WRITE);
		int count = folder.getUnreadMessageCount();
		if (count > 0) {
			int num = folder.getMessageCount();
			mn.OnEvento("Hay " + count + " Mensajes sin leer");
			Pause(1000);
			
			Message[] mensajes = folder.getMessages((num - count), num);

			for (int i = 0; i < mensajes.length; i++) {
				String subj = mensajes[i].getSubject().toString();

				if (subj.equals(asunto)
						&& !mensajes[i].getFlags().contains(Flags.Flag.SEEN)) {
					Admin_db bd = new Admin_db(context);
					bd.RegistrarEvento(asunto);
					bd.Cerrar();
					Log.d("holaaaaaaaaaaaaaaaa", subj + " " + i);
					mensajes[i].setFlag(Flags.Flag.SEEN, true);
					mn.Alerta(asunto);
				}
			}
			folder.close(true);

		} else {
			mn.OnEvento("No hay mensajes nuevos");
			Pause(1000);
		}
	}
	
	private void  Pause(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

/**
 * 
 */
package com.dvr.mailpush;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import android.util.Log;

public class GMailReader extends javax.mail.Authenticator {
	private static final String TAG1 = "jonny";

	private String mailhost = "imap.gmail.com";
	private Session session;
	public Store store;

	public GMailReader(String user, String password) {

		Properties props = System.getProperties();
		if (props == null) {
			Log.e("error", "Properties are null !!");
		} else {
			props.setProperty("mail.store.protocol", "imaps");
		}
		try {
			session = Session.getDefaultInstance(props, null);
			store = session.getStore("imaps");
			store.connect(mailhost, user, password);
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			Log.e("conectar", e+"");
		} catch (MessagingException e) {
			e.printStackTrace();
			Log.e("conectar", e+"");
		}
	}

	public synchronized void readMail() throws Exception {
		try {
			Folder folder = store.getFolder("Inbox");
			folder.open(Folder.READ_ONLY);
			int count = folder.getUnreadMessageCount();
			if (count > 0) {
				int num = folder.getMessageCount();
				Log.d("mensaje", "Hay "+ count +" Mensajes sin leer");
				Message [] mensajes = folder.getMessages((num-count), num);
				for (int i=0;i <mensajes.length;i++){
					String sub = mensajes[i].getSubject();
					if(sub.equals("alerta")){
					Log.d(TAG1, mensajes[i].getFrom()[0].toString());
					Log.d(TAG1, mensajes[i].getSubject());
					Log.d(TAG1, mensajes[i].getReceivedDate().toString());
				}
				}
			}else {
				Log.d("mensaje", "No hay mensajes nuevos");
			}
		} catch (Exception e) {	
			Log.e("leer", e.getMessage(), e);

		}
	}
}

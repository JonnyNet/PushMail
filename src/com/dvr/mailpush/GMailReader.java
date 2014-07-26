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
    private static final String TAG = "jonny";

    private String mailhost = "imaps.gmail.com";  
    private Session session;
    private Store store;
    private String Usuari;
    private String Password;
    
    public  GMailReader(String user, String  password) {
		Usuari = user;
		Password = password;
        Properties props = System.getProperties();
        if (props == null){
        	Log.e(TAG, "Properties are null !!");
        } else {
	        props.setProperty("mail.store.protocol", "imaps");
	        props.setProperty("mail.imap.host", mailhost);
	        props.setProperty("mail.imap.port", "993");
	        props.setProperty("mail.imap.sasl.enable", "true");
	        props.setProperty("mail.imap.sasl.mechanisms", "XOAUTH2");
	        props.setProperty("mail.imap.auth.login.disable", "true");
	        props.setProperty("mail.imap.auth.plain.disable", "true");
	        props.setProperty("mail.imap.ssl.enable", "true");
	
	        Log.d(TAG, "Transport: "+props.getProperty("mail.transport.protocol"));
	        Log.d(TAG, "Store: "+props.getProperty("mail.store.protocol"));
	        Log.d(TAG, "Host: "+props.getProperty("mail.imap.host"));
	        Log.d(TAG, "Authentication: "+props.getProperty("mail.imap.auth"));
	        Log.d(TAG, "Port: "+props.getProperty("mail.imap.port"));
	        Log.d(TAG, "SASL: "+props.getProperty("mail.imap.sasl.enable"));
	        Log.d(TAG, "SASL Mec: "+props.getProperty("mail.imap.sasl.mechanisms"));
	        Log.d(TAG, "Login Disable: "+props.getProperty("mail.imap.auth.login.disable"));
	        Log.d(TAG, "Plain Disable: "+props.getProperty("mail.imap.auth.plain.disable"));
	        Log.d(TAG, "SSL Enable: "+props.getProperty("mail.imap.ssl.enable"));
        }
        
       
        
	    try {
	        session = Session.getDefaultInstance(props, null);
	        store = session.getStore("imaps");
	        store.connect(mailhost, Usuari, Password);
	        Log.i(TAG, "Store: "+store.toString());
	    } catch (NoSuchProviderException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (MessagingException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
    }
    
    public synchronized int readMail() throws Exception { 
        try { 
            Folder folder = store.getFolder("INBOX"); 
            folder.open(Folder.READ_ONLY);
            Message [] mensajes = folder.getMessages();
            for (int i=0;i<mensajes.length;i++)
            {
            	Log.d(TAG,"From:"+mensajes[i].getFrom()[0].toString());
            	Log.d(TAG,"Subject:"+mensajes[i].getSubject());
            }

            /* TODO to rework
            Message[] msgs = folder.getMessages(1, 10);
            FetchProfile fp = new FetchProfile(); 
            fp.add(FetchProfile.Item.ENVELOPE); 
            folder.fetch(msgs, fp);
            
            Message[] msgs = folder.getMessages();
            return msgs;*/
             
            return folder.getUnreadMessageCount();
            
        } catch (Exception e) { 
            Log.e(TAG, e.getMessage(), e); 
            return 0; 
        } 
    } 
    
}

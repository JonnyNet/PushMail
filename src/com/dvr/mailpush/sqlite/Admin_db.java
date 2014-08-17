package com.dvr.mailpush.sqlite;
import com.dvr.mailpush.Utilidades;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Admin_db {
	
	private BD_helper helper;
	private SQLiteDatabase bd;

	public Admin_db(Context context) {
		helper = new BD_helper(context);
		bd = helper.getWritableDatabase();
	}
	
	public void RegistrarEvento(String asunto) {
		ContentValues valores = new ContentValues();
		valores.put("alert", asunto);
		valores.put("sync", "f");
		bd.insert("Notificaciones", null, valores);
	}
	
	public Cursor Notificadas(){
		return bd.rawQuery("SELECT * FROM  Notificaciones  WHERE sync = f", null);
	}
	
	public Cursor VerTodas(){
		return bd.rawQuery("SELECT * FROM  Notificaciones ", null);
	}
	
	public Cursor Fecha(String fecha){
		return bd.rawQuery("SELECT * FROM  Notificaciones fecha  LIKE '"+ fecha + "%' ", null);
	}
	
	
	public Cursor VerDia(){
		String fecha =  Utilidades.fecha();
		return bd.rawQuery("SELECT * FROM  Notificaciones fecha  LIKE '"+ fecha + "%' ", null);
	}
	
	public void Cerrar(){
		bd.close();
	}

}

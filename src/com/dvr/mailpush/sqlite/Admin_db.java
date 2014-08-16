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
		bd.insert("Avatar", null, valores);
	}
	
	
	public Cursor Notificadas(){
		return bd.rawQuery("SELECT * FROM  Avatar  WHERE sync = f", null);
	}
	
	public Cursor VerTodas(){
		return bd.rawQuery("SELECT * FROM  Avatar ", null);
	}
	
	
	public Cursor VerDia(){
		String fecha =  Utilidades.fecha();
		return bd.rawQuery("SELECT * FROM  Avatar fecha  LIKE '"+ fecha + "%' ", null);
	}
	
	public void Cerrar(){
		bd.close();
	}

}

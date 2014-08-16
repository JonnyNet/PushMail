package com.dvr.mailpush.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BD_helper extends SQLiteOpenHelper {

	private final static String BD_NAME = "MailPush.sqlite";
	private final static int BD_VERSION = 1;

	private static final String Avatar = "Notificaciones";

	public static final String tabla1 = "CREATE TABLE " + Avatar + " ( "
			+ "_id  integer primary key autoincrement, "
			+ "alert	Varchar not null, " + "sync char not null, "
			+ "fecha_ingreso TIMESTAMP NOT NULL DEFAULT current_timestamp )";

	public BD_helper(Context context) {
		super(context, BD_NAME, null, BD_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(tabla1);
		} catch (Exception e) {
			Log.e("bd", e+"");
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}

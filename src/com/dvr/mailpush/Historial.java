package com.dvr.mailpush;

import com.dvr.mailpush.sqlite.Admin_db;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

public class Historial extends Activity {
	Button menu;
	Spinner opcion;
	ListView lista;
	Admin_db bd;
	String[] fecha = { "HOY", "TODAS" };
	SimpleCursorAdapter cur;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.historial);
		bd = new Admin_db(this);
		opcion = (Spinner) findViewById(R.id.select);
		lista = (ListView) findViewById(R.id.lista);
		menu = (Button) findViewById(R.id.menu);
		menu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("android.intent.action.ACCIONES");
				startActivity(intent);
			}
		});

		ArrayAdapter<String> adacter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, fecha);
		opcion.setAdapter(adacter);

		opcion.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					Cursor c = bd.VerDia();
					cur.changeCursor(c);
				} else if (position == 1) {
					Cursor c = bd.VerTodas();
					cur.changeCursor(c);
				}

				return false;
			}
		});
		CArgarCursor();

	}

	private void CArgarCursor() {
		Cursor c = bd.VerDia();
		if (c.moveToFirst()) {
			cur = new SimpleCursorAdapter(this, R.layout.listview, c,
					new String[] { "fron", "fecha" }, new int[] { R.id.from,
							R.id.fecha }, 0);
			lista.setAdapter(cur);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		bd.Cerrar();
	}

}

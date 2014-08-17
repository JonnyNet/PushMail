package com.dvr.mailpush;

import com.dvr.mailpush.sqlite.Admin_db;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class Historial extends Activity {

	Spinner opcion;
	ListView lista;
	Admin_db bd;
	String[] fecha = { "Opciones", "Hoy", "Todas" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.historial);
		bd = new Admin_db(this);
		opcion = (Spinner) findViewById(R.id.select);
		lista = (ListView) findViewById(R.id.lista);

		ArrayAdapter<String> adacter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, fecha);
		opcion.setAdapter(adacter);
		
		opcion.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				return false;
			}
		});
		CArgarCursor();

	}

	private void CArgarCursor() {
		Cursor c = bd.VerTodas();
		c.moveToFirst();
		SimpleCursorAdapter cur = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, c, new String[] {
						"alert", "fecha_ingreso" }, new int[] {
						android.R.id.text1, android.R.id.text2 }, 0);
		lista.setAdapter(cur);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		bd.Cerrar();
	}

}

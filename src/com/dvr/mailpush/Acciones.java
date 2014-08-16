package com.dvr.mailpush;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Acciones extends Activity implements OnClickListener{
	ImageButton cam,call,notic,hist;
	Button confg,salir;
	ToggleButton soud;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acciones);
		
		cam = (ImageButton) findViewById(R.id.cam);
		call = (ImageButton) findViewById(R.id.call);
		notic = (ImageButton) findViewById(R.id.historial);
		hist = (ImageButton) findViewById(R.id.salir);
		confg = (Button) findViewById(R.id.config);
		
		cam.setOnClickListener(this);
		call.setOnClickListener(this);
		notic.setOnClickListener(this);
		hist.setOnClickListener(this);
		confg.setOnClickListener(this);
		
		
	}


	@Override
	public void onClick(View v) {
		int key = v.getId();
		switch (key) {
		case R.id.cam:
			AbrirTruvision();
			break;

		case R.id.call:
			Llamar911();
			break;
		case R.id.salir:
			finish();
			break;
		case R.id.config:
			Config();
			break;
			
		case R.id.historial:
			Avatar();
			break;
		}
		
	}


	private void Avatar() {
		Intent v = new Intent("android.intent.action.HISTORIAL");
		startActivity(v);
		finish();
	}


	private void Config() {
		Intent v = new Intent("android.intent.action.CONFIG");
		startActivity(v);
		finish();
	}


	private void Llamar911() {
		if (((TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number()
			    != null) {
			Intent callIntent = new Intent(Intent.ACTION_DIAL);
		    callIntent.setData(Uri.parse("tel:123"));
		    startActivity(callIntent);
		} else {
			Toast.makeText(this, "Este dispositivo no puede hacer llamadas", Toast.LENGTH_SHORT).show();
		}
		
	}


	private void AbrirTruvision() {
		String packageName = "com.vMEyeCloudTruvision";
	    PackageManager pm = this.getPackageManager();
	    
	    try {
	      Intent it = pm.getLaunchIntentForPackage(packageName);
	      if (null != it)
	    	  this.startActivity(it);
	      else {
	    	  try {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
				}  
	      }
	    } catch (ActivityNotFoundException e){
	    	try {
			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
			} catch (android.content.ActivityNotFoundException anfe) {
			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
			}
	    }
		
	}

	

}

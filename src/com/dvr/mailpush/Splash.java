package com.dvr.mailpush;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity{
	
	 private final int DURACION_SPLASH = 3000; 

	 
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  
		  setContentView(R.layout.splash);
		  
	        new Handler().postDelayed(new Runnable(){
	            public void run(){
	        	Intent intent = new Intent(Splash.this, Config.class);
	        	startActivity(intent);
	        	finish();
	            };
	        }, DURACION_SPLASH);	  
	  }

}

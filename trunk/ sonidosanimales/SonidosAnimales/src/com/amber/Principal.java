package com.amber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Principal extends Activity {
    /** Called when the activity is first created. */
	private int SPLASH_DISPLAY_LENGTH = 1000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Handler handler = new Handler();
        handler.postDelayed(getRunnableStartApp(), SPLASH_DISPLAY_LENGTH);
    }
    
    private Runnable getRunnableStartApp(){
    	Runnable runnable = new Runnable(){
    	public void run(){
    			//validaciones después de terminar la página de splash
    		
		    	Intent intent = new Intent(Principal.this, Animales.class);
		    	startActivity(intent);
		    	finish();
	    	}
    	};
    	return runnable;
    }
    

}
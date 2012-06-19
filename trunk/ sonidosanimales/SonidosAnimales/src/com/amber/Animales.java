package com.amber;

import java.io.IOException;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.amber.utils.AccesoDatos;

public class Animales extends Activity{
	private TextView tvNombreAnimal;
	private ImageView ivImagenAnimal;
	private MediaPlayer mediaPlayerSonido;
	private AccesoDatos accesoDatos;
	private static Cursor cursorDatos;
	private final String nombreBD = "Animales.db";
	private String sSonidoAnimal;
	private static Boolean inicio = true;


	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animal);
		ImageView ivMenu = (ImageView)findViewById(R.id.ivMenu);
		ivMenu.setOnClickListener(ivMenuCL);
		
		ImageView ivAnterior = (ImageView)findViewById(R.id.ivAnterior);
		ivAnterior.setOnClickListener(ivAnteriorCL);
		ImageView ivSiguiente = (ImageView)findViewById(R.id.ivSiguiente);
		ivSiguiente.setOnClickListener(ivSiguienteCL);
		

		ivImagenAnimal = (ImageView)findViewById(R.id.ivImagenAnimal);
		ivImagenAnimal.setOnClickListener(ivImagenAnimalCL);
		
		tvNombreAnimal = (TextView)findViewById(R.id.tvNombreAnimal);
		
		ImageView ivAleatorio = (ImageView)findViewById(R.id.ivAleatorio);
		ivAleatorio.setOnClickListener(ivAleatorioCL);
		
		if (inicio == true){
			try{
			inicioDatos();
			}catch (Exception e) {
				Log.e("error inicio", e.toString());
			}
		}else{
			llenaObjetos();
		}
	
		
		//llenaMapa();
		
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
	       case R.id.menuAdivinar:
	           Intent intent = new Intent();
	           intent.setClass(Animales.this, Adivina.class);
	           startActivity(intent);
	    	   return true; 
		}
		
		return true;
	}
	
	/*
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mediaPlayerSonido.stop();
    	mediaPlayerSonido.setOnCompletionListener(completionList);	
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub		
		super.onPause();
		mediaPlayerSonido.stop();

	}
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		sonido(sSonidoAnimal);
		ImageView ivConSinSonido = (ImageView)findViewById(R.id.ivConSinSonido);
		if (sonido == true){
			ivConSinSonido.setImageResource(R.drawable.sinsonido48x48);
			sonido = true;
		}else{
			ivConSinSonido.setImageResource(R.drawable.consonido48x48);
			sonido = false;
		}
	}
	*/
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	//Menú
	public boolean onCreateOptionsMenu(Menu menu) {
	    //Alternativa 1
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	//Listener
	private OnClickListener ivMenuCL = new OnClickListener() {
		
		public void onClick(View v) {
			Animales.this.openOptionsMenu();
		}
	};
	
	private OnClickListener ivAnteriorCL = new OnClickListener() {
		
		public void onClick(View v) {
			cambioAnimalAnt();
		}
	};
	
	private OnClickListener ivSiguienteCL = new OnClickListener() {
		
		public void onClick(View v) {
			cambioAnimalSig();
		}
	};
	
	private OnClickListener ivAleatorioCL = new OnClickListener() {
		
		public void onClick(View arg0) {
			aleatorio();
		}
	};
	/*
	private OnClickListener ivConSinSonidoCL = new OnClickListener() {
		
		public void onClick(View v) {
			ImageView ivConSinSonido = (ImageView)findViewById(R.id.ivConSinSonido);
			
			if (mediaPlayerSonido.isPlaying()){
				mediaPlayerSonido.stop();
				ivConSinSonido.setImageResource(R.drawable.consonido48x48);
			}
			else{
				sonido(sSonidoAnimal);
				ivConSinSonido.setImageResource(R.drawable.sinsonido48x48);
			}
		}
	};
	*/
	private OnClickListener ivImagenAnimalCL = new OnClickListener() {
		
		public void onClick(View v) {
			sonido(sSonidoAnimal);
		}
	};
	
	private void inicioDatos() throws SAXException, IOException, ParserConfigurationException{
		//CreaBD creaBD = new CreaBD(getApplicationContext(), nombreBD, null, 1);
		//SQLiteDatabase db = creaBD.getWritableDatabase();
		
		accesoDatos = new AccesoDatos(getApplicationContext(), nombreBD);
		//accesoDatos.eliminaTabla("animal");
		accesoDatos.creaTabla();
		accesoDatos.insertaDatos("animal", this);	
		cursorDatos = accesoDatos.seleccionaDatos("animal");
		if (cursorDatos.moveToFirst()){
			llenaObjetos(); 
		}
		inicio = false;
	}
	
	private void cambioAnimalSig(){

			mediaPlayerSonido.release();

		if (!cursorDatos.isLast()){
			cursorDatos.moveToNext();
			llenaObjetos();
		}else{
			cursorDatos.moveToFirst();
			llenaObjetos();
		}

	}
	
	private void cambioAnimalAnt(){

			mediaPlayerSonido.release();

		if (!cursorDatos.isFirst()){
			cursorDatos.moveToPrevious();
			llenaObjetos();
		}else{
			cursorDatos.moveToLast();
			llenaObjetos();
		}	
	}
	
	private void llenaObjetos(){
		int iNombreAnimal = cursorDatos.getColumnIndexOrThrow("nombre");
		int iFiguraAnimal = cursorDatos.getColumnIndexOrThrow("drawableSonido");
		int iSonidoAnimal = cursorDatos.getColumnIndexOrThrow("drawableSonido");
		
		String sNombreAnimal = cursorDatos.getString(iNombreAnimal);
		String sFiguraAnimal = cursorDatos.getString(iFiguraAnimal);
		sSonidoAnimal = cursorDatos.getString(iSonidoAnimal);
		
		tvNombreAnimal.setText(sNombreAnimal);
		//imagen
		Resources res = getResources();
		int resID = getResources().getIdentifier(sFiguraAnimal , "drawable", getPackageName());
    	Drawable drawable = res.getDrawable(resID); 
    	ivImagenAnimal.setImageDrawable(drawable);	
    	//sonido
    	sonido(sSonidoAnimal);

	}
	
	OnCompletionListener completionList = new OnCompletionListener() {
		
		public void onCompletion(MediaPlayer mp) {
			mediaPlayerSonido.release();
		}
	};
	
	private void sonido(String sSonidoAnimal){
    	int resIDSonido = getResources().getIdentifier(sSonidoAnimal, "raw", getPackageName());
    	mediaPlayerSonido = MediaPlayer.create(Animales.this, resIDSonido);
    	mediaPlayerSonido.start();
    	mediaPlayerSonido.setLooping(false);
    	mediaPlayerSonido.setOnCompletionListener(completionList);	  	
	} 
	
	private void aleatorio(){
		int numero;
		Random r = new Random();
		numero = (int)(r.nextInt(cursorDatos.getCount()));
		cursorDatos.moveToPosition(numero);
		llenaObjetos();
	}
	
}

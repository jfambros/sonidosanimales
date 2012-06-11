package com.amber;

import java.util.HashMap;

import com.amber.utils.AccesoDatos;
import com.amber.utils.Animal;
import com.amber.utils.CreaBD;
import android.R.anim;
import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class Animales extends Activity{
	private TextView tvNombreAnimal;
	private ImageView ivImagenAnimal;
	private MediaPlayer mediaPlayerSonido;
	private AccesoDatos accesoDatos;
	private Cursor cursorDatos;
	private final String nombreBD = "Animales.db";
	
	
	private HashMap<Integer, Animal> hmAnimal;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animales);
		ImageView ivMenu = (ImageView)findViewById(R.id.ivMenu);
		ivMenu.setOnClickListener(ivMenuCL);
		
		ImageView ivAnterior = (ImageView)findViewById(R.id.ivAnterior);
		ivAnterior.setOnClickListener(ivAnteriorCL);
		ImageView ivSiguiente = (ImageView)findViewById(R.id.ivSiguiente);
		ivSiguiente.setOnClickListener(ivSiguienteCL);
		
		
		//objetos de la interfaz
		tvNombreAnimal = (TextView)findViewById(R.id.tvNombre);
		ivImagenAnimal = (ImageView)findViewById(R.id.ivImagenAnimal);

		
		inicioDatos();
	
		//llenaMapa();
		
		
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
	
	//Llenado de datos
	private void llenaMapa(){
		hmAnimal = new HashMap<Integer, Animal>();
	}
	private void inicioDatos(){
		CreaBD creaBD = new CreaBD(getApplicationContext(), nombreBD, null, 1);
		SQLiteDatabase db = creaBD.getWritableDatabase();
		accesoDatos = new AccesoDatos(getApplicationContext(), nombreBD);
		accesoDatos.insertaDatos("animal");	
		accesoDatos = new AccesoDatos(this, nombreBD);
		cursorDatos = accesoDatos.seleccionaDatos("Animal");
		if (cursorDatos.moveToFirst()){
			llenaObjetos();
		}
	}
	
	private void cambioAnimalSig(){
		cursorDatos.moveToNext();
		if (cursorDatos.isLast()){
			cursorDatos.moveToFirst();
		}

		llenaObjetos();
	}
	
	private void cambioAnimalAnt(){
		cursorDatos.moveToPrevious();
		if (cursorDatos.isFirst()){
			cursorDatos.moveToLast();
		}
		llenaObjetos();		
	}
	
	private void llenaObjetos(){
		int iNombreAnimal = cursorDatos.getColumnIndexOrThrow("nombre");
		int iFiguraAnimal = cursorDatos.getColumnIndexOrThrow("drawable");
		int iSonidoAnimal = cursorDatos.getColumnIndexOrThrow("sonido");
		
		String sNombreAnimal = getString(iNombreAnimal);
		String sFiguraAnimal = getString(iFiguraAnimal);
		String sSonidoAnimal = getString(iSonidoAnimal);
		Log.i("Llenando objetos", sNombreAnimal);
		
		tvNombreAnimal.setText(sNombreAnimal);
		//imagen
		Resources res = getResources();
		int resID = getResources().getIdentifier(sFiguraAnimal , "drawable", getPackageName());
    	Drawable drawable = res.getDrawable(resID); 
    	ivImagenAnimal.setImageDrawable(drawable);	
	}
}

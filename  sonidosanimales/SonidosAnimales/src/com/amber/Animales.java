package com.amber;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.amber.utils.AccesoDatos;
import com.amber.utils.Animal;
import com.amber.utils.CreaBD;

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

		try{
		inicioDatos();
		}catch (Exception e) {
			Log.e("error inicio", e.toString());
		}
	
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
	
	private void inicioDatos() throws SAXException, IOException, ParserConfigurationException{
		CreaBD creaBD = new CreaBD(getApplicationContext(), nombreBD, null, 1);
		SQLiteDatabase db = creaBD.getWritableDatabase();
		accesoDatos = new AccesoDatos(getApplicationContext(), nombreBD);
		accesoDatos.eliminaTabla("animal");
		accesoDatos.insertaDatos("animal", this);	
		cursorDatos = accesoDatos.seleccionaDatos("animal");
		if (cursorDatos.moveToFirst()){
			llenaObjetos(); 
		}
	}
	
	private void cambioAnimalSig(){
		if (!cursorDatos.isLast()){
			cursorDatos.moveToNext();
			llenaObjetos();
		}else{
			cursorDatos.moveToFirst();
			llenaObjetos();
		}

	}
	
	private void cambioAnimalAnt(){
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
		String sSonidoAnimal = cursorDatos.getString(iSonidoAnimal);
		Log.i("Llenando objetos", sNombreAnimal);
		
		tvNombreAnimal.setText(sNombreAnimal);
		//imagen
		Resources res = getResources();
		int resID = getResources().getIdentifier(sFiguraAnimal , "drawable", getPackageName());
    	Drawable drawable = res.getDrawable(resID); 
    	ivImagenAnimal.setImageDrawable(drawable);	
	}
}

package com.amber;

import java.io.IOException;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
	private int numAnimales;
	private String animalSeleccionado;
	private AlertDialog alertDialogSeleccAnimal;


	protected void onCreate(Bundle savedInstanceState) {
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
		
		ImageView ivAdivina = (ImageView)findViewById(R.id.ivAdivinar);
		ivAdivina.setOnClickListener(ivAdivinaCL);
		
		
		if (inicio == true){
			try{
			inicioDatos();
			}catch (Exception e) {

			}
		}else{
			llenaObjetos();
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
	       case R.id.menuConfigurar:
	    	   inicio = true; 
	           Intent intent = new Intent();
	           intent.putExtra("numAnimales", numAnimales);
	           Log.i("Numero", Integer.toString(numAnimales));
	           intent.setClass(Animales.this, Configuracion.class);
	           startActivity(intent);
	    	   return true; 
	       case R.id.menuBuscar:
	    	   buscaAnimal();
	    	   return true;
		}
		
		return true;
	}
	
	private void cerrar(){
		cursorDatos.close();
		accesoDatos.cierraBase();
	}

	
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

	private OnClickListener ivImagenAnimalCL = new OnClickListener() {
		
		public void onClick(View v) {
			sonido(sSonidoAnimal);
		}
	};
	
	private OnClickListener ivAdivinaCL = new OnClickListener() {
		
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Animales.this, AdivinaLista.class);
			startActivity(intent);	
			
		}
	};
	
	private void inicioDatos() throws SAXException, IOException, ParserConfigurationException{

		accesoDatos = new AccesoDatos(getApplicationContext(), nombreBD);
		accesoDatos.abrirBase();
		accesoDatos.creaTabla();
		accesoDatos.insertaDatos("animal", this);	
		cursorDatos = accesoDatos.seleccionaDatos("animal");
		numAnimales = cursorDatos.getCount();
		if (cursorDatos.moveToFirst()){
			llenaObjetos(); 
		}
		accesoDatos.cierraBase();
		inicio = false;
	}
	
	private void cambioAnimalSig(){
		if (!cursorDatos.isLast()){
			cursorDatos.moveToNext();
			//mediaPlayerSonido.stop();
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
		sSonidoAnimal = cursorDatos.getString(iSonidoAnimal);
		
		tvNombreAnimal.setText(sNombreAnimal);
		//imagen
		Resources res = getResources();
		int resID = getResources().getIdentifier(sFiguraAnimal , "drawable", getPackageName());
    	Drawable drawable = res.getDrawable(resID); 
    	ivImagenAnimal.setImageDrawable(drawable);	
    	//sonido
    	Log.i("sonido",sSonidoAnimal);
    	sonido(sSonidoAnimal);

	}
	
	OnCompletionListener completionList = new OnCompletionListener() {
		
		public void onCompletion(MediaPlayer mp) {
			mediaPlayerSonido.release();
		}
	};
	
	private void sonido(String sSonidoAnimal){

	    	int resIDSonido = getResources().getIdentifier(sSonidoAnimal, "raw", getPackageName());
	    	if (mediaPlayerSonido != null){
	    		mediaPlayerSonido.release();
	    	}
	    	mediaPlayerSonido = null;
	    	mediaPlayerSonido = new MediaPlayer();
	    	mediaPlayerSonido = MediaPlayer.create(Animales.this, resIDSonido);
	    	try {
				mediaPlayerSonido.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	private void buscaAnimal(){
		Builder builder;
		LayoutInflater lInflater = (LayoutInflater)Animales.this.getSystemService(Animales.this.LAYOUT_INFLATER_SERVICE);
		View layout = lInflater.inflate(R.layout.dialogobuscar, (ViewGroup)Animales.this.findViewById(R.layout.main));
		Spinner spinAnimales = (Spinner)layout.findViewById(R.id.spinnerAnimales);
		Button btnBuscarAnimal = (Button)layout.findViewById(R.id.btnBuscarAnimal);
		


		
		builder = new AlertDialog.Builder(Animales.this);
		builder.setView(layout);
		alertDialogSeleccAnimal = builder.create();
		alertDialogSeleccAnimal.setTitle("Mensaje");
		//btnAceptar.setOnClickListener(btnAceptarCL);
		String[] from = new String[]{"nombre"};
		// create an array of the display item we want to bind our data to
		int[] to = new int[]{android.R.id.text1};
		// create simple cursor adapter
		SimpleCursorAdapter adapter =  new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, cursorDatos, from, to );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item );
		spinAnimales.setPrompt("Selecciona");
		// get reference to our spinner
		spinAnimales.setAdapter(adapter);
        spinAnimales.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, 
                            View view, 
                            int position, 
                            long id) {
//                    		cursorDatos.getColumnIndexOrThrow( ((TextView)view).getText().toString());
                    	//Log.i("Spinner",((TextView)view).getText().toString() + Integer.toString(position));
                    	animalSeleccionado = ((TextView)view).getText().toString();
                    }

					public void onNothingSelected(AdapterView<?> arg0) {
					
					}
                }
            );
        
		OnClickListener btnBuscarAnimalCL = new OnClickListener() {
			
			public void onClick(View v) {
				Toast.makeText(Animales.this, animalSeleccionado, Toast.LENGTH_SHORT).show();
				alertDialogSeleccAnimal.cancel();
			}
		};
		
		btnBuscarAnimal.setOnClickListener(btnBuscarAnimalCL);
		
		alertDialogSeleccAnimal.show();
	}
	
	
}

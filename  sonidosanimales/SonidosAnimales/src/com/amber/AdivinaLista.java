package com.amber;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amber.utils.AccesoDatos;
import com.amber.utils.Animal;


public class AdivinaLista  extends ListActivity{
	private static AccesoDatos accesoDatos;
	private  Cursor cursor;
	private String sonidoAnimalAleatorio;
	private ArrayList<Animal> listaAnimales;
	private int iNumSonidoAleatorio;
	private MediaPlayer mediaPlayerSonido;
	private int numAnimales = 3; 
	private ListaConImagenes adaptador;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		try{
			setContentView(R.layout.listaadivina);		
			obtenerAnimales(numAnimales);			
			ImageView ivSonido = (ImageView)findViewById(R.id.ivSonidoAdivinaLista);
			ivSonido.setOnClickListener(ivSonidoCL);
			
			ImageView ivRegresaAdivina = (ImageView)findViewById(R.id.ivRegresaAdivinaLista);
			ivRegresaAdivina.setOnClickListener(ivRegresaAdivinaCL);
			
			ImageView ivRecargar = (ImageView)findViewById(R.id.ivRecargaAdivinaLista);
			ivRecargar.setOnClickListener(ivRecargarCL);
			
			TextView tvMensajeAdivina = (TextView)findViewById(R.id.tvMensajeAdivinaLista);
			tvMensajeAdivina.setOnClickListener(tvMensajeAdivinaCL);
		}
		catch (Exception e) {
			Log.e("Error inicia adivina", e.toString());
		}
	}
	
	private OnClickListener ivSonidoCL = new OnClickListener() {
		
		public void onClick(View arg0) {
			sonido(sonidoAnimalAleatorio);
		}
	};
	
private OnClickListener tvMensajeAdivinaCL = new OnClickListener() {
		
		public void onClick(View arg0) {
			sonido(sonidoAnimalAleatorio);			
		}
	};
	private OnClickListener ivRecargarCL = new OnClickListener() {
		
		public void onClick(View arg0) {
			if (mediaPlayerSonido != null){
				mediaPlayerSonido.release();
			}
			obtenerAnimales(numAnimales);
		}
	};
	
	private OnClickListener ivRegresaAdivinaCL = new OnClickListener() {
		
		public void onClick(View arg0) {
			cierra();
			mediaPlayerSonido.release();
			Intent intent = new Intent();
			intent.setClass(AdivinaLista.this, Animales.class);
			startActivity(intent); 
		}
	};
	
	 public class ListaConImagenes extends ArrayAdapter<Animal> {

	        private ArrayList<Animal> items;

	        public ListaConImagenes(Context context, int textViewResourceId, ArrayList<Animal> items) {
	                super(context, textViewResourceId, items);
	                this.items = items;
	        }

	        public View getView(int position, View convertView, ViewGroup parent) {
	                View v = convertView;
	                if (v == null) {
	                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                    v = vi.inflate(R.layout.lista_fila, null);
	                }
	                Animal listaAnimales = items.get(position);
	                if (listaAnimales != null) {
	                	
	                	//se llena la lista de elementos
	                	
                        	ImageView imagenAnimal = (ImageView) v.findViewById(R.id.ivAnimalLista);		                	
	                        TextView nombreAnimal = (TextView) v.findViewById(R.id.tvNombAnimalLista);
	    	                try{
	    	    				Resources res = getResources();
	    	    				
	    	    				int resID = getResources().getIdentifier(((Animal)items.get(position)).getDrawableSonidoAnimal() , "drawable", getPackageName());
	    	    		    	Drawable drawable = res.getDrawable(resID);
	    	    		    	
	    	    				imagenAnimal.setImageDrawable(drawable);
	    	    				nombreAnimal.setText(((Animal) getItem(position)).getNombreAnimal());
	    	    			}
	    	    			catch(Exception err){
	    	    				Log.e("Error en imagen", err.toString());
	    	    			}
	                }
	                return v;
	        }
	}
	 
	    protected void onListItemClick(ListView l, View v, int position, long id) {
	        Animal animal = (Animal) l.getItemAtPosition(position);        
			if (animal.getDrawableSonidoAnimal().equals(sonidoAnimalAleatorio)){
				Toast.makeText(AdivinaLista.this, "Muy bien", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(AdivinaLista.this, "Intenta de nuevo", Toast.LENGTH_SHORT).show();
			}
	    }
	 
		private void cierra(){
			//accesoDatos.cierraBase();
			accesoDatos = null;
			cursor.close();
		}
		
		private void inicioDatos(){
			accesoDatos = new AccesoDatos(this, "Animales.db");
			accesoDatos.abrirBase();
			cursor = accesoDatos.seleccionaDatos("animal");
			cursor.moveToFirst();
			accesoDatos.cierraBase();
		}
	 
	 private void obtenerAnimales(int tam){
			HashSet<Integer> total = new HashSet<Integer>();
			Iterator<Integer> iterator;
			int cont=0;

			inicioDatos();
				listaAnimales = new ArrayList<Animal>();
				total = genera(tam);
				iterator = total.iterator();
				
				
				while (iterator.hasNext()){
					cursor.moveToPosition(Integer.parseInt(iterator.next().toString()));
					int iNombreAnimal = cursor.getColumnIndexOrThrow("nombre");
					int iFiguraAnimal = cursor.getColumnIndexOrThrow("drawableSonido");
					int iSonidoAnimal = cursor.getColumnIndexOrThrow("drawableSonido");
					//int iIdioma = cursor.getColumnIndexOrThrow("idioma");
					
					String sNombreAnimal = cursor.getString(iNombreAnimal);
					String sFiguraAnimal = cursor.getString(iFiguraAnimal);
					String sSonidoAnimal = cursor.getString(iSonidoAnimal);
					//String sIdioma = cursor.getString(iIdioma);
					if (cont == iNumSonidoAleatorio){
						sonidoAnimalAleatorio = sSonidoAnimal;
						Log.i("Sonido animal", sonidoAnimalAleatorio);
					}
					
					Animal animal = new Animal();
					animal.setNombreAnimal(sNombreAnimal);
					animal.setDrawableSonidoAnimal(sFiguraAnimal);
					//animal.setIdioma(sIdioma);
					
					listaAnimales.add(animal);
					cont++;
				}

				this.adaptador = new ListaConImagenes(this, R.layout.lista_fila, listaAnimales);
		        setListAdapter(this.adaptador);
		        
			cursor.close();
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
	    	mediaPlayerSonido = MediaPlayer.create(AdivinaLista.this, resIDSonido);

	    	mediaPlayerSonido.start();
	    	mediaPlayerSonido.setLooping(false);
	    	mediaPlayerSonido.setOnCompletionListener(completionList);	
	    	
		}
		private HashSet<Integer> genera(int tam){
			HashSet<Integer> totalAleatorio=new HashSet<Integer>();
			int cont=1;
			while (cont<=tam) {
				  Integer al=new Integer(new java.util.Random().nextInt(cursor.getCount()));
				  if(totalAleatorio.add(al)){
					  cont++;
				  }
		     }
			//al azar el sonido
			Random random = new Random();
			iNumSonidoAleatorio = random.nextInt(tam);
			return totalAleatorio;
		}	
}

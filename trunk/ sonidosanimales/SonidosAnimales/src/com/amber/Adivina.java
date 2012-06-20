package com.amber;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.amber.utils.AccesoDatos;
import com.amber.utils.Animal;
import com.amber.utils.DatosAnimalView;

public class Adivina extends Activity{
	private AccesoDatos accesoDatos;
	private Cursor cursor;
	private String sonidoAnimalAleatorio;
	private GridView gvAnimales;
	private ArrayList<Animal> listaAnimales;
	private int iNumSonidoAleatorio;
	private MediaPlayer mediaPlayerSonido;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adivina);
		
		gvAnimales = (GridView)findViewById(R.id.gridViewAdivina);
		((GridView) findViewById(R.id.gridViewAdivina)).setOnItemClickListener(clickListGridView);
		
		ImageView ivSonidoAnimal = (ImageView)findViewById(R.id.ivSonidoAdivina);
		ivSonidoAnimal.setOnClickListener(ivSonidoAnimalCL);
		obtenerAnimales(3);
		
		try{
			gvAnimales.setAdapter(new ImageAdapter(this, listaAnimales.size(), listaAnimales));
		}
		catch (Exception e) {
			Log.e("Error inicia adivina", e.toString());
		}
		//sonido(sonidoAnimalAleatorio);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		/*
		if (!cursor.isClosed())
			cursor.close();
		if (mediaPlayerSonido.isPlaying())
			mediaPlayerSonido.release();
			*/
	}
	
	private OnClickListener ivSonidoAnimalCL = new OnClickListener() {
		
		public void onClick(View arg0) {
			sonido(sonidoAnimalAleatorio);
		}
	};
	
	private void obtenerAnimales(int tam){
		HashSet<Integer> total = new HashSet<Integer>();
		Iterator<Integer> iterator;
		int cont=0;
		
		accesoDatos = new AccesoDatos(this, "Animales.db");
		cursor = accesoDatos.seleccionaDatos("animal");
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
		
		

		//cursor.moveToPosition(numAleatorio);
/*
		if (cursor.moveToFirst()){
			for (int i = 0; i<cursor.getCount(); i++){
			int iNombreAnimal = cursor.getColumnIndexOrThrow("nombre");
			int iFiguraAnimal = cursor.getColumnIndexOrThrow("drawableSonido");
			int iSonidoAnimal = cursor.getColumnIndexOrThrow("drawableSonido");
			//int iIdioma = cursor.getColumnIndexOrThrow("idioma");
			
			String sNombreAnimal = cursor.getString(iNombreAnimal);
			String sFiguraAnimal = cursor.getString(iFiguraAnimal);
			sSonidoAnimal = cursor.getString(iSonidoAnimal);
			//String sIdioma = cursor.getString(iIdioma);
			Log.i("datos",sNombreAnimal+" "+sFiguraAnimal+" "+sSonidoAnimal);
			Animal animal = new Animal();
			animal.setNombreAnimal(sNombreAnimal);
			animal.setDrawableSonidoAnimal(sFiguraAnimal);
			//animal.setIdioma(sIdioma);
			
			listaAnimales.add(animal);
			
			cursor.moveToNext();
			
			}
		}
		*/
	}
	
	private OnItemClickListener clickListGridView = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int posicion,	long id) {
			String nombre = ((Animal)(parent.getAdapter().getItem(posicion))).getNombreAnimal();
			Log.i("Seleccionado", nombre);
			String sonido = ((Animal)parent.getAdapter().getItem(posicion)).getDrawableSonidoAnimal();
			if (sonido.equals(sonidoAnimalAleatorio)){
				Toast.makeText(Adivina.this, "Muy bien", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(Adivina.this, "Intenta de nuevo", Toast.LENGTH_SHORT).show();
			}
				
			
		}
	};
	
	public class ImageAdapter extends BaseAdapter{
		private Context myContext; 
		private ArrayList<Animal> listaCat;
		private Animal imagenes[];	
		private LayoutInflater inflater; 
		
        public ImageAdapter(Context c, int numCat, ArrayList<Animal> listaCat) { 
        	this.myContext = c;
        	this.listaCat = listaCat;
        	imagenes = new Animal[numCat]; 
        	for (int cont=0; cont<numCat; cont++){
               imagenes[cont] = listaCat.get(cont);
        	}
        	inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        } 

		public int getCount() {
			return this.imagenes.length;
		}

		public Object getItem(int posicion) {

			return listaCat.get(posicion);
		}

		public long getItemId(int posicion) {
			return posicion;
		}

		public View getView(int posicion, View convertView, ViewGroup parent) {
			DatosAnimalView holder;
			if (convertView == null) 
			{ 
			convertView = inflater.inflate(R.layout.animaladivina, null); 
			holder = new DatosAnimalView(); 
			holder.setImagenAnimal((ImageView)convertView.findViewById(R.id.ivImagenAnimalAdivina));
			holder.setNombreAnimal((TextView)convertView.findViewById(R.id.tvNombreAnimalAdivina));
			convertView.setTag(holder); 
			} 
			else 
			{ 
				holder = (DatosAnimalView) convertView.getTag(); 
			} 
			try{
				Resources res = getResources();
				
				int resID = getResources().getIdentifier(imagenes[posicion].getDrawableSonidoAnimal() , "drawable", getPackageName());
		    	Drawable drawable = res.getDrawable(resID); 
				holder.getImagenAnimal().setImageDrawable(drawable);
				holder.getNombreAnimal().setText(((Animal) getItem(posicion)).getNombreAnimal());

			}
			catch(Exception err){
				Log.e("Error en imagen", err.toString());
			}
			return convertView; 

		}
		
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
		Log.i("numero sonido", Integer.toString(iNumSonidoAleatorio));


		return totalAleatorio;
	}		
	
	
	OnCompletionListener completionList = new OnCompletionListener() {
		
		public void onCompletion(MediaPlayer mp) {
			mediaPlayerSonido.release();
		}
	};
	
	private void sonido(String sSonidoAnimal){
    	int resIDSonido = getResources().getIdentifier(sSonidoAnimal, "raw", getPackageName());
    	mediaPlayerSonido = MediaPlayer.create(Adivina.this, resIDSonido);
    	mediaPlayerSonido.start();
    	mediaPlayerSonido.setLooping(false);
    	mediaPlayerSonido.setOnCompletionListener(completionList);	
    	
	} 
	
	public int measureCellWidth( Context context, View cell )
	{

	    // We need a fake parent
	    RelativeLayout buffer = new RelativeLayout( context );
	    android.widget.AbsListView.LayoutParams layoutParams = 
	    		new  android.widget.AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    buffer.addView( cell, layoutParams);

	    cell.forceLayout();
	    cell.measure(1000, 1000);

	    int width = cell.getMeasuredWidth();

	    buffer.removeAllViews();

	    return width;
	}
	
}

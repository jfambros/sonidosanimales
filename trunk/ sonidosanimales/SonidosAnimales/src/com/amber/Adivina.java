package com.amber;

import java.util.ArrayList;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.amber.utils.AccesoDatos;
import com.amber.utils.Animal;
import com.amber.utils.DatosAnimalView;

public class Adivina extends Activity{
	private AccesoDatos accesoDatos;
	private Cursor cursor;
	private String sSonidoAnimal;
	private GridView gvAnimales;
	private ArrayList<Animal> listaAnimales;
	private ArrayList<Integer> listaNumero;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adivina);
		
		gvAnimales = (GridView)findViewById(R.id.gridViewAdivina);
		((GridView) findViewById(R.id.gridViewAdivina)).setOnItemClickListener(clickListGridView);
		
		obtenerAnimales(2);
		/*
		try{
			gvAnimales.setAdapter(new ImageAdapter(this, listaAnimales.size(), listaAnimales));
		}
		catch (Exception e) {
			Log.e("Error inicia adivina", e.toString());
		}
		*/

		
		for (int i=0; i<5; i++){
			Log.i("generando", Integer.toString(genera(cursor.getCount())));
		}
	}
	
	private void obtenerAnimales(int numero){
		accesoDatos = new AccesoDatos(this, "Animales.db");
		cursor = accesoDatos.seleccionaDatos("animal");
		listaAnimales = new ArrayList<Animal>();
		int numAleatorios[] = new int[numero];
		int totalReg = cursor.getCount();
		
		

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

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public class ImageAdapter extends BaseAdapter{
		private Context myContext; 
		private int numCat;
		private ArrayList<Animal> listaCat;
		private Animal imagenes[];	
		private LayoutInflater inflater; 
		
        public ImageAdapter(Context c, int numCat, ArrayList<Animal> listaCat) { 
        	this.myContext = c;
        	this.numCat = numCat;
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
				ImageView imagen = new ImageView(this.myContext);
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

	private int genera(int tam){
		if(listaNumero.size() < tam ){//Aun no se han generado todos los numeros
            int numeroA = (int)(Math.random()*(tam));//genero un numero
            if(listaNumero.isEmpty()){//si la lista esta vacia
                listaNumero.add(numeroA);
                return numeroA;
            }else{//si no esta vacia
                if(listaNumero.contains(numeroA)){//Si el numero que generé esta contenido en la lista
                    return genera(tam);//recursivamente lo mando a generar otra vez
                }else{//Si no esta contenido en la lista
                    listaNumero.add(numeroA);
                    return numeroA;
                }
            }
        }else{// ya se generaron todos los numeros
            return -1;
        }		
	}
	

}

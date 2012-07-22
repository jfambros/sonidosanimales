package com.amber;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.amber.utils.Animal;
import com.amber.utils.DFondo;
import com.amber.utils.OpcionesGenerales;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Fondo extends ListActivity{
	private ListaConImagenes adaptador;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listafondo);
		
		ImageView ivInicioFondo = (ImageView)findViewById(R.id.ivInicioFondoLista);
		ivInicioFondo.setOnClickListener(ivInicioFondoCL);
		
		try{
			obtenerFondos();
		}catch (Exception err){
			System.out.println(err.toString());
		}
	}
	
	private OnClickListener ivInicioFondoCL = new OnClickListener() {
		
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Fondo.this, Animales.class);
			startActivity(intent);
		}
	};
	
	private void obtenerFondos() throws SAXException, IOException, ParserConfigurationException{
		RecorrerXML recorrerXml = new RecorrerXML(this, R.raw.fondo);
		ArrayList<DFondo> datosFondo = recorrerXml.obtenerFondos();
		this.adaptador = new ListaConImagenes(this, R.layout.listafilafondo, datosFondo);
		setListAdapter(adaptador);
	}
	
	public class ListaConImagenes extends ArrayAdapter<DFondo> {

        private ArrayList<DFondo> items;

        public ListaConImagenes(Context context, int textViewResourceId, ArrayList<DFondo> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.listafilafondo, null);
                }
                DFondo listaAnimales = items.get(position);
                if (listaAnimales != null) {
                	
                	//se llena la lista de elementos
                	
                    	ImageView imagenAnimal = (ImageView) v.findViewById(R.id.ivFondoPeq);		                	
                        TextView nombreAnimal = (TextView) v.findViewById(R.id.tvNombreFondo);
    	                try{
    	    				Resources res = getResources();
    	    				
    	    				int resID = getResources().getIdentifier(((DFondo)items.get(position)).getImagen() , "drawable", getPackageName());
    	    		    	Drawable drawable = res.getDrawable(resID);
    	    		    	
    	    				imagenAnimal.setImageDrawable(drawable);
    	    				nombreAnimal.setText(((DFondo) getItem(position)).getNombreFrondo());
    	    			}
    	    			catch(Exception err){
    	    				Log.e("Error en imagen", err.toString());
    	    			}
                }
                return v;
        }
	}
	
    protected void onListItemClick(ListView l, View v, int position, long id) {
        DFondo fondo = (DFondo) l.getItemAtPosition(position);

        
        
       int resId = getResources().getIdentifier(fondo.getImagen() , "drawable", getPackageName());
       //OpcionesGenerales.fondo = resId;
       Intent intent = new Intent();
       intent.putExtra("resId", resId);
       intent.setClass(Fondo.this, Animales.class);
       startActivity(intent);
       //Resources res = getResources();
       //Drawable drawable = res.getDrawable(resId);
       //ivFondoAplicacion.setBackgroundDrawable(drawable);
       //ivFondoAplicacion.setForeground(drawable);
       //Toast.makeText(this, "Selección "+fondo.getImagen()+" "+resId , Toast.LENGTH_SHORT).show();       
        //ivFondoAplicacion.setImageResource(resId);
    }

}

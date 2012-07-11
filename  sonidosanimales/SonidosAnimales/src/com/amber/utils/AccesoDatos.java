package com.amber.utils;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.amber.R;
import com.amber.RecorrerXML;

public class AccesoDatos {
	private SQLiteDatabase sqlOperaciones;
	private Context context;
	private String nombreBD;
	private String tablaAnimal ="create table animal(_id integer not null, " +
			"nombre text not null, " +
			"drawableSonido text not null, " +
			"idioma text not null,"+
			"constraint idPk primary key (_id));";
	
	public AccesoDatos(Context context, String nombreBd){
		this.context = context;
		this.nombreBD = nombreBd;
	}
	
	public void abrirBase(){
		sqlOperaciones = context.openOrCreateDatabase(nombreBD, 1, null);	
	}
	public void creaTabla(){
		sqlOperaciones.execSQL("drop table if EXISTS animal");
		sqlOperaciones.execSQL(tablaAnimal);		
	}
	public Cursor seleccionaDatos(String tabla){
		Cursor cursor = sqlOperaciones.query(tabla, new String[]{"_id", "nombre","drawableSonido"}, null, null, null, null, null);

		return cursor;
	}
	
	public Cursor seleccionaUnDatoNombre(String tabla, String nombre){
		Cursor cursor = sqlOperaciones.query(tabla, new String[]{"_id", "nombre","drawableSonido"}, "nombre = '"+nombre+"'", null, null, null, null);

		return cursor;		
	}
	
	public Cursor seleccionaNumeroDatos(String tabla, String numeroFilas){
		Cursor cursor = sqlOperaciones.query(tabla, new String[]{"_id", "nombre","drawableSonido"}, null, null, null, null, null, numeroFilas);
		return cursor;
	}
	
	public void insertaDatos(String tabla, Activity activity) throws SAXException, IOException, ParserConfigurationException{
		RecorrerXML recorrerXML = new RecorrerXML(activity,R.raw.datos);
		ArrayList<Animal> arrayListAnimales = recorrerXML.obtenerDatos();
		for (int i = 0; i < arrayListAnimales.size(); i++){
			Animal animal = arrayListAnimales.get(i);
			ContentValues contentValues = new ContentValues();
			contentValues.put("nombre", animal.getNombreAnimal());
			contentValues.put("drawableSonido", animal.getDrawableSonidoAnimal());
			contentValues.put("idioma", animal.getIdioma());
			sqlOperaciones.insert(tabla, null, contentValues);	

		}
	}
	
	public void eliminaTabla(String tabla){
		sqlOperaciones.delete(tabla, "", null);
	}
	
	public void cierraBase(){
		sqlOperaciones.close();
	}

}

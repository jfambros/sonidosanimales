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

import com.amber.RecorrerXML;

public class AccesoDatos {
	private SQLiteDatabase sqlOperaciones;
	private Context context;
	private String nombreBD;
	
	public AccesoDatos(Context context, String nombreBd){
		this.context = context;
		this.nombreBD = nombreBd;
	}
	
	public Cursor seleccionaDatos(String tabla){
		sqlOperaciones = context.openOrCreateDatabase(nombreBD, 1, null);
		Cursor cursor = sqlOperaciones.query(tabla, new String[]{"_id", "nombre","drawableSonido"}, null, null, null, null, null);
		return cursor;
	}
	
	public Cursor seleccionaUnDatoNombre(String tabla, String nombre){
		sqlOperaciones = context.openOrCreateDatabase(nombreBD, 1, null);
		Cursor cursor = sqlOperaciones.query(tabla, new String[]{"_id", "nombre","drawableSonido"}, "nombre = '"+nombre+"'", null, null, null, null);
		return cursor;		
	}
	
	public void insertaDatos(String tabla, Activity activity) throws SAXException, IOException, ParserConfigurationException{
		RecorrerXML recorrerXML = new RecorrerXML(activity);
		ArrayList<Animal> arrayListAnimales = recorrerXML.obtenerDatos();
		sqlOperaciones = context.openOrCreateDatabase(nombreBD, 1, null);
		for (int i = 0; i < arrayListAnimales.size(); i++){
			Animal animal = arrayListAnimales.get(i);
			ContentValues contentValues = new ContentValues();
			contentValues.put("nombre", animal.getNombreAnimal());
			contentValues.put("drawableSonido", animal.getDrawableSonidoAnimal());
			sqlOperaciones.insert(tabla, null, contentValues);	
		}
	}
	
	public void eliminaTabla(String tabla){
		
		sqlOperaciones = context.openOrCreateDatabase(nombreBD, 1, null);
		sqlOperaciones.delete(tabla, "", null);
	}

}

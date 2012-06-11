package com.amber.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
		Cursor cursor = sqlOperaciones.query(tabla, new String[]{"_id", "nombre","drawable","sonido"}, null, null, null, null, null);
		return cursor;
	}
	
	public Cursor seleccionaUnDatoNombre(String tabla, String nombre){
		sqlOperaciones = context.openOrCreateDatabase(nombreBD, 1, null);
		Cursor cursor = sqlOperaciones.query(tabla, new String[]{"_id", "nombre","drawable","sonido"}, "nombre = '"+nombre+"'", null, null, null, null);
		return cursor;		
	}
	
	public void insertaDatos(String tabla){
		sqlOperaciones = context.openOrCreateDatabase(nombreBD, 1, null);
		String insertaDatos = "INSERT INTO animal(nombre, drawable, sonido) " +
				"SELECT 'Caballo', 'caballo', caballo' UNION " +
				"SELECT 'Pato', 'pato', 'pato' UNION " +
				"SELECT 'Elefante', 'elefante', 'elefante';";
		sqlOperaciones.rawQuery(insertaDatos, null);

	}

}

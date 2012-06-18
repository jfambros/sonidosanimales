package com.amber.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CreaBD extends SQLiteOpenHelper{
	private String tablaAnimal ="create table animal(_id integer not null, " +
			"nombre text not null, " +
			"drawableSonido text not null, " +
			"idioma text not null,"+
			"constraint idPk primary key (_id));";

	

	public CreaBD(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("DROP TABLE animal");
		db.execSQL(tablaAnimal);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE animal");
		db.execSQL(tablaAnimal);		
	}
	
	public void eliminaTabla(){
		
	}

}

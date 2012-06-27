package com.amber;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amber.utils.OpcionesGenerales;

public class Configuracion extends ListActivity{
	private OpcionesGenerales config = new OpcionesGenerales();

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setListAdapter(new ArrayAdapter<String>(this, R.layout.lista_opciones, config.getListaOpciones()));
		ListView listaOpciones = getListView();
		listaOpciones.setTextFilterEnabled(true);
		
		listaOpciones.setOnItemClickListener(listaOpcionesCL);
	}
	
	private OnItemClickListener listaOpcionesCL = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int pos,	long id) {
			Toast.makeText(Configuracion.this, ((TextView)view).getText(), Toast.LENGTH_SHORT).show();
			
		}
	};
}

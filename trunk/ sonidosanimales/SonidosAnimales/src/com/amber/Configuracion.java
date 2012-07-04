package com.amber;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amber.utils.OpcionesGenerales;

public class Configuracion extends ListActivity{
	private OpcionesGenerales config = new OpcionesGenerales();
	private Bundle bundle;
	private int numAnimales;
	private EditText etNumAnimales;
	private AlertDialog.Builder builder;
	private AlertDialog alertDialog;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setListAdapter(new ArrayAdapter<String>(this, R.layout.lista_opciones, config.getListaOpciones()));
		ListView listaOpciones = getListView();
		listaOpciones.setTextFilterEnabled(true);
		bundle = getIntent().getExtras();
		numAnimales = bundle.getInt("numAnimales");
		
		
		listaOpciones.setOnItemClickListener(listaOpcionesCL);
	}
	
	private OnItemClickListener listaOpcionesCL = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int pos,	long id) {
			//Toast.makeText(Configuracion.this, ((TextView)view).getText(), Toast.LENGTH_SHORT).show();
			if (pos == 0){
				dialogoNumeroAnimales();
			}
		}
	};
	
	private void dialogoNumeroAnimales(){

		
		LayoutInflater lInflater = (LayoutInflater)Configuracion.this.getSystemService(Configuracion.this.LAYOUT_INFLATER_SERVICE);
		View layout = lInflater.inflate(R.layout.dialogonumanimales, (ViewGroup)Configuracion.this.findViewById(R.layout.main));
		TextView etNumMax = (TextView)layout.findViewById(R.id.tvNumMaxAnimales);
		etNumAnimales = (EditText)layout.findViewById(R.id.etNumAnimales);
		etNumMax.setText(etNumMax.getText().toString() +" "+ Integer.toString(numAnimales));
		Button btnAceptar = (Button)layout.findViewById(R.id.btnAceptaNumAnimales);
		btnAceptar.setOnClickListener(btnAceptarCL);
		builder = new AlertDialog.Builder(Configuracion.this);
		builder.setView(layout);
		alertDialog = builder.create();
		alertDialog.setTitle("Mensaje");
		
		alertDialog.show();
		
	}
	private OnClickListener btnAceptarCL = new OnClickListener() {
		
		public void onClick(View arg0) {
			int numero = 2;
			if (etNumAnimales.getText().toString().length() != 0){
				numero = Integer.parseInt(etNumAnimales.getText().toString());
			}
			if (etNumAnimales.getText().length() != 0 && (numero >= 2 && numero <=numAnimales)){
				OpcionesGenerales.numeroAnimales = Integer.parseInt(etNumAnimales.getText().toString());
				alertDialog.cancel();
				Toast.makeText(Configuracion.this, Integer.toString(OpcionesGenerales.numeroAnimales), Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(Configuracion.this, "Revisa el número capturado", Toast.LENGTH_SHORT).show();
			}			
			
		}
	};
	
}

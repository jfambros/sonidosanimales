package com.amber;

import java.io.IOException;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.amber.utils.AccesoDatos;

public class Animales extends Activity implements android.view.GestureDetector.OnGestureListener{
	private TextView tvNombreAnimal;
	private ImageView ivImagenAnimal;
	private MediaPlayer mediaPlayerSonido;
	private final String nombreBD = "Animales.db";
	private AccesoDatos accesoDatos;
	private static Cursor cursorDatos;	
	private String sSonidoAnimal;
	private static Boolean inicio = true;
	private int numAnimales;
	private String animalSeleccionado;
	private AlertDialog alertDialogSeleccAnimal;
	private int iAnimalSeleccionado;
	private Bundle bundle = new Bundle();
	private int resId;
	
	private ViewFlipper viewFlipper = null;
	private GestureDetector gestureDetector = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animal);
		ImageView ivMenu = (ImageView)findViewById(R.id.ivMenu);
		ivMenu.setOnClickListener(ivMenuCL);
		
		ImageView ivAnterior = (ImageView)findViewById(R.id.ivAnterior);
		ivAnterior.setOnClickListener(ivAnteriorCL);
		ImageView ivSiguiente = (ImageView)findViewById(R.id.ivSiguiente);
		ivSiguiente.setOnClickListener(ivSiguienteCL);
		

		ivImagenAnimal = (ImageView)findViewById(R.id.ivImagenAnimal);
		ivImagenAnimal.setOnClickListener(ivImagenAnimalCL);
		
		tvNombreAnimal = (TextView)findViewById(R.id.tvNombreAnimal);
		
		ImageView ivAleatorio = (ImageView)findViewById(R.id.ivAleatorio);
		ivAleatorio.setOnClickListener(ivAleatorioCL);
		
		ImageView ivAdivina = (ImageView)findViewById(R.id.ivAdivinar);
		ivAdivina.setOnClickListener(ivAdivinaCL);
		FrameLayout flFondo = (FrameLayout)findViewById(R.id.frameFondoPrincipal);
		
		//ImageView ivGuardarSonido = (ImageView)findViewById(R.id.ivGuardarSonido);
		//ivGuardarSonido.setOnClickListener(ivGuardarSonidoCL);
		
		viewFlipper = (ViewFlipper)findViewById(R.id.viewflipper);
		gestureDetector = new GestureDetector(this);
		
		if (inicio == true){
			try{
				inicioDatos();
			}catch (Exception e) {
				Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
			}
		}else{
			
			llenaObjetos();
			
		}
		bundle = getIntent().getExtras();
		if (bundle != null){
			resId = bundle.getInt("resId");
			flFondo.setBackgroundResource(resId);
		}
		
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
	       case R.id.menuConfigurar:
	    	   inicio = true; 
	           Intent intent = new Intent();
	           intent.putExtra("numAnimales", numAnimales);
	           intent.putExtra("resId", resId);
	           
	           intent.setClass(Animales.this, Configuracion.class);
	           startActivity(intent);
	    	   return true; 
	       case R.id.menuBuscar:
	    	   buscaAnimal();
	    	   return true;
	       case R.id.menuAcercaDe:
	    	   acercaDe();
	    	   return true;
		}
		
		return true;
	}
	
	private void cerrar(){
		cursorDatos.close();
		accesoDatos.cierraBase();
	}

	


	//Menï¿½
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	//Listener
	private OnClickListener ivMenuCL = new OnClickListener() {
		
		public void onClick(View v) {
			Animales.this.openOptionsMenu();
		}
	};
	
	private OnClickListener ivAnteriorCL = new OnClickListener() {
		
		public void onClick(View v) {
			cambioAnimalAnt();
		}
	};
	
	private OnClickListener ivSiguienteCL = new OnClickListener() {
		
		public void onClick(View v) {
			//aleatorioImagenFondo(3);
			cambioAnimalSig();
		}
	};
	
	private OnClickListener ivAleatorioCL = new OnClickListener() {
		
		public void onClick(View arg0) {
			aleatorio();
		}
	};

	private OnClickListener ivImagenAnimalCL = new OnClickListener() {
		
		public void onClick(View v) {
			sonido(sSonidoAnimal);
		}
	};
	
	private OnClickListener ivAdivinaCL = new OnClickListener() {
		
		public void onClick(View v) {
			Intent intent = new Intent();
			if (resId == 0)
				resId = idPrimerFondo();
			intent.putExtra("resId", resId);
			intent.setClass(Animales.this, AdivinaLista.class);
			mediaPlayerSonido.release();
			startActivity(intent);	
			finish();
		}
	};
	/*
	private OnClickListener ivGuardarSonidoCL = new OnClickListener() {
		
		public void onClick(View v) {
			EsTelefono esTelefono = new EsTelefono();
			if (esTelefono.verifica(Animales.this) == true){
				guardarSonidoTelefono();
			}
			else
			{
				guardarSonidoNoTelefono();
			}
		}
	};
	*/
	private void inicioDatos() throws SAXException, IOException, ParserConfigurationException{

		accesoDatos = new AccesoDatos(getApplicationContext(), nombreBD);
		accesoDatos.abrirBase();
		accesoDatos.creaTabla();
		accesoDatos.insertaDatos("animal", this);	
		//startManagingCursor(cursorDatos);
		cursorDatos = accesoDatos.seleccionaDatos("animal");
		//startManagingCursor(cursorDatos);
		numAnimales = cursorDatos.getCount();
		if (cursorDatos.moveToFirst()){
			llenaObjetos(); 
		}
		accesoDatos.cierraBase();
		
		inicio = false;
		resId = idPrimerFondo();
	}
	
	private void cambioAnimalSig(){
		if (!cursorDatos.isLast()){
			cursorDatos.moveToNext();
			llenaObjetos();
		}else{
			cursorDatos.moveToFirst();
			llenaObjetos();
		}

	}
	
	private void cambioAnimalAnt(){

		if (!cursorDatos.isFirst()){
			cursorDatos.moveToPrevious();
			llenaObjetos();
		}else{
			cursorDatos.moveToLast();
			llenaObjetos();
		}	
	}
	
	private void llenaObjetos(){
		int iNombreAnimal = cursorDatos.getColumnIndexOrThrow("nombre");
		int iFiguraAnimal = cursorDatos.getColumnIndexOrThrow("drawableSonido");
		int iSonidoAnimal = cursorDatos.getColumnIndexOrThrow("drawableSonido");
		
		String sNombreAnimal = cursorDatos.getString(iNombreAnimal);
		String sFiguraAnimal = cursorDatos.getString(iFiguraAnimal);
		sSonidoAnimal = cursorDatos.getString(iSonidoAnimal);
		
		tvNombreAnimal.setText(sNombreAnimal);
		//imagen
		Resources res = getResources();
		int resID = getResources().getIdentifier(sFiguraAnimal , "drawable", getPackageName());
    	Drawable drawable = res.getDrawable(resID); 
    	ivImagenAnimal.setImageDrawable(drawable);	
    	//sonido
    	sonido(sSonidoAnimal);
    	numAnimales = cursorDatos.getCount();

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
	    	mediaPlayerSonido = null;
	    	mediaPlayerSonido = new MediaPlayer();
	    	mediaPlayerSonido = MediaPlayer.create(Animales.this, resIDSonido);
	    	mediaPlayerSonido.start();
	    	mediaPlayerSonido.setLooping(false);
	    	mediaPlayerSonido.setOnCompletionListener(completionList);
	} 
	
	private void aleatorio(){
		int numero;
		Random r = new Random();
		numero = (int)(r.nextInt(cursorDatos.getCount()));
		cursorDatos.moveToPosition(numero);
		llenaObjetos();
	}
	
	private void buscaAnimal(){
		Builder builder;
		LayoutInflater lInflater = (LayoutInflater)Animales.this.getSystemService(Animales.this.LAYOUT_INFLATER_SERVICE);
		View layout = lInflater.inflate(R.layout.dialogobuscar, (ViewGroup)Animales.this.findViewById(R.layout.main));
		Spinner spinAnimales = (Spinner)layout.findViewById(R.id.spinnerAnimales);
		Button btnBuscarAnimal = (Button)layout.findViewById(R.id.btnBuscarAnimal);
		


		
		builder = new AlertDialog.Builder(Animales.this);
		builder.setView(layout);
		alertDialogSeleccAnimal = builder.create();
		alertDialogSeleccAnimal.setTitle("Mensaje");
		String[] from = new String[]{"nombre"};
		int[] to = new int[]{android.R.id.text1};
		SimpleCursorAdapter adapter =  new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, cursorDatos, from, to );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item );
		spinAnimales.setPrompt("Selecciona");
		spinAnimales.setAdapter(adapter);
        spinAnimales.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, 
                            View view, 
                            int position, 
                            long id) {
                    	animalSeleccionado = ((TextView)view).getText().toString();
                    	iAnimalSeleccionado = position;
                    }

					public void onNothingSelected(AdapterView<?> arg0) {
					
					}
                }
            );
        
		OnClickListener btnBuscarAnimalCL = new OnClickListener() {
			
			public void onClick(View v) {
				Toast.makeText(Animales.this, animalSeleccionado, Toast.LENGTH_SHORT).show();
				alertDialogSeleccAnimal.cancel();
				cursorDatos.moveToPosition(iAnimalSeleccionado);
				llenaObjetos();
			}
		};
		
		btnBuscarAnimal.setOnClickListener(btnBuscarAnimalCL);
		
		alertDialogSeleccAnimal.show();
	}
	
	private int idPrimerFondo(){
		return getResources().getIdentifier("paisaje1magua" , "drawable", getPackageName());
	}
	
	private void acercaDe(){
		AlertDialog.Builder alert = new AlertDialog.Builder(Animales.this);
		
		DialogInterface.OnClickListener aceptar = new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface arg0, int arg1) {
				setResult(RESULT_OK);
			}
		};
		
		alert.setTitle("Acerca de");
		alert.setMessage("Sonidos de animales desarrollado por AmBerSoft \n" +
				"\nAlgunas imágenes tomadas de http://focaclipart.net23.net/ ");
		alert.setPositiveButton("Aceptar", aceptar);
		alert.show(); 
	}

	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		if (arg0.getX() - arg1.getX() > 120)  
        {  
            
            this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,  
                    R.anim.push_left_in));  
            this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,  
                    R.anim.push_left_out));  
            cambioAnimalSig();
            return true;  
        }
        else if (arg0.getX() - arg1.getX() < -120)  
        {  
            this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,  
                    R.anim.push_right_in));  
            this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,  
                    R.anim.push_right_out));  
            cambioAnimalAnt();
            return true;  
        }  
		return true;
	}

	public void onLongPress(MotionEvent arg0) {

		
	}

	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	public void onShowPress(MotionEvent arg0) {
		
	}

	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.gestureDetector.onTouchEvent(event);
	}
	/*
	public void guardarSonidoTelefono(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(Animales.this);
		 
        alertDialog.setTitle("Guardar sonido...");

        alertDialog.setMessage("¿Donde deseas guardar el sonido?");

        alertDialog.setIcon(R.drawable.salvar22x22);

        alertDialog.setPositiveButton("En memoria", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	copiar(getApplicationContext());
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	setResult(RESULT_CANCELED);
            }
        });

        alertDialog.setNeutralButton("Timbre de llamada", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(getApplicationContext(), "Timbre",
                                Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();
	}
	
	private void guardarSonidoNoTelefono(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(Animales.this);
		 
        alertDialog.setTitle("Guardar sonido...");

        alertDialog.setMessage("El sonido se guardará en la memoria del dispositivo");

        alertDialog.setIcon(R.drawable.salvar22x22);

        alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	copiar(getApplicationContext());
            }
        });
       alertDialog.show();
	}
	*/
	/*
	private void copiar(Context context){	
		AssetManager assetManager = getAssets();
        String[] files = null;
        Log.i("Mensaje", "Copiar");
        try {
            files = assetManager.list("file");
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        for(String filename : files) {
            Log.i("File name => ",filename);
        }
		/*
		File dest = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		
		OutputStream out = null;
		InputStream in = context.getResources().openRawResource(R.raw.abeja);
		Log.i("Archivo", in.toString());
		try {
			out = new FileOutputStream(new File(dest, "abeja.ogg"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		byte[] buf = new byte[1024];
		int len;
		try {
		    while ( (len = in.read(buf, 0, buf.length)) != -1){
		         out.write(buf, 0, len);
		    }
		}
		catch (Exception e) {
		}
		 finally {
		    // Ensure the Streams are closed:
		    try {
				in.close(); 
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}		   
		}
		Toast.makeText(Animales.this, dest.toString(), Toast.LENGTH_SHORT).show();
		*/
	
	
}

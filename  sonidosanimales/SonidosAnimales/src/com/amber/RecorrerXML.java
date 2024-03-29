package com.amber;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;

import com.amber.utils.Animal;
import com.amber.utils.DFondo;



public class RecorrerXML {
	private InputStream is;
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private	Document dom;
	
	public RecorrerXML(Activity activity, int i) throws SAXException, IOException, ParserConfigurationException{
		
		 is = activity.getResources().openRawResource(i);
		 factory =  DocumentBuilderFactory.newInstance();
		 builder  = factory.newDocumentBuilder();
		 dom = builder.parse(is);
		// Log.i("constructor xml",dom.toString());
	}
	
	public ArrayList<Animal> obtenerDatos(){
		final ArrayList<Animal> arrayListAnimales = new ArrayList<Animal>();
		 //Nos posicionamos en el nodo principal del �rbol (<rss>)
        Element root = dom.getDocumentElement(); 
        //Localizamos todos los elementos <item>
        NodeList items = root.getElementsByTagName("animal");
        //Recorremos la lista de animales
        for (int i=0; i<items.getLength(); i++)
        {
            Animal animal = new Animal();
            //Obtenemos la noticia actual
            Node item = items.item(i);

            //Obtenemos la lista de datos del animal actual
            NodeList datosAnimal = item.getChildNodes();
            //Procesamos cada dato del animal
            for (int j=0; j<datosAnimal.getLength(); j++)
            {
                Node dato = datosAnimal.item(j);
                String etiqueta = dato.getNodeName();

                if (etiqueta.equals("nombre"))
                {
                    animal.setNombreAnimal(dato.getFirstChild().getNodeValue());
                }else 
                if (etiqueta.equals("drawableSonido"))
                {
                    animal.setDrawableSonidoAnimal(dato.getFirstChild().getNodeValue());
                }else 
                if (etiqueta.equals("idioma"))
                {
                    animal.setIdioma(dato.getFirstChild().getNodeValue());
                }
                
            }
            arrayListAnimales.add(animal);
        }     
		return arrayListAnimales;
		
	}
	
	public ArrayList<DFondo>obtenerFondos(){
		final ArrayList<DFondo> arrayListaFondos = new ArrayList<DFondo>();
		 //Nos posicionamos en el nodo principal del �rbol (<rss>)
       Element root = dom.getDocumentElement(); 
       //Localizamos todos los elementos <item>
       NodeList items = root.getElementsByTagName("fondo");

       //Recorremos la lista de animales
       for (int i=0; i<items.getLength(); i++)
       {
           DFondo fondo = new DFondo();
           //Obtenemos la noticia actual
           Node item = items.item(i);

           //Obtenemos la lista de datos del animal actual
           NodeList datosFondo = item.getChildNodes();
           //Procesamos cada dato del animal
           for (int j=0; j<datosFondo.getLength(); j++)
           {
               Node dato = datosFondo.item(j);
               String etiqueta = dato.getNodeName();

               if (etiqueta.equals("nombreFondo"))
               {
                   fondo.setNombreFrondo(dato.getFirstChild().getNodeValue());
               }else 
               if (etiqueta.equals("imagenFondo"))
               {
                   fondo.setImagen(dato.getFirstChild().getNodeValue());
               }
               
           }
           arrayListaFondos.add(fondo);		
       }
       return arrayListaFondos;
	}	
}

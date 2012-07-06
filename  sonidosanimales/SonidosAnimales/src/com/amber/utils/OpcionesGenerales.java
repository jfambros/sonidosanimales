package com.amber.utils;

public class OpcionesGenerales {
	private String listaOpciones[] = new String[]{
			"Número de animales (adivinar)",
			"Cambiar fondo"
	};
	
	public static int numeroAnimales = 4;

	public String[] getListaOpciones() {
		return listaOpciones;
	}

	public void setListaOpciones(String[] listaOpciones) {
		this.listaOpciones = listaOpciones;
	}
	
	
}

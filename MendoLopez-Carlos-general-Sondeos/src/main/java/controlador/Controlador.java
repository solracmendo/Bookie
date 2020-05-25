package controlador;

import Sondeo_Info.Sondeo;

public interface Controlador {
	
	String createSondeo(String titulo, String descripcion, String apertura, String cierre, String minimo, String maximo);
	
	String anadirRespuesta(String id, String respuesta);
	
	Sondeo getSondeo(String id);
	
	boolean removeSondeo(String id);

}

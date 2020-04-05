package bookle.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import bookle.tipos.Actividad;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class Programa {

    private static final String URL_SERVICIO = "http://localhost:8080/api/";

    public static void main(String[] args) {

        // Invocar operacion: crear una actividad

        Client cliente = Client.create();
        String path = "actividades";

        WebResource recurso = cliente.resource(URL_SERVICIO + path);

 // 1. Creacion de actividades
        
        System.out.println("Creando actividad 1 ...");
        
        MultivaluedMap<String, String> parametros = new MultivaluedMapImpl();
        parametros.add("titulo", "Actividad 1");
        parametros.add("descripcion", "Ejemplo");
        parametros.add("profesor", "Pepe");
        parametros.add("email", "pepe@gmail.com");

        ClientResponse respuesta = recurso.method("POST", ClientResponse.class, parametros);

        System.out.println("Codigo de retorno: " + respuesta.getStatusInfo());

        String actividad1URL = respuesta.getLocation().toString();
        
        System.out.println("Actividad creada: " + actividad1URL);
        
 // 2. Consulta actividad
        
        WebResource recursoActividad1 = cliente.resource(actividad1URL);
        
        System.out.println("Consultando actividad: " + recursoActividad1);
        
        ClientResponse respuesta2 = recursoActividad1
                            .accept(MediaType.APPLICATION_XML)
                            .method("GET", ClientResponse.class);
        
        System.out.println("Codigo de retorno: " + respuesta2.getStatusInfo());
        
        Actividad actividad = respuesta2.getEntity(Actividad.class);
        
        System.out.println("Actividad titulo: " + actividad.getTitulo());
        
        
  //3. Actualizacion de actuiividad
        
        System.out.println("Actualizando actividad: " + recursoActividad1);
        MultivaluedMap<String, String> parametros_Mod = new MultivaluedMapImpl();
        parametros_Mod.add("titulo", "Actividad JAJA");
        parametros_Mod.add("descripcion", "EjemploJAJA");
        parametros_Mod.add("profesor", "PepeJAJA");
        parametros_Mod.add("email", "pepe@gmail.comJAJA");
        ClientResponse respuesta3 = recursoActividad1
        							.method("PUT",ClientResponse.class,parametros_Mod);
        System.out.println("Codigo de retorno: " + respuesta3.getStatusInfo());
        
        
        //Consulta adicional
        
        WebResource recursoActividadCons = cliente.resource(actividad1URL);
        
        System.out.println("Consultando actividad: " + recursoActividadCons);
        
        ClientResponse respuestaAdic = recursoActividadCons
                            .accept(MediaType.APPLICATION_XML)
                            .method("GET", ClientResponse.class);
        
        System.out.println("Codigo de retorno: " + respuestaAdic.getStatusInfo());
        
        Actividad actividadAdd = respuestaAdic.getEntity(Actividad.class);
        
        System.out.println("Actividad titulo: " + actividadAdd.getTitulo());
        
        
  //4. Eliminacion de actividad   
        System.out.println("Eliminando actividad: " + recursoActividadCons);
        ClientResponse respDel = recursoActividadCons.method("DELETE",ClientResponse.class);
        System.out.println("Codigo de retorno: " + respDel.getStatusInfo());
        
        
  
    }
}
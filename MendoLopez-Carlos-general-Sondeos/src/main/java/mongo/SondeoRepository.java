package mongo;



import java.util.ArrayList;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;

import Sondeo_Info.Respuesta;
import Sondeo_Info.Sondeo;

import com.mongodb.client.model.Filters;

public class SondeoRepository {
	
	private final MongoCollection<Document> sondeos;
	
	public SondeoRepository(MongoCollection<Document> sondeos) {
		this.sondeos = sondeos;
	}
	
	private Sondeo sondeo(Document doc) { //Obtener informacion de un sondeo a partir de un documento

		ArrayList<Respuesta> nuevasResps = new ArrayList<Respuesta>();
		@SuppressWarnings("unchecked")
		ArrayList<Document> resps =(ArrayList<Document>)doc.get("respuestas");
		for(Document a : resps) {
			String nombre = a.getString("nombre");
			Integer cantidad = a.getInteger("cantidad");
			Respuesta respuestaNueva = new Respuesta(nombre,cantidad);
			nuevasResps.add(respuestaNueva);
		}
		
		return new Sondeo (
				doc.get("_id").toString(),
				doc.getString("pregunta"),
				doc.getString("descripcion"),
				nuevasResps,
				doc.getString("inicio"),
				doc.getString("fin"),
				doc.getInteger("minimo"),
				doc.getInteger("maximo"));
		}
	
	
	public Sondeo saveSondeo(Sondeo sondeo) { //Guardar sondeo
		Document doc = new Document();
		doc.append("pregunta",sondeo.getPregunta());
		doc.append("descripcion", sondeo.getDescripcion());
		doc.append("respuestas",new BasicDBList());
		doc.append("inicio", sondeo.getInicio());
		doc.append("fin", sondeo.getFin());
		doc.append("minimo", sondeo.getMinimo());
		doc.append("maximo", sondeo.getMaximo());
		sondeos.insertOne(doc);
		return sondeo(doc);
	}
	
	public Sondeo findById(String id) { //Encontrar sondeo por Id
		Document doc = sondeos.find(Filters.eq("_id", new ObjectId(id))).first();
		return sondeo(doc);
	}
	
	public boolean existId(String id) { //Comprobar si existe sondeo a partir del Id
		Document doc = sondeos.find(Filters.eq("_id", new ObjectId(id))).first();
		if(doc == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public Document findByIdDocument(String id) { //Encontrar documento de sondeo a través de Id
		return sondeos.find(Filters.eq("_id", new ObjectId(id))).first();
	}
	
	public ArrayList<Sondeo> getAllSondeos() { //Encontrar todos los sondeos
		ArrayList<Sondeo> allSondeos = new ArrayList<>();
		for(Document doc : sondeos.find()) {
			allSondeos.add(sondeo(doc));
		}
		return allSondeos;
	}
	
	public boolean deleteById(String id) { //Eliminar sondeo por Id
		try {
			sondeos.deleteOne(new Document("_id", new ObjectId(id)));
			return true;
		} catch (MongoException e) {
			return false;
		}
	}

	public boolean AnadirRespuestaById(String id,String respuesta) { //Anadir respuesta a un sondeo por id
		try {
			
			Document doc = findByIdDocument(id);

			@SuppressWarnings("unchecked")
			ArrayList<BasicDBObject> lista =  (ArrayList<BasicDBObject>)doc.get("respuestas");
			
			
			lista.add((new BasicDBObject("nombre", respuesta).append("cantidad",0)));
				
			sondeos.updateOne(Filters.eq("_id",  new ObjectId(id)), new Document("$set",new Document("respuestas", lista)));
			
			return true;
			
		} catch (MongoException e) {
			return false;
		}
		
	}
	
	public boolean ResponderRespuestaById(String id, String respuesta) { //Contestar a un sondeo
		try {
			
			sondeos.updateOne(Filters.and(Filters.eq("_id", new ObjectId(id)), Filters.eq("respuestas.nombre", respuesta)), new Document("$inc", new Document("respuestas.$.cantidad",1)));
			return true;
			
		} catch (MongoException e) {
			return false;
		}
	}
	
	

}

package mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import modelo.Tarea;

public class TareaRepository {
	
	private final MongoCollection<Document> tareas;
	
	public TareaRepository( MongoCollection<Document> tareas) {
		this.tareas = tareas;
	}
	
	public List<Tarea> getAllTareas() {
		List<Tarea> allTareas = new ArrayList<>();
		for(Document doc : tareas.find()) {
			allTareas.add(tarea(doc));
		}
		return allTareas;
	}
	
	public List<Tarea> getMyTareas(String email){
		List<Tarea> myTareas = new ArrayList<>();
		for(Document doc : tareas.find(Filters.eq("email", email))) {
			myTareas.add(tarea(doc));
		}
		return myTareas;
	}
	
	public Tarea findTarea(String email,String identificador_Personal, String servicio) {
		Document doc = tareas.find(Filters.and(Filters.eq("identificador",identificador_Personal), Filters.eq("email", email), Filters.eq("servicio", servicio))).first();
		return tarea(doc);
	}
	
	public boolean deleteTarea(String email,String identificador_Personal, String servicio) {
		try {
			tareas.deleteOne(Filters.and(Filters.eq("identificador",identificador_Personal), Filters.eq("email", email), Filters.eq("servicio", servicio)));
			return true;
		} catch (MongoException e) {
			return false;
		}
	}
	
	public void deleteAllId(String identificador, String servicio) {
		try {
			tareas.deleteMany(Filters.and(Filters.eq("identificador",identificador),Filters.eq("servicio", servicio)));
		} catch (MongoException e) {
			return;
		}
	}
	
	private Tarea tarea(Document document) {
		return new Tarea(
				document.get("_id").toString(),
				document.getString("nombre"),
				document.getString("identificador"),
				document.getString("servicio"),
				document.getString("email"));
	}
	
	public Tarea saveTarea(Tarea tarea) {
		Document doc = new Document();
		doc.append("nombre", tarea.getNombre());
		doc.append("identificador", tarea.getIdentificador_Personal());
		doc.append("servicio", tarea.getServicio());
		doc.append("email", tarea.getEmail());
		
		tareas.insertOne(doc);
		
		return tarea(doc);
	}

}

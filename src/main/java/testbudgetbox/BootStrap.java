package testbudgetbox;

import static spark.Spark.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import spark.Request;
import testbudgetbox.commons.Aliment;
import testbudgetbox.commons.Groupe;
import testbudgetbox.commons.SousGroupe;
import testbudgetbox.services.AlimentService;
import testbudgetbox.services.GroupeService;
import testbudgetbox.services.SousGroupeService;

public class BootStrap {

	public static final Logger logger = LoggerFactory.getLogger(BootStrap.class);

	private AlimentService alimentService;
	private GroupeService groupeService;
	private SousGroupeService sousgroupeservice;
	
	public BootStrap(AlimentService alimentService, GroupeService groupeService, SousGroupeService sousgroupeservice){
		this.alimentService=alimentService;
		this.groupeService=groupeService;
		this.sousgroupeservice=sousgroupeservice;
	}
	
	public void start(){
		initExceptionHandler((e) -> logger.error("Server not starting\n"+e.getMessage()));
	    before("/*", (q, a) -> logger.info("Received api call"));
		// conservé pour vérifier que le serveur fonctionne.
		get("/", (req, res) -> "Hello World");
		// groupes :
		path("/groupe", () -> {
			get("/", "application/json", (req, res) -> {
				List<Groupe> liste = groupeService.getAllGroupes();
				String retour="[";
				boolean first = true;
				for (Groupe groupe : liste) {
					if (!first)
						retour += ",";
					else 
						first=false;
					retour += groupe.toJSONString();
				}
				retour += "]";
				return retour;
			});
			get("/:id", "application/json", (req, res) -> { 
				Groupe groupe = groupeService.getGroupeParId(getId(req));
				return groupe.toJSONString();
			});
			post("/", "application/json", (req, res) -> { 
				Groupe groupe = groupeService.addGroupe(getGroupe(req));
				return "Groupe ajouté à l'Id "+groupe.getId();
			});
			put("/", "application/json", (req, res) -> { 
				Groupe groupe = getGroupe(req);
				groupeService.updateGroupe(groupe);
				return "Groupe "+groupe.getId()+" modifié";
			});
			delete("/:id", "application/json", (req, res) -> {
				int id = getId(req);
				groupeService.delGroupe(id);
				return "Groupe "+id+" supprimé";
			});
		});
		// sousgroupes :
		path("/sousgroupe", () -> {
			get("/", "application/json", (req, res) -> {
				List<SousGroupe> liste = sousgroupeservice.getAllSousGroupes();
				String retour="[";
				boolean first = true;
				for (SousGroupe sousgroupe : liste) {
					if (!first)
						retour += ",";
					else 
						first=false;
					retour += sousgroupe.toJSONString();
				}
				retour += "]";
				return retour;
			});
			get("/:id", "application/json", (req, res) -> {
				SousGroupe sousgroupe = sousgroupeservice.getSousGroupeParId(getId(req));
				return sousgroupe.toJSONString();
			});
			post("/", "application/json", (req, res) -> {
				SousGroupe sousGroupeSent = getSousGroupe(req);
				SousGroupe sousgroupe = sousgroupeservice.addSousGroupe(sousGroupeSent);
				return "SousGroupe ajouté à l'Id "+sousgroupe.getId();
			});
			put("/", "application/json", (req, res) -> {
				SousGroupe sousgroupe = getSousGroupe(req);
				sousgroupeservice.updateSousGroupe(sousgroupe);
				return "SousGroupe "+sousgroupe.getId()+" modifié";
			});
			delete("/:id", "application/json", (req, res) -> {
				int id = getId(req);
				sousgroupeservice.delSousGroupe(id);
				return "SousGroupe "+id+" supprimé";
			});
		});
		// aliments :
		path("/aliment", () -> {
			get("/", "application/json", (req, res) -> {
				List<Aliment> liste = alimentService.getAllAliments();
				String retour="[";
				boolean first = true;
				for (Aliment aliment : liste) {
					if (!first)
						retour += ",";
					else 
						first=false;
					retour += aliment.toJSONString();
				}
				retour += "]";
				return retour;
			});
	        get("/:id", "application/json", (req, res) -> {
	        	Aliment aliment = alimentService.getAlimentParId(getId(req));
				return aliment.toJSONString();
			});
	        post("/", "application/json", (req, res) -> {
	        	Aliment aliment = alimentService.addAliment(getAliment(req));
				return "Aliment ajouté à l'Id "+aliment.getId();
			});
	        put("/", "application/json", (req, res) -> {
	        	Aliment aliment = getAliment(req);
				alimentService.updateAliment(aliment);
	        	return "Aliment "+aliment.getId()+" modifié";
			});
		    delete("/:id", "application/json", (req, res) -> {
		    	int id = getId(req);
				alimentService.delAliment(id);
		    	return "Aliment "+id+" supprimé";
			});
		});
	}

	
	
	protected Aliment getAliment(Request request) {
		JsonElement parsed = JsonParser.parseString(request.body());
		JsonObject parsedObject = parsed.getAsJsonObject();
		int id = parsedObject.get("id").getAsInt();
		String nom = parsedObject.get("nom").getAsString();
		String nomScient = parsedObject.get("nomSc").getAsString();
		int sousgroupeId = parsedObject.get("sousgroupeid").getAsInt();
		SousGroupe sousgroupe=null;
		try {
			sousgroupe = sousgroupeservice.getSousGroupeParId(sousgroupeId);
		} catch (Exception e) {}
		return new Aliment(id, nom, nomScient, sousgroupe);
	}

	protected SousGroupe getSousGroupe(Request request) {
		JsonElement parsed = JsonParser.parseString(request.body());
		JsonObject parsedObject = parsed.getAsJsonObject();
		int id = parsedObject.get("id").getAsInt();
		String nom = parsedObject.get("nom").getAsString();
		int groupeId = parsedObject.get("groupeid").getAsInt();
		Groupe groupe=null;
		try {
			groupe = groupeService.getGroupeParId(groupeId);
		} catch (Exception e) {}
		return new SousGroupe(id, groupe, nom);
	}

	protected Groupe getGroupe(Request request) {
		JsonElement parsed = JsonParser.parseString(request.body());
		JsonObject parsedObject = parsed.getAsJsonObject();
		int id = parsedObject.get("id").getAsInt();
		String nom = parsedObject.get("nom").getAsString();
		return new Groupe(id, nom);
	}
	
	protected int getId(Request request, String champ) {
		int id = -1;
		try {
			String paramId = request.params(":"+champ);
			if (paramId!=null)
				id = Integer.parseInt(paramId);
			else {
				JsonElement parsed = JsonParser.parseString(request.body());
				JsonObject parsedObject = parsed.getAsJsonObject();
				id = parsedObject.get(champ).getAsInt();
			}
		}
		catch (Exception e) {}
		return id;
	}

	protected int getId(Request request) {
		return getId(request, "id");
	}

}

package testbudgetbox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import testbudgetbox.commons.Aliment;
import testbudgetbox.commons.Groupe;
import testbudgetbox.commons.SousGroupe;
import testbudgetbox.services.AlimentService;
import testbudgetbox.services.GroupeService;
import testbudgetbox.services.SousGroupeService;

public class ImportCsv {
	private static final String SEPARATOR = ",";
	private static final String QUOTE = "\"";
	private static final String NULL = "NULL";

	private static final Logger logger = LoggerFactory.getLogger(ImportCsv.class);

	private GroupeService groupeService;
	private AlimentService alimentService;
	private SousGroupeService sousGroupeService;
	
	
	public ImportCsv(GroupeService groupeService, SousGroupeService sousGroupeService, AlimentService alimentService) {
		this.alimentService=alimentService;
		this.sousGroupeService=sousGroupeService;
		this.groupeService=groupeService;
	}

	public void traitement(String fileName) {
		try {
			readCsvFile(fileName);
		}
		catch(IOException e) {
			logger.warn("Accès fichier csv impossible");
		}
	}
		
	
	public void readCsvFile(String fileName) throws IOException{
		List<String> lignes = Files.readAllLines(Paths.get(fileName));
		boolean firstligne=true;
		for (String ligne : lignes) {
			if (firstligne) {
				firstligne=false;
				continue;
			}
			List<String >data = splitLigne(ligne);
			if (data.size()>=4) {
				String nomAlim=data.get(0);
				String nomScAlim=data.get(1);
				String nomGroupe=data.get(2);
				String nomSousGroupe=data.get(3);
				Groupe groupe = getGroupe(nomGroupe);
				if (groupe!=null) {
					SousGroupe sousgroupe = getSousGroupe(nomSousGroupe, groupe);
					if (sousgroupe!=null) {
						if (nomScAlim.equals(NULL))
							nomScAlim=null;
						Aliment aliment = new Aliment(-1, nomAlim, nomScAlim, sousgroupe);
						try {
							alimentService.addAliment(aliment);
						}
						catch(Exception E) {
							logger.error("erreur d'écriture aux donnees de groupe");
						}
					}
				}
			}
		}
	}
	
	private List<String> splitLigne(String ligne){
		if (!ligne.contains(QUOTE))
			return Arrays.asList(ligne.split(SEPARATOR));
		List<String> listeChamps = new LinkedList<String>(Arrays.asList(ligne.split(SEPARATOR)));
		while(!traitechamps(listeChamps));
		return listeChamps;
	}
	
	private boolean traitechamps(List<String> listeChamps) {
		boolean termine=true;
		for (int i=0;i<listeChamps.size();i++) {
			String champ=listeChamps.get(i);
			if (champ.startsWith(QUOTE) && champ.endsWith(QUOTE))
				listeChamps.set(i, champ.substring(1, champ.length()-1));
			else if (champ.startsWith(QUOTE) && i>=listeChamps.size()-1)
				listeChamps.set(i, champ.substring(1, champ.length()));
			else if (champ.startsWith(QUOTE)) {
				String champSuiv = listeChamps.get(i+1);
				listeChamps.set(i, champ+SEPARATOR+champSuiv);
				listeChamps.remove(i+1);
				termine=false;
				break;
			}
		}
		return termine;
	}
	
	private Groupe getGroupe(String nomGroupe){
		Groupe groupe = null;
		try {
			groupe = groupeService.getGroupeParNom(nomGroupe);
		}
		catch (Exception e) {
			logger.error("erreur d'acces aux donnees de groupe");
		}
		if (groupe==null)
			try {
				groupe = groupeService.addGroupe(new Groupe(-1, nomGroupe));
			}
			catch (Exception e) {
				logger.error("erreur d'écriture aux donnees de groupe");
			}
		return groupe;
	}
	
	private SousGroupe getSousGroupe(String nomSousGroupe, Groupe groupe){
		SousGroupe sousgroupe = null;
		try {
			sousgroupe = sousGroupeService.getSousGroupeParNom(nomSousGroupe, groupe);
		}
		catch (Exception e) {
			logger.error("erreur d'acces aux donnees de sous-groupe");
		}
		if (sousgroupe==null)
			try {
				sousgroupe = sousGroupeService.addSousGroupe(new SousGroupe(-1, groupe, nomSousGroupe));
			}
			catch (Exception e) {
				logger.error("erreur d'écriture aux donnees de sous-groupe");
			}
		return sousgroupe;
	}
	
}

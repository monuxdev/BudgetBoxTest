package testbudgetbox.services;

import java.util.List;

import testbudgetbox.commons.Groupe;
import testbudgetbox.commons.Repository;
import testbudgetbox.commons.SousGroupe;

public class SousGroupeService {
	
	private Repository<SousGroupe> repo;
	
	public SousGroupeService(Repository<SousGroupe> repo) {
		this.repo=repo;
	}

	public SousGroupe getSousGroupeParNom(String nom, Groupe groupe) throws Exception {
		for (SousGroupe sousGroupe : repo.getAll())
			if (sousGroupe.getNom().equals(nom) && sousGroupe.getGroupe().equals(groupe))
				return sousGroupe;
		return null;
	}

	public SousGroupe getSousGroupeParNom(String nom) throws Exception {
		for (SousGroupe sousGroupe : repo.getAll())
			if (sousGroupe.getNom().equals(nom))
				return sousGroupe;
		return null;
	}

	public SousGroupe getSousGroupeParId(int id) throws Exception {
		return repo.getMap().get(id);
	}

	public List<SousGroupe> getAllSousGroupes() throws Exception {
		return repo.getAll();
	}

	public void delSousGroupe(SousGroupe sousgroupe) throws Exception {
		repo.del(sousgroupe);
	}

	public void delSousGroupe(int id) throws Exception {
		repo.del(getSousGroupeParId(id));
	}

	public SousGroupe updateSousGroupe(SousGroupe sousgroupe) throws Exception {
		repo.update(sousgroupe);
		return sousgroupe;
	}

	public SousGroupe addSousGroupe(SousGroupe sousGroupe) throws Exception{
		return repo.add(sousGroupe);
	}
}

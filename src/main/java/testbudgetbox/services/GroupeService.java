package testbudgetbox.services;

import java.util.List;

import testbudgetbox.commons.Groupe;
import testbudgetbox.commons.Repository;
import testbudgetbox.commons.SousGroupe;

public class GroupeService {
	
	private Repository<Groupe> repo;
	
	public GroupeService(Repository<Groupe> repo) {
		this.repo=repo;
	}

	public Groupe getGroupeParNom(String nom) throws Exception {
		for (Groupe groupe : repo.getAll())
			if (groupe.getNom().equals(nom))
				return groupe;
		return null;
	}
	
	public Groupe getGroupeParId(int id) throws Exception {
		return repo.getMap().get(id);
	}

	public List<Groupe> getAllGroupes() throws Exception {
		return repo.getAll();
	}

	public void delGroupe(Groupe groupe) throws Exception {
		repo.del(groupe);
	}

	public void delGroupe(int id) throws Exception {
		repo.del(getGroupeParId(id));
	}

	public void updateGroupe(Groupe groupe) throws Exception {
		repo.update(groupe);
	}

	public Groupe addGroupe(Groupe groupe) throws Exception{
		return repo.add(groupe);
	}

}

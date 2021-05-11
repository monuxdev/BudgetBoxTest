package testbudgetbox.services;

import java.util.List;

import testbudgetbox.commons.Aliment;
import testbudgetbox.commons.Repository;

public class AlimentService {
	
	private Repository<Aliment> repo;
	
	public AlimentService(Repository<Aliment> repo) {
		this.repo=repo;
	}

	public Aliment getAlimentParNom(String nom) throws Exception {
		for (Aliment alim : repo.getAll())
			if (alim.getNom().equals(nom))
				return alim;
		return null;
	}
	
	public Aliment getAlimentParId(int id) throws Exception {
		return repo.getMap().get(id);
	}

	public List<Aliment> getAllAliments() throws Exception {
		return repo.getAll();
	}

	public void delGroupe(Aliment aliment) throws Exception {
		repo.del(aliment);
	}

	public void delAliment(int id) throws Exception {
		repo.del(getAlimentParId(id));
	}

	public void updateAliment(Aliment aliment) throws Exception {
		repo.update(aliment);
	}

	public Aliment addAliment(Aliment aliment) throws Exception{
		return repo.add(aliment);
	}
}

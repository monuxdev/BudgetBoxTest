package testbudgetbox.commons;

public class SousGroupe {
	private int id;
	private Groupe groupe;
	private String nom;
	
	public SousGroupe(int id, Groupe groupe, String nom) {
		this.id=id;
		this.setGroupe(groupe);
		this.nom=nom;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}

	public Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}

	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SousGroupe)
			return ((SousGroupe) obj).getId()==getId();
		return false;
	}
	
	public String toJSONString(){
		return "{"+
			"\"id\": "+getId()+","+
			"\"nom\": \""+getNom()+"\","+
			"\"groupeid\": "+getGroupe().getId()+","+
			"\"groupe\": \""+getGroupe().getNom()+"\","+
			"}";
	}
	
	
}

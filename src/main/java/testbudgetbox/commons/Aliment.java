package testbudgetbox.commons;

public class Aliment {
	private int id;
	private String nom;
	private String nomScient;
	private SousGroupe sousGroupe;
	
	public Aliment(int id, String nom, String nomScient, SousGroupe sousGroupe) {
		this.id=id;
		this.setSousGroupe(sousGroupe);
		this.nom=nom;
		this.nomScient=nomScient;
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

	public String getNomScient() {
		return nomScient;
	}

	public void setNomScient(String nomScient) {
		this.nomScient = nomScient;
	}

	public SousGroupe getSousGroupe() {
		return sousGroupe;
	}

	public void setSousGroupe(SousGroupe sousGroupe) {
		this.sousGroupe = sousGroupe;
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
			"\"nomSc\": \""+getNomScient()+"\","+
			"\"groupeid\": "+getSousGroupe().getGroupe().getId()+","+
			"\"groupe\": \""+getSousGroupe().getGroupe().getNom()+"\","+
			"\"sousgroupeid\": "+getSousGroupe().getId()+","+
			"\"sousgroupe\": \""+getSousGroupe().getNom()+"\","+
			"}";
	}
}

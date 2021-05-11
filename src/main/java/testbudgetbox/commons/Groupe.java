package testbudgetbox.commons;

public class Groupe {
	private int id;
	private String nom;
	
	public Groupe(int id, String nom) {
		this.id=id;
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
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Groupe)
			return ((Groupe) obj).getId()==getId();
		return false;
	}
	
	public String toJSONString(){
		return "{"+
			"\"id\": "+getId()+","+
			"\"nom\": \""+getNom()+"\","+
			"}";
	}
}

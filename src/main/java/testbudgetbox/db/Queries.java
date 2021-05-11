package testbudgetbox.db;

public enum Queries {
	LISTE_GROUPES("SELECT id, nom FROM groupe"),
	ADD_GROUPE("INSERT INTO groupe(id, nom) VALUES (?, ?)"),
	UPDATE_GROUPE("UPDATE groupe SET nom=? WHERE id=?"),
	DEL_GROUPE("DELETE groupe WHERE id=?"),
	LISTE_SOUS_GROUPES("SELECT id, nom, groupe_id FROM sousgroupe"),
	ADD_SOUS_GROUPE("INSERT INTO sousgroupe(id, nom, groupe_id) VALUES(?, ?, ?)"),
	UPDATE_SOUS_GROUPE("UPDATE sousgroupe SET nom=?, groupe_id=? WHERE id=?"),
	DEL_SOUS_GROUPE("DELETE sousgroupe WHERE id=?"),
	LISTE_ALIMENTS("SELECT id, nom, nom_sc, sousgroupe_id FROM aliment"),
	ADD_ALIMENT("INSERT INTO aliment(id, nom, nom_sc, sousgroupe_id) VALUES(?, ?, ?, ?)"),
	UPDATE_ALIMENT("UPDATE aliment SET nom=?, nom_sc=?, sousgroupe_id=? WHERE id=?"),
	DEL_ALIMENT("DELETE aliment WHERE id=?");
	
	private String query;
	
	Queries(String query){
		this.query=query;
	}
	
	@Override
	public String toString() {
		return this.query;
	}
}

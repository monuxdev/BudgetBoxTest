package testbudgetbox.db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import testbudgetbox.commons.Groupe;
import testbudgetbox.commons.Repository;
import testbudgetbox.db.Connexion;
import testbudgetbox.db.Queries;

public class GroupeDbRepo implements Repository<Groupe>{
	
	private Connexion connexion;
	private Map<Integer, Groupe> mapGroupe=null;

	public GroupeDbRepo(Connexion connexion) {
		this.connexion=connexion;
	}

	@Override
	public Groupe get(int id) throws SQLException{
		Groupe groupe = getMap().get(id);
		return groupe;
	}

	private int getNewId() throws SQLException{
		int newId=1;
		for (int key : getMap().keySet()) {
			if (newId<=key)
				newId=key+1;
		}
		return newId;
	}
	
	@Override
	public Groupe add(Groupe groupe) throws SQLException{
		int id=getNewId();
		groupe.setId(getNewId());
		// on modifie la map avant la connexion
		getMap().put(id, groupe);
		Connection conn = connexion.getConnexion();
		String query = Queries.ADD_GROUPE.toString();
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setInt(1, groupe.getId());
		stmt.setString(2, groupe.getNom());
		stmt.execute();
		refresh();
		return groupe;
	}

	@Override
	public Map<Integer, Groupe> getMap() throws SQLException{
		if (mapGroupe==null) {
			mapGroupe=new HashMap<Integer, Groupe>();
			refresh();
		}
		return mapGroupe;
	}

	public void refresh() throws SQLException{
		Connection conn = connexion.getConnexion();
		String query = Queries.LISTE_GROUPES.toString();
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		if (rs!=null && !rs.isClosed()) {
			// on vide la map uniquement si on peut se connecter
			mapGroupe.clear();
			while (rs.next()) {
				int id=rs.getInt("id");
				String nom=rs.getString("nom");
				Groupe groupe = new Groupe(id, nom);
				mapGroupe.put(id, groupe);
			}
			rs.close();
		}
	}
	
	public Groupe getParNom(String nom) throws SQLException {
		for (Groupe g : getMap().values())
			if (g.getNom().equals(nom))
				return g;
		return null;
	}

	@Override
	public boolean del(Groupe groupe) throws SQLException {
		Connection conn = connexion.getConnexion();
		String query = Queries.DEL_ALIMENT.toString();
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setInt(1, groupe.getId());
		stmt.execute();
		conn.commit();
		getMap().remove(groupe.getId());
		return true;
	}

	@Override
	public Groupe update(Groupe groupe) throws SQLException {
		Connection conn = connexion.getConnexion();
		String query = Queries.UPDATE_GROUPE.toString();
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, groupe.getNom());
		stmt.setInt(2, groupe.getId());
		stmt.execute();
		conn.commit();
		getMap().put(groupe.getId(), groupe);
		return groupe;
	}

	@Override
	public List<Groupe> getAll() throws SQLException {
		List<Groupe> liste=new ArrayList<Groupe>();
		for (Groupe groupe : getMap().values())
			liste.add(groupe);
		return liste;
	}
}

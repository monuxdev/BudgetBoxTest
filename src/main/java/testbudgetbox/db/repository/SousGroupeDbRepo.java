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
import testbudgetbox.commons.SousGroupe;
import testbudgetbox.db.Connexion;
import testbudgetbox.db.Queries;

public class SousGroupeDbRepo implements Repository<SousGroupe> {

	private Connexion connexion;
	private GroupeDbRepo groupeRepo;
	private Map<Integer, SousGroupe> mapSousGroupe=null;

	public SousGroupeDbRepo(Connexion connexion, GroupeDbRepo groupeRepo){
		this.connexion=connexion;
		this.groupeRepo=groupeRepo;
	}

	@Override
	public SousGroupe get(int id) throws SQLException{
		SousGroupe groupe = getMap().get(id);
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
	public SousGroupe add(SousGroupe sousgroupe) throws SQLException{
		int id=getNewId();
		sousgroupe.setId(getNewId());
		// on modifie la map avant la connexion
		getMap().put(id, sousgroupe);
		Connection conn = connexion.getConnexion();
		String query = Queries.ADD_SOUS_GROUPE.toString();
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setInt(1, sousgroupe.getId());
		stmt.setString(2, sousgroupe.getNom());
		stmt.setInt(3, sousgroupe.getGroupe().getId());
		stmt.execute();
		conn.commit();
		return get(id);
	}

	@Override
	public Map<Integer, SousGroupe> getMap() throws SQLException{
		if (mapSousGroupe==null) {
			mapSousGroupe=new HashMap<Integer, SousGroupe>();
			refresh();
		}
		return mapSousGroupe;
	}

	private void refresh() throws SQLException{
		Connection conn = connexion.getConnexion();
		String query = Queries.LISTE_SOUS_GROUPES.toString();
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		if (rs!=null && !rs.isClosed()) {
			// on vide la map uniquement si on peut se connecter
			mapSousGroupe.clear();
			while (rs.next()) {
				int id=rs.getInt("id");
				String nom=rs.getString("nom");
				Groupe groupe = groupeRepo.get(rs.getInt("groupe_id"));
				SousGroupe sousGroupe = new SousGroupe(id, groupe, nom);
				mapSousGroupe.put(id, sousGroupe);
			}
			rs.close();
		}
	}


	@Override
	public boolean del(SousGroupe sousgroupe) throws Exception {
		Connection conn = connexion.getConnexion();
		String query = Queries.DEL_SOUS_GROUPE.toString();
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setInt(1, sousgroupe.getId());
		stmt.execute();
		conn.commit();
		getMap().remove(sousgroupe.getId());
		return true;
	}

	@Override
	public SousGroupe update(SousGroupe sousgroupe) throws Exception {
		Connection conn = connexion.getConnexion();
		String query = Queries.UPDATE_SOUS_GROUPE.toString();
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, sousgroupe.getNom());
		stmt.setInt(2, sousgroupe.getGroupe().getId());
		stmt.setInt(4, sousgroupe.getId());
		stmt.execute();
		conn.commit();
		getMap().put(sousgroupe.getId(), sousgroupe);
		return sousgroupe;
	}

	@Override
	public List<SousGroupe> getAll() throws Exception {
		List<SousGroupe> liste=new ArrayList<SousGroupe>();
		for (SousGroupe sousgroupe : getMap().values())
			liste.add(sousgroupe);
		return liste;
	}

}

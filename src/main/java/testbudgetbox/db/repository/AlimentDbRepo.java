package testbudgetbox.db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import testbudgetbox.commons.Aliment;
import testbudgetbox.commons.Repository;
import testbudgetbox.commons.SousGroupe;
import testbudgetbox.db.Connexion;
import testbudgetbox.db.Queries;

public class AlimentDbRepo implements Repository<Aliment>{

	private Connexion connexion;
	private SousGroupeDbRepo sousgroupeRepo;
	private Map<Integer, Aliment> mapAliment=null;

	public AlimentDbRepo(Connexion connexion, SousGroupeDbRepo sousgroupeRepo) {
		this.connexion=connexion;
		this.sousgroupeRepo=sousgroupeRepo;
	}

	@Override
	public Aliment get(int id) throws SQLException{
		Aliment aliment = getMap().get(id);
		return aliment;
	}

	@Override
	public Aliment add(Aliment aliment) throws SQLException{
		int id=getNewId();
		aliment.setId(getNewId());
		// on modifie la map avant la connexion
		getMap().put(id, aliment);
		Connection conn = connexion.getConnexion();
		String query = Queries.ADD_ALIMENT.toString();
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setInt(1, aliment.getId());
		stmt.setString(2, aliment.getNom());
		stmt.setString(3, aliment.getNomScient());
		stmt.setInt(4, aliment.getSousGroupe().getId());
		stmt.execute();
		conn.commit();
		refresh();
		return get(id);
	}

	@Override
	public Map<Integer, Aliment> getMap() throws SQLException{
		if (mapAliment==null) {
			mapAliment=new HashMap<Integer, Aliment>();
			refresh();
		}
		return mapAliment;
	}


	@Override
	public List<Aliment> getAll() throws SQLException{
		List<Aliment> listeAlim = new ArrayList<Aliment>();
		for (Aliment aliment : getMap().values()) {
			listeAlim.add(aliment);
		}
		return listeAlim;
	}

	@Override
	public boolean del(Aliment aliment) throws SQLException {
		Connection conn = connexion.getConnexion();
		String query = Queries.DEL_ALIMENT.toString();
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setInt(1, aliment.getId());
		stmt.execute();
		conn.commit();
		refresh();
		return true;
	}

	@Override
	public Aliment update(Aliment aliment) throws SQLException {
		Connection conn = connexion.getConnexion();
		String query = Queries.UPDATE_ALIMENT.toString();
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, aliment.getNom());
		stmt.setString(2, aliment.getNomScient());
		stmt.setInt(3, aliment.getSousGroupe().getId());
		stmt.setInt(4, aliment.getId());
		stmt.execute();
		conn.commit();
		refresh();
		return aliment;
	}

	private void refresh() throws SQLException{
		Connection conn = connexion.getConnexion();
		String query = Queries.LISTE_ALIMENTS.toString();
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		if (rs!=null && !rs.isClosed()) {
			// on vide la map uniquement si on peut se connecter
			mapAliment.clear();
			while (rs.next()) {
				int id=rs.getInt("id");
				String nom=rs.getString("nom");
				String nomScient=rs.getString("nom_sc");
				SousGroupe sousGroupe = sousgroupeRepo.get(rs.getInt("sousgroupe_id"));
				Aliment aliment = new Aliment(id, nom, nomScient, sousGroupe);
				mapAliment.put(id, aliment);
			}
			rs.close();
		}
	}

	private int getNewId() throws SQLException{
		int newId=1;
		for (int key : getMap().keySet()) {
			if (newId<=key)
				newId=key+1;
		}
		return newId;
	}
	
}

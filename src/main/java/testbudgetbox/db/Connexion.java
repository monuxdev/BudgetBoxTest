package testbudgetbox.db;

import java.sql.Connection;

public interface Connexion {
	public Connection getConnexion();
	public void closeConnexion();
}

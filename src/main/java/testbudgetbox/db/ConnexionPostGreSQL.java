package testbudgetbox.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import testbudgetbox.BootStrap;

public class ConnexionPostGreSQL implements Connexion{
	
	private static final Logger LOGGER = Logger.getLogger(BootStrap.class.getSimpleName());
	private static final String driverKey="jdbc.drivers";

	private Connection connexion;

	public ConnexionPostGreSQL(Properties prop) {
		TypeBase type = TypeBase.postgresql;
		String serveurName = prop.getProperty("SERVEUR");
		String dbName = prop.getProperty("DB");
		String login = prop.getProperty("LOGIN");
		String pwd = prop.getProperty("PASSWORD");
		String driver = type.getDriverString();
		addDriverProperty(driver);
		try {
			//Class.forName(base.getDriverString());
			DriverManager.setLoginTimeout(5);
			Connection conn = DriverManager.getConnection(
				"jdbc:" + type.getTypeJdbc() + "://" + serveurName + "/" + dbName, login, pwd);
			connexion = conn;
			conn.setAutoCommit(false);
			LOGGER.fine("Connexion "+type.toString()+" créée");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,"Création connexion "+type.toString(), e);
		}
	}
	
	private static void addDriverProperty(String driver) {
		Properties sysProps=System.getProperties();
		String jdbcDrivers = sysProps.getProperty(driverKey);
		if (jdbcDrivers==null)
			jdbcDrivers="";
		if (jdbcDrivers.length()==0)
			jdbcDrivers=driver;
		else if (!jdbcDrivers.contains(driver)) {
			jdbcDrivers += driver;
		}
		sysProps.setProperty(driverKey, jdbcDrivers);
	}

	@Override
	public Connection getConnexion() {
		return this.connexion;
	}

	@Override
	public void closeConnexion() {
		try {
			getConnexion().close();
		}
		catch(Exception E) {
			E.printStackTrace();
		}
	}
}

package testbudgetbox.db;

public enum TypeBase {
	postgresql("PG_", "org.postgresql.Driver", "postgresql"),
	firebird("OR_", "org.firebirdsql.jdbc.FBDriver", "firebirdsql"), 
	sqlserver("SS_", "net.sourceforge.jtds.jdbc.Driver", "jtds:sqlserver");

	private final String prefixe;
	private final String driverString;
	private final String typeJdbc;

	private TypeBase(String prefixe, String driver, String jdbc) {
		this.prefixe = prefixe;
		this.driverString = driver;
		this.typeJdbc = jdbc;
	}

	public String getDriverString() {
		return this.driverString;
	}

	public String getPrefixe() {
		return this.prefixe;
	}

	public String getTypeJdbc() {
		return this.typeJdbc;
	}
}

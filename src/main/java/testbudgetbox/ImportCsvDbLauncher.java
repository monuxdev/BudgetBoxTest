package testbudgetbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import testbudgetbox.db.Connexion;
import testbudgetbox.db.ConnexionPostGreSQL;
import testbudgetbox.db.repository.AlimentDbRepo;
import testbudgetbox.db.repository.GroupeDbRepo;
import testbudgetbox.db.repository.SousGroupeDbRepo;
import testbudgetbox.services.AlimentService;
import testbudgetbox.services.GroupeService;
import testbudgetbox.services.SousGroupeService;

public class ImportCsvDbLauncher {
	private static Properties properties=null;

	public static void main(String[] args) {
		// recupération des paramètres :
		initProperties();
		// On pourrait en fonction des paramètres se brancher sur plusieurs types de base :
		Connexion connexion = new ConnexionPostGreSQL(properties);

		// initialisation des services :
		GroupeDbRepo groupeRepo = new GroupeDbRepo(connexion);
		GroupeService groupeService = new GroupeService(groupeRepo);
		SousGroupeDbRepo sousGroupeRepo = new SousGroupeDbRepo(connexion, groupeRepo);
		SousGroupeService sousGroupeService = new SousGroupeService(sousGroupeRepo);
		AlimentDbRepo alimentRepo = new AlimentDbRepo(connexion, sousGroupeRepo);
		AlimentService alimentService = new AlimentService(alimentRepo);

		// localisation du fichier csv (devrait être dans un autre fichier de configuration) :
		String fileName=properties.getProperty("CSV");
		
		new ImportCsv(groupeService, sousGroupeService, alimentService).traitement(fileName);
		connexion.closeConnexion();
		System.exit(0);
	}

	public static void initProperties() {
		if (properties == null) {
			properties = new Properties();
			FileInputStream inputStream = null;
			try {
				inputStream = new FileInputStream(getPropertiesFile());
				properties.load(inputStream);
				if (properties.isEmpty()) {
					inputStream.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private static File getPropertiesFile() throws IOException {
		File file = new File("db.conf");
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}
}

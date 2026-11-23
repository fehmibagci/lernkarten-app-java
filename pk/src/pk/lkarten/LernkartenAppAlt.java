package pk.lkarten;

import java.sql.Connection;
import pk.lkarten.db.Database;
import pk.lkarten.db.LernkartenDao;

public class LernkartenAppAlt {

	public static void main(String[] args) {

		// try-with-resources um Connection automatisch zu schliessen
		try (Database db = Database.getInstance()) {
			// 1. Verbindung zur Datenbank aufbauen
			Connection connection = db.getConnection();

			// 2.DAO initilisiern
			LernkartenDao dao = new LernkartenDao(connection);

			// 3. Lernkartei initialisieren
			Lernkartei kartei = new Lernkartei(dao);

			// 4. Menue starten
			Menue menue = new Menue(kartei);
			menue.starteMenue();

		} catch (Exception e) {
			System.out.println("Fehler beim Start der Anwendung:");
			e.printStackTrace();
		}
	}
}

//Aktueller Stand: P08 fertig
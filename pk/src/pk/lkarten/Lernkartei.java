package pk.lkarten;

import java.sql.SQLException;
import java.util.List;
import pk.lkarten.db.LernkartenDao;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Lernkartei {
	// DAO für den Zugriff auf die Datenbank
	private LernkartenDao dao;

	// Konstruktor: DAO wird von außen übergeben
	public Lernkartei(LernkartenDao dao) {
		this.dao = dao;
	}

	public int hinzufuegen(Lernkarte karte) throws UngueltigeKarteException, SQLException, DoppelteKarteException {
		karte.validiere();

		// Prueft ob es sich um eine Einzelantwortkarte handelt
		if (karte instanceof EinzelantwortKarte e) {
			return dao.createEinzelantwortKarte(e); // Speichert die Karte in der Datenbank
			// Prueft ob es sich um eine Mehrfachantwortkarte handelt
		} else if (karte instanceof MehrfachantwortKarte m) {
			return dao.createMehrfachantwortKarte(m);// Speichert die Karte in der Datenbank
		}
		return -1; //Falls beides Fehlschlaegt
	}

	public void druckeAlleKarten() throws SQLException {
		List<Lernkarte> karten = dao.findAll();

		// prueft ob keine Karten vorhanden sind, wenn leer, Meldung ausgeben
		if (karten.isEmpty()) {
			System.out.println("Keine Karteikarten vorhanden");
			return;
		}

		// gibt jede Lernkarte aus (Vorder- und Rueckseite)
		for (Lernkarte k : karten) {
			k.druckeKarte();
		}
	}

	public void exportiereEintraegeAlsCsv(Path datei) throws IOException, SQLException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(datei.toFile())))) {
            writer.write("ID,Kategorie,Titel,Frage,Antwort(en),Richtige Antwort(en)");
            writer.newLine();

            for (Lernkarte k : dao.findAll()) {
                writer.write(k.exportiereAlsCsv());
                writer.newLine();
            }
        }
    }
	
	// gibt die ANzahl der Lernkarten direkt aus der Datenbank zurueck
	public int gibAnzahlKarten() throws SQLException {
		return dao.countLernkarten();
	}

	public Lernkarte[] gibKartenZuKategorie(String kategorie) throws SQLException {
		List<Lernkarte> karten = dao.findByKategorie(kategorie);// Liest alle Lernkarten mit der gegebenen Kategorie aus
																// der DB
		return karten.toArray(new Lernkarte[0]); // Wandelt die Liste in ein Array um
	}

	public Lernkarte[] erzeugeDeck(int anzahlKarten) throws SQLException {
		// Alle Lernkarten aus der Datenbannk laden
		List<Lernkarte> karten = dao.findAll();

		// Prueft ob ueberhaupt Karten vorhanden sind, wenn nicht, leeres Array
		// zurueckgeben
		if (karten.isEmpty()) {
			return new Lernkarte[0];
		}

		int n = Math.min(anzahlKarten, karten.size()); // Bestimmt die Deckgroesse -> maximal anzahlKarten aber
														// hoechstens soviele Karten wie exisitieren
		Lernkarte[] deck = new Lernkarte[n]; // Array fuer das Deck mit genau n Karten

		// befuellt das Deck mit zufaellig ausgewaehlten Karten
		for (int i = 0; i < deck.length; i++) {
			int i2 = (int) (Math.random() * karten.size()); // Zufaelliger Index im Bereich der vorhandenen Karten
			deck[i] = karten.get(i2); // Zufaellige Karte an Pos i im Deck ablegen
		}

		return deck; // das erzeuugte Deck zurueckgeben
	}
	
	// Nicht in der Aufgabenstellung aber besser um das erzeugte (Random) Deck
	// ausgeben zu koennen
	public void druckeDeckAlleKarten(Lernkarte[] deck) {
		// Erweiterte for-Schleife
		for (Lernkarte karte : deck) {
			karte.druckeKarte();
			System.out.println();
		}
	}

	public Lernkarte[] gibAlleKarten() throws SQLException{
		List<Lernkarte> temp = dao.findAll();
		return temp.toArray(new Lernkarte[temp.size()]);
	}
	
}

package pk.lkarten;

import java.util.Objects;
import java.util.Random;

public abstract class Lernkarte implements Comparable<Lernkarte>, ValidierbareKarte, CsvExportable {

	private int id;
	protected String kategorie;
	protected String titel;
	protected String frage;

	// Konstruktor ohne ID
	public Lernkarte(String kategorie, String titel, String frage) {
		this.kategorie = kategorie;
		this.titel = titel;
		this.frage = frage;
	}

	// Konstruktor mit ID
	public Lernkarte(int id, String kategorie, String titel, String frage) {
		this.id = id;
		this.kategorie = kategorie;
		this.titel = titel;
		this.frage = frage;
	}

	public void zeigeVorderseite() {
		System.out.println("[" + id + ", " + kategorie + "] " + titel + ":");
		System.out.println(frage);
	}

	public abstract void zeigeRueckseite(); // abstract weil die Methode in EinzelantwortKarte und MehrfachantwortKarte
											// unterschiedlich ist

	//Hilfsmethode
	protected String front() {
		return "[" + id + ", " + kategorie + "] " + titel + ":\n" + frage + "\n";
	}

	public abstract String gibVorderseite();

	public abstract String gibRueckseite();

	public void druckeKarte() {
		zeigeVorderseite();
		// System.out.println("\n");
		zeigeRueckseite();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKategorie() { // Getter fuer die Kategorie fuer Aufg. 1.4
		return kategorie;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getFrage() {
		return frage;
	}

	public void setFrage(String frage) {
		this.frage = frage;
	}

	@Override
	public String exportiereAlsCsv() {
		return id + "," + kategorie + "," + titel + "," + frage;
	}

	@Override
	public int hashCode() {
		return Objects.hash(frage, kategorie, titel);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null) // if(!super.equals(obj)) return false; ??
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lernkarte other = (Lernkarte) obj;
		return Objects.equals(frage, other.frage) && Objects.equals(kategorie, other.kategorie)
				&& Objects.equals(titel, other.titel);
	}

	@Override
	public int compareTo(Lernkarte other) {
		return Integer.compare(other.getId(), this.getId()); // absteigend
	}

	@Override
	public void validiere() throws UngueltigeKarteException {
		// Pruefen ob Kategorie null, leer oder nur Leerzeichen ist
		if (kategorie == null || kategorie.isBlank()) {
			throw new UngueltigeKarteException("Kategorie darf nicht leer/null sein");
		}
		// Pruefen ob Titel null, leer oder nur Leerzeichen ist
		if (titel == null || titel.isBlank()) {
			throw new UngueltigeKarteException("Titel darf nicht leer/null sein");
		}
		// Pruefen ob Frage null, leer oder nur Leerzeichen ist
		if (frage == null || frage.isBlank()) {
			throw new UngueltigeKarteException("Frage darf nicht leer/null sein");
		}

	}

	@Override
	public String toString() {
		return "ID: " + getId() + ", Kategorie: " + getKategorie() + ", Titel: " + getTitel() + ", Frage: "
				+ getFrage();
	}

}

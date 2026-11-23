package pk.lkarten;

import java.util.Objects;

public class EinzelantwortKarte extends Lernkarte {

	private String antwort;

	//Konstruktor OHNE ID
	public EinzelantwortKarte(String kategorie, String titel, String frage, String antwort) {
		super(kategorie, titel, frage); // Attribute des Konstruktors der Oberklasse aufrufen
		this.antwort = antwort;
	}
	
	//Konstruktor MIT ID
		public EinzelantwortKarte(int id, String kategorie, String titel, String frage, String antwort) {
			super(id, kategorie, titel, frage);
			this.antwort = antwort;
		}

	public void zeigeRueckseite() {
		System.out.println("\t" + antwort);
	}
	
	

	public String getAntwort() {
		return antwort;
	}

	@Override
	public String exportiereAlsCsv() {
		return super.exportiereAlsCsv() + ";" + antwort;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(antwort);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EinzelantwortKarte other = (EinzelantwortKarte) obj;
		return Objects.equals(antwort, other.antwort);
	}

	@Override
	public void validiere() throws UngueltigeKarteException {
		super.validiere(); // hiermit werden die Basispruefungen durchgefuehrt (Kategorie, Titel, Frage)
		if (antwort == null || antwort.isBlank()) {
			throw new UngueltigeKarteException("Antwort darf nicht leer/null sein");
		}
	}

	@Override
	public String toString() {
		return "Einzelantwortkarte [" + super.toString() + ", Antwort: " + getAntwort() + "]\n";
	}

	@Override
	public String gibVorderseite() {
		return front();
	}

	@Override
	public String gibRueckseite() {
		return antwort;
	}

	
	
}

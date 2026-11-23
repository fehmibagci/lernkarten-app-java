package pk.lkarten;

import java.util.Arrays;

public class MehrfachantwortKarte extends Lernkarte {
	private String[] moeglicheAntworten;
	private int[] richtigeAntworten;

	// Konstruktor OHNE ID
	public MehrfachantwortKarte(String kategorie, String titel, String frage, String[] moeglicheAntworten,
			int[] richtigeAntworten) {
		super(kategorie, titel, frage);
		this.moeglicheAntworten = moeglicheAntworten;
		this.richtigeAntworten = richtigeAntworten;
	}

	// Konstruktor MIT ID
	public MehrfachantwortKarte(int id, String kategorie, String titel, String frage, String[] moeglicheAntworten,
			int[] richtigeAntworten) {
		super(id, kategorie, titel, frage);
		this.moeglicheAntworten = moeglicheAntworten;
		this.richtigeAntworten = richtigeAntworten;
	}

	public void zeigeRueckseite() {
		System.out.println("\nRichtige Antwort/en:");
		for (int i = 0; i < richtigeAntworten.length; i++) {
			int index = richtigeAntworten[i];
			String antwortText = moeglicheAntworten[index - 1];
			System.out.println(index + ": " + antwortText);
		}
	}

	public String[] getMoeglicheAntworten() {
		return moeglicheAntworten;
	}

	public void setMoeglicheAntworten(String[] moeglicheAntworten) {
		this.moeglicheAntworten = moeglicheAntworten;
	}

	public int[] getRichtigeAntworten() {
		return richtigeAntworten;
	}

	public void setRichtigeAntworten(int[] richtigeAntworten) {
		this.richtigeAntworten = richtigeAntworten;
	}

	@Override
	public String exportiereAlsCsv() {
		return super.exportiereAlsCsv() // id, Titel, Kategorie und Frage
				+ ";" + Arrays.toString(moeglicheAntworten) + ";" + Arrays.toString(richtigeAntworten);
	}

	@Override
	public void zeigeVorderseite() {
		super.zeigeVorderseite();
		for (int i = 0; i < moeglicheAntworten.length; i++) {
			System.out.println((i + 1) + ": " + moeglicheAntworten[i]);
		}
	}

	//Hilfsklasse fuer GUI in LernkartenApp 
	public String vorderseiteFuerGUI() {
        String text = frage + "\n";

        for (int i = 0; i < moeglicheAntworten.length; i++) {
            text += (i+1) + ":\t" + moeglicheAntworten[i] + "\n\n";
        }

        if (richtigeAntworten.length > 1) {
            text += "\n(mehrere Antworten möglich)\n";
        } else {
            text += "\n";
        }

        return text;
    }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(moeglicheAntworten);
		result = prime * result + Arrays.hashCode(richtigeAntworten);
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
		MehrfachantwortKarte other = (MehrfachantwortKarte) obj;
		return Arrays.equals(moeglicheAntworten, other.moeglicheAntworten)
				&& Arrays.equals(richtigeAntworten, other.richtigeAntworten);
	}

	@Override
	public void validiere() throws UngueltigeKarteException {
		super.validiere(); // hiermit werden die Basispruefungen durchgefuehrt (Kategorie, Titel, Frage)

		if (moeglicheAntworten == null || moeglicheAntworten.length < 2) { // Prueft, ob das Array moeglicheAntworten
																			// existiert und mindestens zwei Antworten
																			// enthaelt
			throw new UngueltigeKarteException("Weniger als 2 Antwortmoeglichkeiten vorhanden.");
		}
		// JEde Antwort auf null, leer oder nur Leerzeichen pruefen
		for (int i = 0; i < moeglicheAntworten.length; i++) {
			if (moeglicheAntworten[i] == null || moeglicheAntworten[i].isBlank()) {
				throw new UngueltigeKarteException("Antwortmoeglichkeiten duerfen nicht leer sein.");
			}
		}

		if (richtigeAntworten == null || richtigeAntworten.length == 0) { // Prueft, ob das Array richtigeAntworten
																			// existiert und mindestens eine richtige
																			// Antwort enthaelt
			System.out.println("Geklappt");									
			throw new UngueltigeKarteException("Mindestens eine richtige Antwort muss angegeben werden.");
		}

		for (int i = 0; i < richtigeAntworten.length; i++) {
			int index = richtigeAntworten[i];
			if (index < 1) {
				throw new UngueltigeKarteException(
						"Antwort falsch angegeben. (Antwortindex liegt ausserhalb des gueltigen Bereichs.)");
			}

			if (index > moeglicheAntworten.length) {
				throw new UngueltigeKarteException("Antwortindex liegt außerhalb des gueltigen Bereichs.");
			}
		}

	}

	@Override
	public String toString() {
		return "Mehrfachantwortkarte [" + super.toString() + ", Moegliche Antworten: "
				+ Arrays.toString(moeglicheAntworten) + ", Richtige Antworten: " + Arrays.toString(richtigeAntworten);
	}

	@Override
	public String gibVorderseite() {
		String txt = front();

		for (int i = 0; i < moeglicheAntworten.length; i++) {
			txt += (i + 1) + ".\t" + moeglicheAntworten[i] + "\n";
		}

		if (richtigeAntworten.length > 1) {
			txt += "\n(mehrere Antworten möglich)\n";
		} else {
			txt += "\n";
		}

		return txt;
	}

	@Override
	public String gibRueckseite() {
		if (richtigeAntworten.length == 1) {
			return "Die richtigen Antwort ist:\n\t" + moeglicheAntworten[richtigeAntworten[0]] + "\n";
		} else {
			String text = "Die richtigen Antworten sind:\n";

			for (int i = 0; i < richtigeAntworten.length; i++) {
				text += (i + 1) + ".\t" + moeglicheAntworten[richtigeAntworten[i]] + "\n";
			}
			return text;
		}
	}
}



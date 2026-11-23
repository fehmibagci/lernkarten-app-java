package pk.lkarten;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.List;
import java.util.HashSet;

public class LernkarteiSet {

	// Implementierung mit Liste
	// ArrayList<Lernkarte> liste = new ArrayList<Lernkarte>();

	// Implementierung mit HashSet
	Set<Lernkarte> karten = new HashSet<>();

	// Konstruktor
	public LernkarteiSet() {
	}

	// hinzufuegen einer Karte in die Liste mittels .add
	public void hinzufuegen(Lernkarte karte) throws UngueltigeKarteException {
		karte.validiere(); // wirft UngueltigeKarteException wenn etwas an der Karte falsch ist
		boolean hinzugefuegt = karten.add(karte); // add() gibt false zurueck wenn ein Objekt identisch/doppelt ist,
													// wenn true, wird es ausgefuehrt
		if (hinzugefuegt == false) {
			System.out.println("Karte existiert bereits!");
		}
	}

	public void druckeAlleKarten() {

		if (karten.isEmpty()) {
			System.out.println("Keine Karteikarten vorhanden");
			return;
		}

		// HashSet -> ArratList
		ArrayList<Lernkarte> sortiert = new ArrayList<>(karten);

		// mit Java-API sortieren
		Collections.sort(sortiert);

		// sortiert ausgeben
		for (Lernkarte karte : sortiert) {
			karte.druckeKarte();
		}
	}

	// Mittels .size() die Laenge der Liste ermitteln und zurueckgeben
	public int gibAnzahlKarten() {
		return karten.size();
	}

	public Lernkarte[] gibKartenZuKategorie(String kategorie) {
		ArrayList<Lernkarte> treffer = new ArrayList<>(); // temporaere Liste fuer Treffer

		for (Lernkarte k : karten) {
			if (k.getKategorie().equals(kategorie)) {
				treffer.add(k);
			}
		}

		return treffer.toArray(new Lernkarte[0]);
	}

	public Lernkarte[] erzeugeDeck(int anzahlKarten) {
		Random random = new Random(); // Random Objekt erzeugen bevor man es benutzen kann, aehnlich wie beim Scanner
		Lernkarte[] deck = new Lernkarte[anzahlKarten]; // Array anlegen fuer die Random Karten

		List<Lernkarte> tempList = new ArrayList<>(karten);

		for (int i = 0; i < anzahlKarten; i++) {
			int index = random.nextInt(tempList.size());
			deck[i] = tempList.get(index);
		}

		return deck;
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

	void exportiereEintraegeAlsCsv(Path datei) throws IOException {

		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(datei)))) {

			bw.write("ID,Kategorie,Titel,Frage,Antwort(en),Richtige Antwort(en)");
			bw.newLine();

			for (Lernkarte k : karten) {
				bw.write(k.exportiereAlsCsv());
				bw.newLine();
			}
		}
	}

	// Kopie
	void exportiereEintraegeAlsCsvNio(Path datei) throws IOException { // Exception nicht hier behandeln sondern
																		// weiterwerfen
		// Erste Zeile
		String csv = "ID,Kategorie,Titel,Frage,Antwort(en),Richtige Antwort(en)\n";

		// Ueber Karten iterieren und fuer jede Karte eine neue Zeile
		for (Lernkarte Lernkarte : karten) {
			csv += Lernkarte.exportiereAlsCsv() + "\n";
		}

		Files.writeString(datei, csv);

	}

	@Override
	public int hashCode() {
		return Objects.hash(karten);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LernkarteiSet other = (LernkarteiSet) obj;
		return Objects.equals(karten, other.karten);
	}

}

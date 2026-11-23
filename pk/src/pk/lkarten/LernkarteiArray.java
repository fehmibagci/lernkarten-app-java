package pk.lkarten;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class LernkarteiArray {
	private Lernkarte[] karten; // Assoziation Lernkarte
	private int anzahl; // anzahl Lernkarten

		

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(karten);
		result = prime * result + Objects.hash(anzahl);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LernkarteiArray other = (LernkarteiArray) obj;
		return anzahl == other.anzahl && Arrays.equals(karten, other.karten);
	}

	// Konstruktor
	public LernkarteiArray(int maxKarten) { // maximale Anzahl an Karten im Parameter uebergeben
		karten = new Lernkarte[maxKarten];
		anzahl = 0;
	}

	public void hinzufuegen(Lernkarte karte) {
		// Fehlermeldung ausgeben wenn Stapel voll ist
		if (anzahl >= karten.length) {
			System.out.println("Fehler: Stapel ist voll, Lernkarte wurde nicht hinzugefuegt");
			return; // return um Methode zu beenden
		}

		karten[anzahl] = karte; // Neue Karte an die naechste freie Postition setzen
		anzahl++; // Zaehler erhoehen
	}

	public void druckeAlleKarten() {
		// Meldung rausgeben wenn keine Karten vorhanden
		if (anzahl == 0) {
			System.out.println("Keine Karteikarten vorhanden");
			return; // return um Methode zu beenden
		}

		for (int i = 0; i < anzahl; i++) {
			karten[i].druckeKarte(); // Methode druckeKarte() aus Lernkarte aufrufen
			System.out.println(); // Leerzeile
		}
	}

	public int gibAnzahlKarten() {
		return anzahl;
	}

	public Lernkarte[] gibKartenZuKategorie(String kategorie) {
		int counter = 0; // Counter fuer Anzahl der Uebereinstimmungen

		// Zaehlen wieiviele Karten zur Kategorie passen
		for (int i = 0; i < anzahl; i++) {
			if (karten[i].getKategorie().equals(kategorie)) { // Pruefen ob Kartenkategorie == gesuchte Kategorie
				counter++;
			}
		}

		// Wenn keine Karte passt, leeres Array zurueckgeben
		if (counter == 0) {
			return new Lernkarte[0];
		}

		Lernkarte[] erg = new Lernkarte[counter];
		int j = 0;

		// Uebereinstimmende Karten in das neue Array "erg" hineinkopieren
		for (int i = 0; i < anzahl; i++) {
			if (karten[i].getKategorie().equals(kategorie)) { // Pruefen ob Kartenkategorie == gesuchte Kategorie
				erg[j] = karten[i]; // Die erste Uebereinstimmung in das neue Array erg kopieren
				j++; // Index des neuen Arrays erg erhoehen
			}
		}
		return erg;
	}

	public Lernkarte[] erzeugeDeck(int anzahlKarten) {
		Lernkarte[] deck = new Lernkarte[anzahlKarten]; // Array anlegen fuer die Random Karten
		Random random = new Random(); // Random Objekt erzeugen bevor man es benutzen kann, aehnlich wie beim Scanner

		// Schleife um deck mit random Karten aus karten zu fuellen
		for (int i = 0; i < anzahlKarten; i++) {
			int zufall = random.nextInt(anzahl); // In der Schleife deklarieren damit der Wert immer ein anderer ist
			deck[i] = karten[zufall];
		}

		return deck;
	}

	// Nicht in der Aufgabenstellung aber besser um das erzeugte Random Deck
	// ausgeben zu koennen
	public void druckeDeckAlleKarten(Lernkarte[] deck) {
		for (int i = 0; i < deck.length; i++) {
			deck[i].druckeKarte(); // Vorder und Rueckseite ausgeben
			System.out.println(); // Leerzeile
		}
	}
}

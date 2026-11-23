package pk.lkarten;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Menue {

	// Assoziation zu Lernkartei
	private Lernkartei kartei;
	// Scanner anlegen (fuer die Benutzerangabe)
	Scanner sc = new Scanner(System.in);

	// Konstruktor
	public Menue(Lernkartei kartei) {
		this.kartei = kartei;
	}

	// Methode um das Auswahlmenue auszudrucken
	public void druckeMenue() {
		System.out.println("Lernkarten-Manager\n");
		System.out.println("1. Lernen!");
		System.out.println("2. Einzelantwortkarte hinzufuegen");
		System.out.println("3. Drucke alle Karten");
		System.out.println("4. Drucke Karten zu Kategorie");
		System.out.println("5. Beenden");
		System.out.print("\nBitte Aktion waehlen: ");
	}

	private void warteAufEnter() {
		try {
			System.in.read(new byte[2]);
		} catch (IOException e) {
			System.err.println("Fehler: " + e.getMessage());
		}
	}

	public void starteMenue() {
		int eingabe = 0;
		do {
			druckeMenue();

			// Ganze Zeile lesen und in einen int parsen
			String eingabeString = sc.nextLine();

			// Fehlerbehandlung bei ungueltiger Zahl
			try {
				eingabe = Integer.parseInt(eingabeString); // Pruefen ob was ungueltiges eingegeben wird
			} catch (Exception e) { // Falls ja, Fehler fangen, Fehlermeldung ausgeben und Menue nochmal ausgeben
				System.out.println("Ungueltige Zahl eingegeben"); // Fehler ausgeben
				continue; // Menue nochmal ausgeben
			}
			// eingabe = Integer.parseInt(eingabeString);

			switch (eingabe) {
			// Lernen!
			case 1: {
				lernen();
				break;
			}
			// Einzelantwortkarte hinzufuegen
			case 2: {
				einzelkarteHinzufuegen();
				break;
			}
			// Drucke alle Karten
			case 3: {
				try {
					kartei.druckeAlleKarten();
					System.out.println();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Datenbankfehler:\n" + e.getMessage(), "Fehler",
							JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
			// Drucke Karten zu Kategorie
			case 4: {
				druckeKartenZuKategorie();
				break;
			}
			case 5: {
				System.out.println("Tschuess!");
				break;
			}
			default:
				System.out.println("Falsche Eingabe");
			}
		} while (eingabe != 5);

	}

	// Methode fuer den 1. case
	private void lernen() {
		try {
			int anzahl = kartei.gibAnzahlKarten();

			// Falls keine Karten vorhanden Fehlermeldung ausgeben
			if (anzahl == 0) {
				System.out.println("Es sind keine Karten zum Lernen vorhanden.\n");
				return;
			}
			// Variable Deckgroesse
			Lernkarte[] deck = kartei.erzeugeDeck(Math.min(5, anzahl)); // Deck der groesse 5 erzeugen

			// for loop um Deck zu durchlaufen
			for (int i = 0; i < deck.length; i++) {
				deck[i].zeigeVorderseite(); // Vorderseite der aktuellen Karte anzeigen
				System.out.println("<Drücken Sie Enter, um die Rückseite der Karte zu sehen.>");
				warteAufEnter();
				deck[i].zeigeRueckseite(); // Rueckseite anzeigen

				// Nur die naechste Karte anbieten wenn ueberhaupt noch eine weitere existiert
				if (i < deck.length - 1) {
					System.out.println("<Drücken Sie Enter, um die nächste Karte zu sehen.>");
					warteAufEnter();
				}
			}
			System.out.println("<Alle Karten betrachtet.>\n");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Datenbankfehler:\n" + e.getMessage(), "Fehler",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void einzelkarteHinzufuegen() {
		// Kategorie, Titel, Frage und Antwort des Benutzers einlesen
		String kategorie = JOptionPane.showInputDialog(null, "Bitte geben Sie eine Kategorie ein: ");
		// JOptionPane.showMessageDialog(null, "Kategorie ausgewaehlt");
		String titel = JOptionPane.showInputDialog("Bitte geben Sie den Titel ein:");
		String frage = JOptionPane.showInputDialog("Bitte geben Sie die Frage ein");
		String antwort = JOptionPane.showInputDialog("Bitte geben Sie die Antwort ein");

		try {
			kartei.hinzufuegen(new EinzelantwortKarte(kategorie, titel, frage, antwort)); // Neue Einzelanwortkarte mit
																							// den von Benutzer
																							// eingegebenen Daten
																							// erstellen
			System.out.println("Einzelantwortkarte erfolgreich hinzugefuegt"); // Erfolgsbenachrichtigung
		} catch (UngueltigeKarteException | DoppelteKarteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Datenbankfehler:\n" + e.getMessage(), "Fehler",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	private void druckeKartenZuKategorie() {
		String kategorie = JOptionPane.showInputDialog("Gebe die Kategorie ein deren Karten angezeigt werden sollen: ");
		try {
			Lernkarte[] kartenArray = kartei.gibKartenZuKategorie(kategorie); // Neues Array fuer die Karten zu
																				// Kategorie

			// Pruefen ob Karten zur Kategorie gefunden wurden, wenn ja, kartenArray drucken
			if (kartenArray.length == 0) {
				System.out.println("Keine Karten gefunden.\n");
			} else {
				kartei.druckeDeckAlleKarten(kartenArray);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Datenbankfehler:\n" + e.getMessage(), "Fehler",
					JOptionPane.ERROR_MESSAGE);
		}

	}
}

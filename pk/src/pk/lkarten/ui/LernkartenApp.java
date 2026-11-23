package pk.lkarten.ui;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pk.lkarten.EinzelantwortKarte;
import pk.lkarten.Lernkarte;
import pk.lkarten.Lernkartei;
import pk.lkarten.LernkarteiSet;
import pk.lkarten.MehrfachantwortKarte;
import pk.lkarten.Menue;
import pk.lkarten.db.Database;
import pk.lkarten.db.LernkartenDao;
import pk.lkarten.ui.util.DialogUtil;

public class LernkartenApp extends Application {

	private Lernkartei kartei;

	protected ListView<String> kartenListe = new ListView<>();

	private void aktualisiereKartenListe() {
		try {
			kartenListe.getItems().clear();
			Lernkarte[] alleKarten = kartei.gibAlleKarten();
			for (Lernkarte k : alleKarten) {
				kartenListe.getItems().add(k.toString());
			}
		} catch (SQLException e) {
			DialogUtil.showErrorDialog("DB-Fehler", "Fehler beim laden der Liste");
		}
	}

	public void start(Stage primaryStage) {

		try {
			Connection conn = Database.getInstance().getConnection(); // Verbindung zur DB gherstellen

			LernkartenDao dao = new LernkartenDao(conn);
			this.kartei = new Lernkartei(dao);

			aktualisiereKartenListe();

			// ---Hauptfenster---

			// MenuBar und Menüs vorbereiten
			MenuBar menuBar = new MenuBar();
			Menu menuDatei = new Menu("Datei");
			Menu menuLernkartei = new Menu("Lernkartei");

			// Einträge für "Datei"
			MenuItem itemExport = new MenuItem("CSV-Export");
			MenuItem itemBeenden = new MenuItem("Beenden");
			menuDatei.getItems().addAll(itemExport, itemBeenden);

			// Einträge für "Lernkartei"
			MenuItem itemEinzel = new MenuItem("Einzelantwortkarte hinzufügen");
			MenuItem itemMehrfach = new MenuItem("Mehrfachantwortkarte hinzufügen");
			menuLernkartei.getItems().addAll(itemEinzel, itemMehrfach);

			Button lernen = new Button("Lernen!");
			Spinner spinner = new Spinner(1, 100, 1); // Anfang, Ende, Standardeinstellnungen

			// ab hier kommen die Ereignisbehandlung Dinge rein

			// Datei -> CSV-Export
			itemExport.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					String dateiname = "";
					Path path = null;

					while (true) {
						dateiname = DialogUtil.showInputDialog("CSV-Export", "Bitte geben Sie einen Dateinamen an:");

						if (dateiname == null || dateiname.trim().isEmpty()) {
							DialogUtil.showMessageDialog("Vorgang Abgebrochen!",
									"Falsche Angabe, geben sie bitte einen richtigen Namen ein.");
							return;
						}

						dateiname = dateiname.trim();
						if (!dateiname.toLowerCase().endsWith(".csv")) {
							dateiname += ".csv";
						}

						path = Paths.get(dateiname);

						// Existenzprüfung und Überschreib-Abfrage über DialogUtil
						if (Files.exists(path)) {
							// showConfirmDialog liefert true bei "Ja"
							boolean überschreiben = DialogUtil.showConfirmDialog("Datei überschreiben?",
									"Die Datei '" + dateiname + "' existiert bereits. Möchten Sie sie überschreiben?");

							if (!überschreiben) {
								continue; // Zurück zur Namenseingabe
							}
						}
						// Export
						try {
							kartei.exportiereEintraegeAlsCsv(path);
							// Optionale Erfolgsmeldung
							DialogUtil.showMessageDialog("Erfolg!", "Der Export wurde erfolgreich abgeschlossen.");
							break;
						} catch (Exception e) {
							// Fehlerbehandlung über DialogUtil
							DialogUtil.showErrorDialog("Export-Fehler",
									"Der Export ist fehlgeschlagen: " + e.getMessage());
							break;
						}
					}
				}
			});

			// Datei -> Beenden
			itemBeenden.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					DialogUtil.showMessageDialog("Programm wurde beendet", "Tschuess");
					Platform.exit(); // PrimaryStage schliessen
					stop(); // DB Verbindung schliessen
				}
			});

			// Lernkartei -> Mehrfach
			itemMehrfach.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					MehrfachantwortKarteErfassungView mV = new MehrfachantwortKarteErfassungView(primaryStage, null,
							kartei);
					mV.showView();
					aktualisiereKartenListe();
				}
			});

			// Lernkartei -> Einzel
			itemEinzel.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					EinzelantwortKarteErfassungView ev = new EinzelantwortKarteErfassungView(primaryStage, null,
							kartei);
					ev.showView();
					aktualisiereKartenListe();
				}
			});

			lernen.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					try {
						Lernkarte[] temp = kartei.erzeugeDeck((int) spinner.getValue());
						for (int i = 0; i < temp.length; i++) {
							if (temp[i] instanceof EinzelantwortKarte) {
								// Vorderseite
								DialogUtil.showTextDialog(temp[i].getKategorie(), temp[i].getTitel(),
										temp[i].getFrage(), "Rueckseite zeigen");
								// Rueckseite
								DialogUtil.showTextDialog(temp[i].getKategorie(), temp[i].getTitel(),
										((EinzelantwortKarte) temp[i]).getAntwort(), "Naechste Karte zeigen");

							} else {
								MehrfachantwortKarte tempKarte = ((MehrfachantwortKarte) temp[i]);
								//Vorderseite
								DialogUtil.showTextDialog(temp[i].getKategorie(), temp[i].getTitel(),
										tempKarte.vorderseiteFuerGUI(), "Rueckseite zeigen");
								// Rueckseite
								DialogUtil.showTextDialog(temp[i].getKategorie(), temp[i].getTitel(),
										tempKarte.gibRueckseite(), "Naechste Karte zeigen");
							}
						}

					} catch (SQLException e) {
						DialogUtil.showErrorDialog("DB-Fehler", "Deck konnte nicht erzeugt werden");
					}
				}
			});

			// Menüs der Bar hinzufügen
			menuBar.getMenus().addAll(menuDatei, menuLernkartei);

			// Hauptfenster
			VBox root = new VBox();
			HBox bottom = new HBox(5);

			bottom.getChildren().addAll(lernen, spinner);
			bottom.setAlignment(Pos.CENTER);

			root.getChildren().addAll(menuBar, kartenListe, bottom);

			Scene scene = new Scene(root, 600, 400);

			primaryStage.setTitle("Lernkarten-App");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (SQLException e) {
			DialogUtil.showErrorDialog("Datenbankfehler",
					"Verbindung konnte nicht aufgebaut werden: " + e.getMessage());
			e.printStackTrace();
		}

		/*
		 * >>>!!!<<< Standardmaessig ist Testfall 1 aktiv. Zum testen der anderen Faelle
		 * bitte einfach alles ausser denn gewuenschtren Testfall auskommentieren
		 */

		// Leeres Fenster als Hauptfenster oeffnen
		// primaryStage.setTitle("Lernkarten-App");
		// primaryStage.show();

		// Testfall 1: Öffnen des EinzelantwortKarteErfassungView für ein valides
		// EinzelantwortKarte-Objekt
		// var karte = new EinzelantwortKarte("Konstruktoren", "Objektorientierung",
		// "Welchem Zweck dient ein Konstruktor?",
		// "Ein Konstruktor ist eine spezielle Methode zur Initialisierung von
		// Objekten.");

		// var view = new EinzelantwortKarteErfassungView(primaryStage, karte);

		// Testfall 2: Öffnen des EinzelantwortKarteErfassungView für ein valides
		// EinzelantwortKarte-Objekt/ null
		// new EinzelantwortKarteErfassungView(primaryStage, null).showView();

		// Testfall 3: Öffnen des MehrfachantwortKarteErfassungView für ein valides
		// MehrfachantwortKarte -Objekt mit > 5 möglichen Antworten
		/*
		 * var karte = new MehrfachantwortKarte("Java Grundlagen", "Datentyp int",
		 * "Welche Aussagen zu int stimmen?",
		 * 
		 * new String[] { "int speichert ganze Zahlen",
		 * "int kann Nachkommastellen speichern", "int ist 32 Bit groß",
		 * "int kann negativ sein", "int speichert Zeichen", "int belegt 1 Byte" }, //
		 * richtige Antworten: 1, 3, 4 new int[] { 1, 3, 4 });
		 * 
		 * var view = new MehrfachantwortKarteErfassungView(primaryStage, karte);
		 */
		// Testfall 4: Öffnen des MehrfachantwortKarteErfassungView für ein valides
		// MehrfachantwortKarte -Objekt mit < 5 möglichen Antworten
		/*
		 * var karte = new MehrfachantwortKarte("Geographie", "Kontinente",
		 * "Welche der folgenden sind Kontinente?",
		 * 
		 * new String[] { "Europa", "Atlantik", "Afrika", "Asien" },
		 * 
		 * // richtige Antworten: 1, 3, 4 new int[] { 1, 3, 4 });
		 * 
		 * var view = new MehrfachantwortKarteErfassungView(primaryStage, karte);
		 */

		// Testfall 5: Öffnen des MehrfachantwortKarteErfassungView für ein leeres
		// MehrfachantwortKarte -Objekt/ null
		// var karte = (MehrfachantwortKarte) null;
		// var view = new MehrfachantwortKarteErfassungView(primaryStage, karte);

		// view.showView(); // Bleibt immer fuer jeden Faall
	}

	@Override
	public void stop() {
		try {
			Database.getInstance().close();
		} catch (SQLException exp) {
			DialogUtil.showErrorDialog("Datenbankfehler",
					"Verbindung konnte nicht geschlossn werden: " + exp.getMessage());
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
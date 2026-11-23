package pk.lkarten.ui;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pk.lkarten.DoppelteKarteException;
import pk.lkarten.Lernkartei;
import pk.lkarten.MehrfachantwortKarte;
import pk.lkarten.UngueltigeKarteException;
import pk.lkarten.ui.util.DialogUtil;

public class MehrfachantwortKarteErfassungView extends ErfassungView {

	// Eingabefelder oben
	private final TextField kategorie = new TextField();
	private final TextField titel = new TextField();
	private final TextField frage = new TextField();

	// Bis zu 5 Antwortboxen und "Richtig"-Checklboxen
	private final TextArea[] antwortFelder = new TextArea[5];
	private final CheckBox[] richtigCheckbox = new CheckBox[5];

	public MehrfachantwortKarteErfassungView(Stage owner, MehrfachantwortKarte karte, Lernkartei lernkartei) {
		super(owner, karte, lernkartei);

		// Titel des Fensters bestimmen
		setTitle("Erfassung einer Mehrfachantwortkarte");

		// Grid
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10));
		grid.setHgap(5);
		grid.setVgap(5);

		// kopfzeile mit Kategorie, Titel und Frage
		grid.add(new Label("Kategorie:"), 0, 0); // Spalte 0, Zeile 0
		grid.add(kategorie, 1, 0, 2, 1); // Spalte 1, Zeile 0 - Spaltenlaenge 2, Zeilenhoehe 1

		grid.add(new Label("Titel:"), 0, 1);// Spalte 0, Zeile 1
		grid.add(titel, 1, 1, 2, 1); // Spalte 1, Zeile 1 - Spaltenlaenge 2, Zeilenhoehe 1

		grid.add(new Label("Frage:"), 0, 2);// Spalte 0, Zeile 2
		grid.add(frage, 1, 2, 2, 1); // Spalte 1, Zeile 2 - Spaltenlaenge 2, Zeilenhoehe 1

		// Antworten 1-5
		for (int i = 0; i < antwortFelder.length; i++) {
			int row = 3 + i; // Erste Antwot in Zeile 3, dann in 4,5,6,7
			antwortFelder[i] = new TextArea();
			antwortFelder[i].setPrefRowCount(2);

			richtigCheckbox[i] = new CheckBox("Richtig?");

			grid.add(new Label("Antwort " + (i + 1) + ":"), 0, row); // Label(Antwort) 1-5 Links
			grid.add(antwortFelder[i], 1, row); // TextArea in der Mitte
			grid.add(richtigCheckbox[i], 2, row); // "Richtig"-Checkboxen rechts
		}

		// Buttons unten
		Button buttonOk = new Button("OK");
		Button buttonAbbrechen = new Button("Abbrechen");

		// Horizontal Box um Buttons nebeneinander anzuzeigen, Abstand 10px
		HBox buttonBox = new HBox(10);
		// einzelne add-Aufrufe statt addAll(...)
		buttonBox.getChildren().add(buttonOk);
		buttonBox.getChildren().add(buttonAbbrechen);

		buttonBox.setAlignment(Pos.CENTER);

		// Alles in eine vertikale Box, Grid oben, Buttons unten
		VBox root = new VBox(10, grid, buttonBox);
		root.setPadding(new Insets(10));

		// Ok Button
		buttonOk.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				try {
					// Daten aus den Textfeldern in die Karte übertragen!
					ArrayList<String> tempAnt = new ArrayList();
					ArrayList<Integer> tempRichtigAnt = new ArrayList();

					for (int i = 0; i < 5; i++) {
						if (!antwortFelder[i].getText().isBlank()) {
							tempAnt.add(antwortFelder[i].getText());

							if (richtigCheckbox[i].isSelected())
								tempRichtigAnt.add(tempAnt.size() - 1);
						}
					}

					String[] moeglicheAntwortenArray = new String[tempAnt.size()];
					int[] richtigeAntwortenArray = new int[tempRichtigAnt.size()];

					for (int i = 0; i < tempAnt.size(); i++)
						moeglicheAntwortenArray[i] = tempAnt.get(i);
					for (int i = 0; i < tempRichtigAnt.size(); i++)
						richtigeAntwortenArray[i] = tempRichtigAnt.get(i);

					lernkartei.hinzufuegen(new MehrfachantwortKarte(0, kategorie.getText(), titel.getText(),
							frage.getText(), moeglicheAntwortenArray, richtigeAntwortenArray));

					DialogUtil.showMessageDialog("Erfolg", "Karte wurde gespeichert.");
					close(); // Fenster nur bei Erfolg schließen

				} catch (UngueltigeKarteException e) {
					DialogUtil.showErrorDialog("Validierungsfehler", e.getMessage());
					// Fenster bleibt offen, damit der Nutzer korrigieren kann
				} catch (DoppelteKarteException e) {
					DialogUtil.showErrorDialog("Karte doppelt", e.getMessage());
				} catch (SQLException e) {
					DialogUtil.showErrorDialog("DB-Fehler", "Fehler: " + e.getMessage());
				}

			}
		});

		// Abbrech Button
		buttonAbbrechen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				close(); // Schliesst das Unterfenster
			}
		});

		Scene scene = new Scene(root);
		setScene(scene);

		// Falls eine bestehende Karte bearbeitet werden soll: Felder vorbelegen
		if (karte != null) {
			kategorie.setText(karte.getKategorie());
			titel.setText(karte.getTitel());
			frage.setText(karte.getFrage());

			String[] moegliche = karte.getMoeglicheAntworten();
			int[] richtige = karte.getRichtigeAntworten();

			for (int i = 0; i < antwortFelder.length; i++) {
				if (moegliche != null && i < moegliche.length) {
					antwortFelder[i].setText(moegliche[i]);
				} else {
					antwortFelder[i].clear();
				}

				boolean istRichtig = richtige != null && enthaeltIndex(richtige, i + 1);
				richtigCheckbox[i].setSelected(istRichtig);
			}
		}

	}

	// Prueft, ob ein Index in der Liste der richtigen Antworten vorkommt
	private boolean enthaeltIndex(int[] indices, int gesucht) {
		if (indices == null)
			return false;
		for (int i : indices) {
			if (i == gesucht)
				return true;
		}
		return false;
	}

	@Override
	public void showView() {
		this.showAndWait();
	}

}

package pk.lkarten.ui;

import java.sql.SQLException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pk.lkarten.DoppelteKarteException;
import pk.lkarten.EinzelantwortKarte;
import pk.lkarten.Lernkartei;
import pk.lkarten.UngueltigeKarteException;
import pk.lkarten.ui.util.DialogUtil;

public class EinzelantwortKarteErfassungView extends ErfassungView {

	// TetxFields erzeugen
	private final TextField kategorie = new TextField();
	private final TextField titel = new TextField();
	private final TextField frage = new TextField();
	private final TextArea antwort = new TextArea(); // TextArea anstelle von TextField fuer ein groesseres Feld

	public EinzelantwortKarteErfassungView(Stage owner, EinzelantwortKarte karte, Lernkartei lernkartei) {
		super(owner, karte, lernkartei);

		// Titel des Fensters bestimmen
		setTitle("Erfassung einer Einzelantwortkarte");

		// Grid
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10));
		grid.setHgap(5);
		grid.setVgap(5);

		// grid.add(node, columnIndex, rowIndex) <node bspw. Textfield, Button, Label,
		// ...>

		grid.add(new Label("Kategorie:"), 0, 0); // Spalte 0, Zeile 0
		grid.add(kategorie, 1, 0); // Spalte 1, Zeile 0

		grid.add(new Label("Titel:"), 0, 1); // Spalte 0, Zeile 1
		grid.add(titel, 1, 1); // Spalte 1, Zeile 1

		grid.add(new Label("Frage:"), 0, 2);
		grid.add(frage, 1, 2);

		grid.add(new Label("Antwort:"), 0, 3);
		antwort.setPrefRowCount(4); // Ueber wieviele Zeilen sich das Textfeld strecken soll
		grid.add(antwort, 1, 3);

		// Buttons unten
		Button buttonOk = new Button("OK"); // OK Button
		Button buttonAbbrechen = new Button("Abbrechen"); // Abbbrechen Button

		// Horizontal Box um Buttons nebeneinander anzuzeigen
		HBox buttonBox = new HBox(10); // 10px Abstaand
		buttonBox.getChildren().addAll(buttonOk, buttonAbbrechen);
		buttonBox.setAlignment(Pos.CENTER);

		VBox root = new VBox(10, grid, buttonBox);
		root.setPadding(new Insets(10));

		// Ereignisbehandlung Dinge
		//OK Button
		buttonOk.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					lernkartei.hinzufuegen(new EinzelantwortKarte(0, kategorie.getText(), titel.getText(),
							frage.getText(), antwort.getText()));
					
					//Um nachdem man ok gedrueckt hat das Fenster zu schliessen
					DialogUtil.showMessageDialog("Karte erfolgreich hinzugefuegt", "Super!");
					close();
				} catch (UngueltigeKarteException e) {
					DialogUtil.showErrorDialog("Ungueltige Karte!", e.getMessage());
				} catch (SQLException e) {
					DialogUtil.showErrorDialog("DB-Fehler", e.getMessage());
				} catch (DoppelteKarteException e) {
					DialogUtil.showErrorDialog("KArte doppelt!", e.getMessage());
				}
			}
		}); 

		//Abbrech Button
		buttonAbbrechen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				close(); //Schliesst das Unterfenster
			}
		});
		
		Scene scene = new Scene(root);
		setScene(scene);
		this.showAndWait();

		// Felder belegen falls eine KArte beaerbeitet werden soll die schon besteht
		if (karte != null) {
			kategorie.setText(karte.getKategorie());
			titel.setText(karte.getTitel());
			frage.setText(karte.getFrage());
			antwort.setText(karte.getAntwort());
		}
	}

	@Override
	public void showView() {
		this.showAndWait();
	}
}

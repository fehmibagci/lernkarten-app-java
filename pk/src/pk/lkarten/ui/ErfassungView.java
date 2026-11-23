package pk.lkarten.ui;

import java.util.Objects;

import javafx.stage.Modality;
import javafx.stage.Stage;
import pk.lkarten.Lernkarte;
import pk.lkarten.Lernkartei;

public abstract class ErfassungView extends Stage {
	private Lernkarte karte;
	protected Lernkartei lernkartei;

	public abstract void showView();

	public ErfassungView(Stage owner, Lernkarte karte, Lernkartei lernkartei) {
		super();
		this.karte = karte;
		this.lernkartei = lernkartei;
		// Um die Stage modal zu macen
		this.initOwner(owner);
		this.initModality(Modality.WINDOW_MODAL);

	}

	public Lernkarte getKarte() {
		return karte;
	}

	public void setKarte(Lernkarte karte) {
		this.karte = karte;
	}

	@Override
	public int hashCode() {
		return Objects.hash(karte);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ErfassungView other = (ErfassungView) obj;
		return Objects.equals(karte, other.karte);
	}
}

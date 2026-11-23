package pk;

//Kommentar hinzugefuegt
public class FirstProgramm {

	public static void main(String[] args) {
		maleTreppe(6, 2); // erst hoehe, dann tiefe eingeben
	}

		public static void maleTreppe(int hoehe, int stufentiefe) {
			int maximaleBreite = hoehe * stufentiefe;

			for (int i = 1; i <= hoehe; i++) {
				int aktuelleBreite = i * stufentiefe;
				int anzahlLeerzeichen = (maximaleBreite - aktuelleBreite);
				
				for (int j = 0; j < anzahlLeerzeichen; j++) {
					System.out.print(" ");
				}
				for (int k = 0; k < aktuelleBreite; k++) {
					System.out.print("*");
				}

				System.out.println();
			}
		}
} 
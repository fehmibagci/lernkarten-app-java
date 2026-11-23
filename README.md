Lernkarten App (Java)
Eine Desktop-App zum Erstellen und Verwalten von Lernkarten. Entstanden im Programmierpraktikum an der FH Dortmund.

Funktionen:
- Lernkarten mit einer Antwort (Einzelantwort) oder mehreren Antworten (Mehrfachantwort) erfassen
- Karten in einer SQLite-Datenbank speichern und wieder laden
- Karten als CSV-Datei exportieren
- Eingaben prüfen und bei Fehlern eine Meldung anzeigen

Technick
- Java mit JavaFX für die Oberfläche
- SQLite über JDBC (Zugriff über eine DAO-Klasse mit PreparedStatements)
- Objektorientierter Aufbau: abstrakte Klasse `Lernkarte` mit Unterklassen, Interfaces und eigene Exceptions
- Entwickelt in Eclipse, Zusammenarbeit über Git

Hinweis
Das Projekt haben wir zu zweit umgesetzt. Ein Teil des Grundgerüsts (z. B. die Datenbankverbindung) war von der Hochschule vorgegeben. Die Implementierung der Funktionen habe ich umgesetzt.

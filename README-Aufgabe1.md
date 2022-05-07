# Zuordnungsaufgabe Teil 1

In dieser Woche beginnen wir mit der Implementation eines Tools zur Zuordnung von Funden in prähistorischen Orten mittels Paläoproteomik.

Damit verständlich ist, was eigentlich mit diesem Projekt bezweckt wird, beginnen wir mit einer (sehr vereinfachten - das Modul heißt ja Programmierung und nicht Proteomik) Beschreibung des Problems. Eine grundlegende Einarbeitung in eine neue Fachlichkeit ist grundsätzlich neben der Programmierung auch ein wichtiger Bestandteil der Arbeit in der Informatik: Nur, wenn man die Fragestellung und die Sprache der späteren Anwender_innen kennt, ist man in der Lage, nützliche Software zu schreiben. Wo immer Sie mal arbeiten werden: Wenn Sie Ihren Job gut machen wollen, werden Sie nicht drum herum kommen, auch ein wenig Expertise in dem Gebiet zu werden, für welches Sie Software entwickeln.


## Fachlicher Hintergrund

Die Publikation, welche diese Aufgabe inspiriert hat, finden Sie [hier](https://www.mpg.de/10731379/palaeoproteomik-neandertaler).

Menschen und Neanderthaler teilten sich über längere Zeit hinweg den selben Lebensraum. Für Paläontologen macht die dies eine Zuordnung von Funden (wie beispielsweise Höhlenmalereien) zu Menschen, Neanderthalern oder anderen Humanoiden herausfordernd.

Eine Herangehensweise an dieses Problem ist die Paläoproteomik. Durch Mutationen veärndern sich die Proteine, welche vom Körper hergestellt werden. Ist eine solche Mutation vorteilhaft, so kann sie über die Generationen hinweg zum "Standard" in einer Population werden (der Organismus, in dem dies Mutation aufgetreten ist, erzeugt mehr Nachwuchs, der auch diese Mutation hat und sich entsprechend auch stärker vermehrt - so dass nach vielen Generationen alle Organismen Nachfahren dieses ersten Organismus mit der Mutation sind und somit auch die Mutation tragen). Eine Anhäufung von solchen Mutationen in unterschiedlichen Populationen (in der Regel im Zusammenhang mit einer räumlichen Trennung dieser Populationen, so dass sich die Genome nicht vermischen können) kann über die Zeit hinweg dazu führen, dass die Organismen der beiden Populationen sich so stark voneinander unterscheiden, dass sie keinen gemeinsamen Nachwuchs mehr erzeugen können. Dies wird als Spezies-Bildung bezeichnet und ist der Grund für die biologische Vielfalt auf der Erde.

Entsprechend ist es möglich, Unterschiede zwischen den Genomen auch recht eng miteinander verwandter Organismen - wie in diesem Fall Neanderthaler und Meschen - zu identifizieren. Dabei kann auch eine Sorte Score berechnet werden, der aussagt, wie sicher dsa Vorhandensein eines bestimmten Unterschieds die Identifikation der Spezies zulässt. Beispielsweise könnte es Mutationen geben, die ausschließlich im Genom der Nanderthaler vorhanden sind, und andere, welche primär im Neanderthaler-Genom vorkommen, aber auch im menschlichen Genom vorkommen könnten.

Auf Basis dieser Informationen ist es möglich, durch die Untersuchung von Proteinresten in paläontologischen Funden und den Vergleich der ermittelten Proteinsequenzen mit Listen bekannter Sequenzen zu ermitteln, welcher Spezies die Funde zuzuordnen sind.

In dieser und den folgenden Aufgaben werden Sie eine Software schreiben, welche auf Basis von Proteinsequenzen einer Fundstätte die Wahrscheinlichkeit bestimmt, dass diese Menschen, Neanderthalern oder Schimpansen (stellen Sie sich vor, es soll die Hypothese untersucht werden, ob auch Schimpansen an einigen Orten primitive Zivilisationen entwickelt haben) zugeordnet werden kann.

## Ausgangsdaten

In diesem Projekt verwenden Sie Referenz-Proteinsequenzen aus dem humanen Genom von der [NCBI proteome database](https://www.ncbi.nlm.nih.gov/protein/) sowie simulierte Fundstätten-Daten.

### Referenz-Sequenz und FASTA-Format

Um die bekannten Mutationen zu beschreiben, werden diese als Abweichungen von einer sogenannten Referenzsequenz beschrieben. Hierbei handelt es sich um die Abfolge von Aminosäuren in dem Protein (ein Protein ist im Endeffekt nur eine Kette von Aminosäuren genannten Bausteinen), welches von dem betroffenen Genomabschnitt kodiert wird. Der Einfachheit halber überspringen wir hier die dreckigen Details der Übersetzung von Peptiddaten aus einer Proteinsequenzierung zu vollständigen Aminosäuresequenzen und arbeiten direkt mit der letzteren.

Aminosäuresequenzen werden in sogenannten FASTA-Dateien gespeichert. Diese sind wie folgt aufgebaut:

```text
> Sequenzname 1
PQVTLWRRPIV
LLDTYADDTVL
...
> Sequenzname 2
TIKIGGQLKEA
EEMSLPGKWKP
...
...
> Sequenzname n
SEQUENZ_N
```

Jede Sequenz beginnt mit einem Header: Einer Zeile, deren erstes Zeichen ein ```>``` ist und von dem Sequenznamen gefolgt ist. Danach kommen beliebig viele (aber mindestens eine) Zeilen mit der Aminosäuresequenz, wobei jeder Buchstabe eine der 20 möglichen Aminosäuren entsprechend des [IUPAC Amino Acid Code](http://bioinformatics.org/sms2/iupac.html) darstellt.

Es können in einer FASTA-Datei beliebig viele Sequenzen hintereinander vorkommen. Wichtig ist zu beachten, dass es keine Kommentarzeichen o.Ä. gibt - jede Zeile ist entweder ein Sequenzname oder eine Sequenz.

Die Referenzsequenzen für die 5 in dieser Aufgabe verwendeten Proteine sind in den entsprechenden fasta-Dateien zu finden, für GREB1 beispielsweise in  [data/HumanProteins.fasta](data/GREB1_reference.fasta).

### Mutationsmuster

Mutationsmuster werden als Abweichung(en) von der Referenzsequenz beschrieben. Dabei wird die Position in der Referenzsequenz sowie die dort zu findende Aminosäure angegeben. Lautet die Referenzsequenz beispielsweise:

```PQVTLWQRPI```

so ergibt sich beispielsweise aus dem Mutationsmuster ```3P``` (an der dritten Position ist abweichend von der Referenzsequenz die Aminosäure P, also Prolin) die Sequenz:

```PQPTLWQRPI```

Ein Mutationsmuster kann auch mehrere Mutationen enthalten. Bezogen auf die oben genannte Referenzsequenz würde beispielsweise das Mutationsmuster ```2L,5Q``` die folgende Sequenz beschreiben:

```PLVTQWQRPI```

In dieser Aufgabe ist die Referenz die Sequenz im menschlichen Genom, das Mutationsmuster beschreibt den im Neanderthal-Genom vorhandenen Austausch. Das, wie "eindeutig" ein Mutationsmuster für die Zuordnung zum Neanderthaler-Genom ist, wird durch den Grantham-Score (GS-Score) beschrieben. Der Einfachheit halber nehmen wir für diese Aufgabe an, dass die Wahrscheinlichkeit, sich bei einer Zuordnung zu irren, wie folgt zu berechnen ist:

<img src="https://render.githubusercontent.com/render/math?math=$P_{err}=10^{-\frac{GS}{10}}$">

Wird also eine Mutation mit einem GS-Score von 180 gefunden, kann man mit einer nur 1.6%-igen Fehlerwahrscheinlichkeit sagen, dass es sich um ein Neanderthal-Genom handelt:

<img src="https://render.githubusercontent.com/render/math?math=P_{err}=10^{-\frac{180}{10}}=10^{-1.8}=0.015849"> 

Hat man Hinweise von mehreren Proteinen, kann die Gesamtwahrscheinlichkeit sich zu irren durch eine Multiplikation der Einzelfehlerwahrscheinlichkeiten berechnet werden. Gibt es also eine Mutation mit einem GS-Score von 180 und eine mit einem GS-Score von 100, dann ist die gesamte Fehlerwahrscheinlichkeit nur noch ca. 0.16%:

<img src="https://render.githubusercontent.com/render/math?math=P_{err}=10^{-\frac{180}{10}}*10^{-\frac{100}{10}}=10^{-1.8}*10^{-1}=0.001585">

Die Zuordnung bekannter Mutationsmuster in den vorgegebenen Referenzsequenzen zu ihren GS-Scores sind für jede Referenzsequenz in den entsprechenden csv-Dateien hinterlegt, so beispielsweise für GREB1 in [data/GREB1_patterns.csv](data/GREB1_patterns.csv)) hinterlegt. Dabei beschreibt jede Zeile ein Mutationsmuster. In der ersten Spalte ist das Mutationsmuster selber genannt. In den darauffolgenden Spalten sind die GS-Scores für unterschiedliche Spezies genannt - welche, lässt sich aus dem Header entnehmen. In der ersten Zeile der Beispieldatei ist also die Mutation ein Austausch der Referenzaminosäure zu C an Position 1164 des Gens GREB1 beschrieben, und diese hat einen GS-Score 180 für eine Zuordnung zum Neanderthaler, von 8 für eine Zuordnung zum Schimpansen und von 70 für eine Zuordnung zum Menschen:

|Mutation Pattern|Neanderthal|Chimp|Human|
|---|---|---|---|---|
|1164C|180|8|70|


### Simulierte Fundstätten-Daten

In der Datei [data/site_sequences.fasta](data/site_sequences.fasta) sind simulierte Sequenzen von GREB1 von einer Fundstätte im FASTA-Format abgespeichert. Diese sind in dieser Woche noch nicht relevant. Perspektivisch sollen in diesem Projekt diese Sequenzen mit den Mutationsmustern abgeglichen werden, um wie oben beschrieben die Wahrscheinlichkeit einer Zuordenbarkeit zum Neanderthaler zu bestimmen.

## Programmieraufgabe

In dieser Aufgabe legen Sie den Grundstein für ein Tool, welches die oben genannten Daten analysiert. In der ersten Ausbaustufe bauen wir ein Tool, das nur eine Referenzsequenz verwendet (also nur von einem Protein die Sequenzen anschaut), später erweitern wir die Funktionalität dann auf mehrere Referenzsequenzen.

### FASTA-Datei parsen

Implementieren Sie eine Klasse SeqFile, die sich um das Einlesen und die Verwaltung von FASTA-Dateien kümmert. Diese sollte folgende Methoden haben:

* ```public SeqFile(String filename)```: Liest die FASTA-Datei ein.
  ```public boolean isValid()```: Gibt ```true``` zurück, wenn die FASTA-Datei erfolgreich eingelesen wurde, sonst (z.B. weil die Datei nicht existiert oder das Format falsch ist) ```false```
* ```public int getNumberOfSequences()```: Gibt die Anzahl der eingelesenen Sequenzen zurück (0, falls die Datei nicht eingelesen werden konnte)
* ```public HashSet<String> getSequences()```: Gibt die Sequenzen als HashSet zurück (leers HashSet, falls die Datei nicht eingelesen werden konnte)
* ```public String getFirstSequence()```: Utility-Methode, um einfach an die erste (im Fall der Referenzsequenz: einzige) Sequenz zu kommen. Gibt die erste Sequenz zurück (leerer String, falls die Datei nicht eingelesen werden konnte)

### Optionen parsen

Implementieren Sie die Methode ```public static CommandLine parseOptions(String[] args)``` in der Klasse ```SiteClassification```. Dabei sollen die folgenden Optionen unterstützt werden:

* -m, --mutationfiles: Pfad zu CSV-Datei mit Mutationspattern. Muss angegeben werden.
* -r, --references: Pfad zu FASTA-Datei mit der Referenzsequenz. Muss angegeben werden.
* -p, --proteinseqs: Pfad zu FASTA-Datei mit Patientensequenzen. Muss angegeben werden.

Die Methode soll ```null``` zurückgeben, falls eine der erforderlichen Optionen nicht übergeben wurde.
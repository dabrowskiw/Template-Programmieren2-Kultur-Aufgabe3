package org.htw.prog2.aufgabe1;
import org.apache.commons.cli.*;
import org.htw.prog2.aufgabe1.files.MutationFile;
import org.htw.prog2.aufgabe1.files.SequenceFile;
import org.htw.prog2.aufgabe1.readers.*;

public class SiteClassification {

    /**
     * Parst die Kommandozeilenargumente. Gibt null zurück, falls:
     * <ul>
     *     <li>Ein Fehler beim Parsen aufgetreten ist (z.B. eins der erforderlichen Argumente nicht angegeben wurde)</li>
     *     <li>Bei -m, -d und -r nicht die gleiche Anzahl an Argumenten angegeben wurde</li>
     * </ul>
     * @param args Array mit Kommandozeilen-Argumenten
     * @return CommandLine-Objekt mit geparsten Optionen
     */

    public static CommandLine parseOptions(String[] args) {
        Options options = new Options();
        options.addOption(Option.builder("m").
                hasArg(true).
                numberOfArgs(Option.UNLIMITED_VALUES).
                longOpt("mutationfiles").
                required(true).
                desc("CSV files with mutation patterns, separated with commas.").build());
        options.addOption(Option.builder("r").
                hasArg(true).
                numberOfArgs(Option.UNLIMITED_VALUES).
                longOpt("references").
                required(true).
                desc("Reference sequence FASTA files, separated with commas.").build());
        options.addOption(Option.builder("p").
                hasArg(true).
                longOpt("proteinseqs").
                required(true).
                desc("FASTA file with site protein sequences.").build());
        CommandLineParser parser = new DefaultParser();
        CommandLine cli;
        try {
            cli = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Error: " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("SiteClassification", options);
            return null;
        }
        return cli;
    }

    public static void main(String[] args) {
        CommandLine cli = parseOptions(args);
        if(cli == null ){
            System.exit(1);
        }
        String referenceFileName = cli.getOptionValue('r');
        String siteSeqFileName = cli.getOptionValue('p');
        String patternsFileName = cli.getOptionValue('m');

        // Add all available sequence file readers to sequenceReaderManager:
        ReaderManager<SequenceFileReader> sequenceReaderManager = new ReaderManager<>();
        sequenceReaderManager.addReader(new FASTAFileReader());
        sequenceReaderManager.addReader(new FASTQFileReader());

        // Add all available mutation file readers to mutationReaderManager:
        ReaderManager<MutationFileReader> mutationReaderManager = new ReaderManager<>();
        mutationReaderManager.addReader(new CSVFileReader());

        try {
            // Use reader managers to determine appropriate readers for all input files, and then use those
            // readers to read the files
            SequenceFile referencefile = sequenceReaderManager.getReaderForFile(referenceFileName).readFile(referenceFileName);
            SequenceFile siteseqs = sequenceReaderManager.getReaderForFile(siteSeqFileName).readFile(siteSeqFileName);
            MutationFile patterns = mutationReaderManager.getReaderForFile(patternsFileName).readFile(patternsFileName);
            System.out.println("Eingelesene Mutationen: " + patterns.getNumberOfMutations());
            System.out.println("Länge der eingelesenen Referenzsequenz: " +
                    referencefile.getFirstSequence().length() + " Aminosäuren");
            System.out.println("Anzahl der eingelesenen Proteinsequenzen: " +
                    siteseqs.getNumberOfSequences());
        } catch(Exception e) {
            System.out.println("Fehler beim Einlesen einer der Dateien: " + e.getMessage());
        }
    }
}

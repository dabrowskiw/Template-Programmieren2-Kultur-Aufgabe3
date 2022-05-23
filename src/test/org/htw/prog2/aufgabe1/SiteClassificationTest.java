package org.htw.prog2.aufgabe1;

import org.apache.commons.cli.CommandLine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class SiteClassificationTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }


    @Test
    void parseOptions_requiredArguments() {
        assertNull(SiteClassification.parseOptions(new String[] {}));
        assertNull(SiteClassification.parseOptions(
                "-m data/GREB1_patterns.csv -r data/GREB1_reference.fasta".split(" ")));
        assertNotNull(SiteClassification.parseOptions(
                "-m data/GREB1_patterns.csv -p data/site_sequences.fasta -r data/GREB1_reference.fasta".
                        split(" ")));
    }

    @Test
    void parseOptions_argumentValues() {
        CommandLine cli = SiteClassification.parseOptions(
                "-m data/GREB1_patterns.csv -p data/site_sequences.fasta -r data/GREB1_reference.fasta ".
                        split(" "));
        assertEquals("data/GREB1_patterns.csv", cli.getOptionValue('m'));
        assertEquals("data/site_sequences.fasta", cli.getOptionValue('p'));
        assertEquals("data/GREB1_reference.fasta", cli.getOptionValue('r'));
    }

    @Test
    void main() {
        SiteClassification.main("-m data/GREB1_patterns.csv -p data/site_sequences.fasta -r data/GREB1_reference.fasta".split(" "));
        assertEquals(new String[] {"Eingelesene Mutationen: 4",
                "Länge der eingelesenen Referenzsequenz: 1949 Aminosäuren",
                "Anzahl der eingelesenen Proteinsequenzen: 382"}, outContent.toString().strip().replace("\\r", "").split("\\n"));
    }

}
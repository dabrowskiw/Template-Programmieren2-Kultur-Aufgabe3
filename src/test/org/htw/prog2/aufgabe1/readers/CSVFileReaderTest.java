package org.htw.prog2.aufgabe1.readers;

import org.htw.prog2.aufgabe1.exceptions.FileFormatException;
import org.htw.prog2.aufgabe1.files.MutationFile;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVFileReaderTest {

    @Test
    void parseSusceptibilities() {
    }

    @Test
    void parseOrganisms_wrongFirstElements() {
        assertThrows(FileFormatException.class, () -> {
            CSVFileReader.parseOrganisms("\"Mutation\";\"Chimp GS\";\"Human GS\";\"Neanderthal GS\"");
        });
    }

    @Test
    void parseOrganisms_wrongOrganismNames() {
        assertThrows(FileFormatException.class, () -> {
            CSVFileReader.parseOrganisms("\"Mutation\";\"ChimpGS\";\"Human GS\";\"Neanderthal GS\"");
        });
        assertThrows(FileFormatException.class, () -> {
            CSVFileReader.parseOrganisms("\"Mutation\";\"Chimp GS\";\"Human GS\";\"Neanderthal\"");
        });
    }

    @Test
    void parseOrganisms() {
        assertDoesNotThrow(() -> {
            List<String> drugs = CSVFileReader.parseOrganisms("\"Mutation Pattern\";\"Chimp GS\";\"Human GS\";\"Neanderthal GS\"");
            LinkedList<String> expected = new LinkedList<>();
            expected.add("Chimp");
            expected.add("Human");
            expected.add("Neanderthal");
            assertEquals(expected, drugs);
        });
    }

    @Test
    void constructor_doesNotExist() {
        assertThrows(FileNotFoundException.class, () -> {
            new CSVFileReader().readFile("data/IDONOTEXIST");
        });
    }

    @Test
    void constructor_wrongHeaderStart() {
        assertThrows(FileFormatException.class, () -> {
            new CSVFileReader().readFile("data/GREB1_patterns_wrongHeader.csv");
        });
    }

    @Test
    void constructor_wrongHeaderDrugnames() {
        assertThrows(FileFormatException.class, () -> {
            new CSVFileReader().readFile("data/GREB1_patterns_wrongHeader2.csv");
        });
    }

    @Test
    void constructor_columnNumbers() {
        assertThrows(FileFormatException.class, () -> {
            new CSVFileReader().readFile("data/GREB1_patterns_wrongElementNumber.csv");
        });
    }

    @Test
    void constructor_correctFile() {
        assertDoesNotThrow(() -> {
            new CSVFileReader().readFile("data/GREB1_patterns.csv");
        });
    }

    @Test
    void getNumberOfMutations() {
        assertDoesNotThrow(() -> {
            MutationFile patterns = new CSVFileReader().readFile("data/GREB1_patterns.csv");
            assertEquals(4, patterns.getNumberOfMutations());
        });
    }

}
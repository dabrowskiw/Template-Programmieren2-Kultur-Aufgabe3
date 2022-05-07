package org.htw.prog2.aufgabe1.readers;

import org.htw.prog2.aufgabe1.exceptions.NoValidReadersException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReaderManagerTest {

    @Test
    void throwsDoesNotExist_getReaderForFile() {
        ReaderManager<SequenceFileReader> manager = new ReaderManager<>();
        manager.addReader(new FASTAFileReader());
        manager.addReader(new FASTQFileReader());
        assertThrows(NoValidReadersException.class, () -> {
            manager.getReaderForFile("DOESNOTEXIST");
        });
    }

    @Test
    void throwsWrongFormat_getReaderForFile() {
        ReaderManager<SequenceFileReader> manager = new ReaderManager<>();
        manager.addReader(new FASTAFileReader());
        manager.addReader(new FASTQFileReader());
        assertThrows(NoValidReadersException.class, () -> {
            manager.getReaderForFile("data/GREB1_patterns.csv");
        });
    }

    @Test
    void correctFormat_getReaderForFile() {
        ReaderManager<SequenceFileReader> manager = new ReaderManager<>();
        manager.addReader(new FASTAFileReader());
        manager.addReader(new FASTQFileReader());
        assertDoesNotThrow(() -> { manager.getReaderForFile("data/site_sequences.fasta"); });
        try {
            SequenceFileReader reader = manager.getReaderForFile("data/site_sequences.fastq");
            assertTrue(reader instanceof FASTQFileReader);
        } catch(Exception e) {
            fail(e);
        }
    }

}
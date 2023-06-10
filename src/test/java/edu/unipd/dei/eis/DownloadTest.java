package edu.unipd.dei.eis;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.io.File;
import edu.unipd.dei.eis.CLI.DownloadCommand;
import org.junit.jupiter.api.Test;

class DownloadTest {
    private String[] sources = {"all"}; // Valore di default per la sorgente degli articoli

    // Path dello storage del test
    private static final File store = new File("src/test/resources/test_storage/DownloadTest");

    private static final int num = 2; // Numero di articoli da scaricare

    @Test
    void testDownload() throws Exception {
        assertDoesNotThrow(() -> {
            /*
             * Finge un comando download con uno storage specifico e un specifico numero di articoli
             * da scaricare
             */
            new DownloadCommand(sources, store, num).task();
        });
    }
}

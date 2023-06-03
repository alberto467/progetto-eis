package edu.unipd.dei.eis;

import java.io.File;
import picocli.CommandLine.Option;

import edu.unipd.dei.eis.CLI.DownloadCommand;

import org.junit.jupiter.api.Test;

class DownloadManagerTest extends DownloadCommand
{
    private String[] sources = {"all"};//Valore di default per la sorgente degli articoli
    private static final File store = new File("src/test/resources/test_storage");//Path dello storage del test
    private static final int num = 2;//Numero di articoli da scaricare

    @Option(names = {"-s", "--sources"},
        description = "A list of sources to download from, separated by a comma. All will use all the available sources (the default). List supported sources with the list-sources command",
        split = ",")

    @Test
    void testdownload()
    {
        try
        {
            task();
        } 
        catch (Exception e){}
    }

    @Override
    public void task() throws Exception
    {
        new DownloadCommand(sources, store, num).task();//Finge un comando download con uno specifico storage e un specifico numero di articoli da scaricare
    }
}

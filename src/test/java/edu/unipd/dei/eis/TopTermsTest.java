package edu.unipd.dei.eis;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.AbstractMap;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;

public class TopTermsTest
{
    private List<Map.Entry<String, Integer>> terms = new ArrayList<>();
    private static final File folder = new File("src/test/resources/test_storage/terms");
    private static final File output = new File("src/test/resources/test_storage/results/test6_results.txt");
    private static final File comparison = new File("src/test/resources/test_storage/results/test4_results.txt");
    
    @Test
    void testtopterms() throws Exception
    {
        File[] input = folder.listFiles((dir, name)->name.endsWith(".json"));//cerca file .json nella cartella folder
        if(input != null)
        {
            BufferedReader reader = new BufferedReader(new FileReader(input[0]));//prende per default il primo file .json
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                jsonContent.append(line);
            }
            reader.close();
            String jsonString = jsonContent.toString();
            jsonString = jsonString.replaceAll("\\s", "");// Rimuove gli spazi bianchi e i caratteri di nuova riga dal contenuto JSON e le virgolette doppie attorno alle chiavi
            jsonString = jsonString.replaceAll("\"", "");
            String innerDataString = jsonString.substring(1, jsonString.length() - 1);// Rimuove le parentesi graffe iniziali e finali e divide i dati interni in parole e valori
            String[] keyValuePairs = innerDataString.split(",");
            for (String keyValuePair : keyValuePairs)// Estrae le parole e i valori dal file json
            {
                String[] parts = keyValuePair.split(":");
                String key = parts[0];
                int value = Integer.parseInt(parts[1]);
                Map.Entry<String, Integer> termEntry = new AbstractMap.SimpleEntry<>(key, value);
                terms.add(termEntry);
            }

            Map<String, Integer> article = new ConcurrentHashMap<String, Integer>();// Seleziona solo i 50 termini con il peso maggiore
            for (Map.Entry<String, Integer> entry : terms)
            {
                String key = entry.getKey();
                Integer value = entry.getValue();
                article.put(key, value);
            }
            terms = (article.entrySet().stream().sorted((a, b) -> a.getKey().compareTo(b.getKey()))
                .sorted((a, b) -> b.getValue() - a.getValue()).limit(50)
                .collect(Collectors.toList()));


            write(output);// test della funzione write

            if(!output.equals(comparison))//confronta il risultato di questo test con il risultato del test4 ExtractionTest
            {
                Assertions.fail("I risultati previsti sono diversi");
            }        
        }
        else Assertions.fail("Ci sono troppi o nessun file .json nella cartella src/test/resources/test_storage/terms");
        
    }

    private void write(File path) throws Exception
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        getPrintStream().forEachOrdered(s -> 
        {
            try
            {
                bw.write(s);
                bw.newLine();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });
        bw.close();
    }

    private Stream<String> getPrintStream() {
        Stream<String> out =
            terms.stream().map(e -> e.getKey().toString() + " " + e.getValue().toString());

        return out;
    }

    @Override
    public String toString()
    {
        return getPrintStream().reduce("", (a, b) -> a + b + "\n");
    }
}

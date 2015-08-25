package xyz.owenjow.swa;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * A base for noun data. This class houses methods for processing synset/hyponym
 * collections, both of which have been conveniently extracted as text files
 * from the WordNet database.
 *
 * Note that for the time being, these data sets are limited to English nouns.
 * @author Owen Jow
 */
public class NounDatabase {
    private static final String SYNSET_FILENAME = "synsets.txt";
    private static final String HYPONYM_FILENAME = "hyponyms.txt";

    private HashMap<Integer, HashSet<String>> synsetsByID;
    private HashMap<String, HashSet<Integer>> synsetsByNoun;
    private Digraph hyponymGraph;

    public NounDatabase(AssetManager assets) {
        synsetsByID = new HashMap<Integer, HashSet<String>>();
        synsetsByNoun = new HashMap<String, HashSet<Integer>>();

        // Parse the synset & hyponym text files
        try {
            processSynsetFile(assets.open(SYNSET_FILENAME));
            populateHyponymGraph(assets.open(HYPONYM_FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
            System.out.println("Database configuration aborted.");
            System.exit(1);
        }
    }

    /**
     * Processes the synset file and updates the internal hash map
     * with the newly acquired data.
     * @param synsetStream the input stream for the synsets.txt file
     */
    private void processSynsetFile(InputStream synsetStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(synsetStream));
        try {
            String[] synsetData = new String[3]; // there should be 3 data components per line
            String line = reader.readLine();

            while (line != null) {
                if (!line.matches("\\d+,.+,.+")) {
                    throw new IllegalArgumentException("Invalid synset file");
                }

                // Split line into its individual synset data components
                synsetData = line.split(",");
                // Split synset into individual nouns
                String[] synonyms = synsetData[1].split(" ");

                HashSet<Integer> idSet;
                HashSet<String> nounSet = new HashSet<String>();

                for (String noun : synonyms) {
                    nounSet.add(noun);

                    if (synsetsByNoun.containsKey(noun)) {
                        idSet = synsetsByNoun.get(noun);
                        idSet.add(Integer.parseInt(synsetData[0]));
                    } else {
                        idSet = new HashSet<Integer>();
                        idSet.add(Integer.parseInt(synsetData[0]));
                        synsetsByNoun.put(noun, idSet);
                    }
                }

                synsetsByID.put(Integer.parseInt(synsetData[0]), nounSet);
                line = reader.readLine(); // read the next line (if there is one)
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * As should be implied, this method populates the hyponym digraph
     * with data from hyponyms.txt.
     * @param hyponymStream the input stream for the hyponyms.txt file
     */
    private void populateHyponymGraph(InputStream hyponymStream) {
        hyponymGraph = new Digraph(synsetsByID.size()); // initialize the digraph to be filled in

        BufferedReader reader = new BufferedReader(new InputStreamReader(hyponymStream));
        String line;

        try {
            line = reader.readLine();
            while (line != null) {
                String[] hyponymData = line.split(",");
                Integer hypernymID = Integer.parseInt(hyponymData[0]);

                for (int i = 1; i < hyponymData.length; i++) {
                    hyponymGraph.addEdge(hypernymID, Integer.parseInt(hyponymData[i]));
                }

                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns a set that contains all of the hyponyms and synonyms of WORD.
     * @param word the word at the center of all the attention
     */
    public HashSet<String> getWordAssociations(String word) {
        if (!synsetsByNoun.containsKey(word)) {
            return new HashSet<String>(); // if word is not in the database, return an empty set
        }

        HashSet<Integer> synsetIDs = synsetsByNoun.get(word);
        Set<Integer> hyponymIDs = hyponymGraph.reachableVertices(synsetIDs);

        HashSet<String> associations = new HashSet<String>();
        // Include all synonyms of WORD
        for (Integer id : synsetIDs) {
            associations.addAll(synsetsByID.get(id));
        }
        // Include all hyponyms of WORD
        for (Integer id : hyponymIDs) {
            associations.addAll(synsetsByID.get(id));
        }

        return associations;
    }
}

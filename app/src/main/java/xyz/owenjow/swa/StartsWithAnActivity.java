package xyz.owenjow.swa;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

/**
 * The main activity for the SWA application.
 * @author Owen Jow
 */
public class StartsWithAnActivity extends AppCompatActivity {
    Spinner letterSpinner;
    NounDatabase nounDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_starts_with_an);

        // Spinner setup
        letterSpinner = (Spinner) findViewById(R.id.letterSpinner);
        configureSpinner();

        // Process all of the noun data
        nounDB = new NounDatabase(getAssets());
    }

    /**
     * Configures the letter spinner for use in the application.
     */
    private void configureSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.letters, R.layout.spinner_item_view);
        adapter.setDropDownViewResource(R.layout.spinner_item_view);
        letterSpinner.setAdapter(adapter);
    }

    /**
     * The method that's called whenever the "get word" button is pressed.
     * It will (indirectly) search the database for a match and subsequently
     * add that result to the display.
     * @param view the mandatory View parameter
     */
    public void getWord(View view) {
        // Retrieve the starting letter and associated words
        String letter = letterSpinner.getSelectedItem().toString().toLowerCase();
        EditText wordsET  = (EditText) findViewById(R.id.wordDesc);
        String[] words = wordsET.getText().toString().split("[^a-zA-Z]+");

        // Update the results area with the (hopefully) desired word
        TextView resultsView = (TextView) findViewById(R.id.results);
        resultsView.setText(searchDatabase(letter, words));
    }

    /**
     * Searches the database for a word that starts with LETTER
     * and has the greatest association with the contents of the WORDS array.
     * @param letter the first letter of the desired word result
     * @param words an array of words that the user associates with the desired result
     * @return the most promising word
     */
    private String searchDatabase(String letter, String[] words) {
        // Create a hash map that will keep track of the number of times each association comes up
        HashMap<String, Integer> associationCts = new HashMap<String, Integer>();

        // These variables will keep track of aforementioned "most promising word"
        String bestMatch = null;
        int highestCount = 0;

        Integer count; // the count for each new association
        for (String word : words) {
            for (String association : nounDB.getWordAssociations(word)) {
                if (association.toLowerCase().startsWith(letter)) {
                    // We'll only consider the word at all if it starts with LETTER

                    // First, reformat the constituent words of a collocation (repl "_"s w/ " "s)
                    association = association.replace('_', ' ');

                    count = associationCts.get(association);
                    if (count == null) { // the word hasn't been seen before
                        count = 1;
                    } else {
                        count += 1;
                    }

                    // Add the word to the map of association rankings
                    associationCts.put(association, count);
                    if (count > highestCount) {
                        highestCount = count;
                        bestMatch = association;
                    }
                }
            }
        }

        if (bestMatch == null) {
            return "NO WORDS FOUND";
        } else {
            return bestMatch;
        }
    }
}

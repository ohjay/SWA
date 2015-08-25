package xyz.owenjow.swa;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
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
    private Spinner letterSpinner;
    private NounDatabase nounDB;
    private Dialog loadingDialog;
    private static final int CHECK_DELAY = 1000; // ms between processing completion checks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayLoadingScreen();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_starts_with_an);

        // Spinner setup
        letterSpinner = (Spinner) findViewById(R.id.letterSpinner);
        configureSpinner();

        // WordNet parsing performed in a separate thread so as not to bottleneck everything
        new Thread(new Runnable() {
            public void run() {
                // Process all of the noun data
                nounDB = new NounDatabase(getAssets());
            }
        }).start();
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
     * Shows the loading screen (to be used while the noun data is being processed).
     */
    private void displayLoadingScreen() {
        loadingDialog = new Dialog(this, R.style.LoadingScreen);
        loadingDialog.setContentView(R.layout.loading_screen);
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        // Of course, we'll want to remove the loading screen after the background job is done
        activateCheckHandler();
    }

    /**
     * Schedules a processing completion check for the near future.
     * For each check: if the noun processing has finished, we will close
     * the loading dialog so that the main content can be displayed. Otherwise
     * we will set another check to run in CHECK_DELAY ms.
     */
    private void activateCheckHandler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (nounDB != null) {
                    // Processing is complete; remove the loading screen
                    loadingDialog.dismiss();
                    loadingDialog = null;
                } else {
                    // Check again in CHECK_DELAY ms
                    activateCheckHandler();
                }
            }
        }, CHECK_DELAY);
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
        // Create a hash map that will keep track of the score for each association
        HashMap<String, Integer> associationScores = new HashMap<String, Integer>();

        // These variables will keep track of aforementioned "most promising word"
        String bestMatch = null;
        int highestScore = 0;

        int score; // the score for each new association
        String definition; // the definition for some word in question
        for (String word : words) {
            for (String association : nounDB.getWordAssociations(word.toLowerCase())) {
                if (association.toLowerCase().startsWith(letter)) {
                    // We'll only consider the association at all if it starts with LETTER

                    score = 2; // the word came up here, so it gets a +2 bonus no matter what

                    // First, analyze the dictionary definition of our potential answer
                    definition = nounDB.getDefinition(association).toLowerCase();
                    for (String w : words) {
                        if (definition.contains(w.toLowerCase())) {
                            score += 1;
                        }
                    }

                    // Reformat the constituent words of a collocation (repl "_"s w/ " "s)
                    association = association.replace('_', ' ');

                    Integer currScore = associationScores.get(association);
                    if (currScore != null) { // the word already has a running score
                        score += currScore; // ...so we'll add it in
                    }

                    // Add the word to the map of association rankings
                    associationScores.put(association, score);
                    if (score > highestScore) {
                        highestScore = score;
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

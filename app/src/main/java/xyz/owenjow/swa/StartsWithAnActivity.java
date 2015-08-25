package xyz.owenjow.swa;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
        return "";
    }
}

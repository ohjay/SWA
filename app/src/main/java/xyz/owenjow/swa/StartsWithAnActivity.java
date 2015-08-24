package xyz.owenjow.swa;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

public class StartsWithAnActivity extends AppCompatActivity {
    Spinner letterSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_starts_with_an);

        // Spinner setup
        letterSpinner = (Spinner) findViewById(R.id.letterSpinner);
        configureSpinner();
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

    public void getWord(View view) {
        // Retrieve the starting letter and associated words
        String letter = letterSpinner.getSelectedItem().toString().toLowerCase();
        EditText wordsET  = (EditText) findViewById(R.id.wordDesc);
        String[] words = wordsET.getText().toString().split("[^a-zA-Z]+");

        // Update the results area with the (hopefully) desired word
        TextView resultsView = (TextView) findViewById(R.id.results);
        resultsView.setText(searchWordNet(letter, words));
    }

    /**
     * Searches the WordNet database for a word that starts with LETTER
     * and has the greatest association with the contents of the WORDS array.
     * @param letter the first letter of the desired word result
     * @param words an array of words that the user associates with the desired result
     * @return the most promising word
     */
    private String searchWordNet(String letter, String[] words) {
        // Set up the WordNet dictionary
        String path = ".." + File.separator + ".." + File.separator + ".." + File.separator
                + ".." + File.separator + "assets" + File.separator + "dict";
        URL url;
        try {
            url = new URL("file", null, path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "ERROR: MALFORMED URL";
        }

        IDictionary dict = new Dictionary(url);
        try {
            dict.open();
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR: DICTIONARY COULD NOT BE OPENED";
        }

        // This is only a test
        IIndexWord idxWord = dict.getIndexWord("test", POS.NOUN);
        IWordID wordID = idxWord.getWordIDs().get(0);
        IWord word = dict.getWord(wordID);
        return word.getLemma();
    }
}

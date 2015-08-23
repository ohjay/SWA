package xyz.owenjow.swa;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
        String letter = letterSpinner.getSelectedItem().toString();
        EditText wordsET  = (EditText) findViewById(R.id.wordDesc);
        String[] words = wordsET.getText().toString().split("[^a-zA-Z]+");

        String result = "";
        for (int i = 0; i < words.length; i++) {
            result += words[i] + " ";
        }

        // Update the results area with the (hopefully) desired word
        TextView resultsView = (TextView) findViewById(R.id.results);
        resultsView.setText("Letter: " + letter + "; Words: " + result);
    }
}

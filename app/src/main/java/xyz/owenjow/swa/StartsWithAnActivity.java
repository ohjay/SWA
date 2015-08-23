package xyz.owenjow.swa;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class StartsWithAnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_starts_with_an);
        configureSpinner();
    }

    /**
     * Configures the letter spinner for use in the application.
     */
    private void configureSpinner() {
        Spinner letterSpinner = (Spinner) findViewById(R.id.letterSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.letters, R.layout.spinner_item_view);
        adapter.setDropDownViewResource(R.layout.spinner_item_view);
        letterSpinner.setAdapter(adapter);
    }
}

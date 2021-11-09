package ca.cmpt276.parentapp;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(this, "spinnerPosition chosen", Toast.LENGTH_LONG).show();
        /*spinnerPosition = position;*/
    }
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
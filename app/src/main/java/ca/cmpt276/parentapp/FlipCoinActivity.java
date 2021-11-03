package ca.cmpt276.parentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.TextView;

import ca.cmpt276.parentapp.model.FlipCoin;
import ca.cmpt276.parentapp.model.FlipCoinManager;

public class FlipCoinActivity extends AppCompatActivity {
    FlipCoinManager flipCoinManager = FlipCoinManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin);

        getChildSelection();
    }

    private void getChildSelection() {
        //COMMENT: I don't think a dialog is an appropriate view for user to choose a button
        FragmentManager manager = getSupportFragmentManager();
        CoinSideSelectMessageFragment dialog = new CoinSideSelectMessageFragment();
        dialog.show(manager, "MessageDialog");
    }

}
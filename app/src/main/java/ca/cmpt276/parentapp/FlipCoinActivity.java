package ca.cmpt276.parentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.TextView;

import ca.cmpt276.parentapp.model.FlipCoin;

public class FlipCoinActivity extends AppCompatActivity {
    FlipCoin flipCoin = FlipCoin.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin);

        getChildSelection();
    }

    private void getChildSelection() {
        FragmentManager manager = getSupportFragmentManager();
        CoinSideSelectMessageFragment dialog = new CoinSideSelectMessageFragment();
        dialog.show(manager, "MessageDialog");
    }
}
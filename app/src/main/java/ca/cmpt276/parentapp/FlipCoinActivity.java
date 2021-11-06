package ca.cmpt276.parentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.FlipCoin;
import ca.cmpt276.parentapp.model.FlipCoinManager;

public class FlipCoinActivity extends AppCompatActivity {
    FlipCoinManager flipCoinManager = FlipCoinManager.getInstance();
    // For testing
    ArrayList<Child> childrenList = new ArrayList<Child>();
    Child able = new Child("Able");
    Child betty = new Child("Betty");
    Child peter = new Child("Peter");

    FlipCoin flipCoin;
    int index;
    FlipCoin.CoinSide childChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin);
        // For testing
        childrenList.add(able);
        childrenList.add(betty);
        childrenList.add(peter);

        flipCoin = new FlipCoin(childrenList);
        flipCoinManager.addGame(flipCoin);
        index = flipCoinManager.getCurrentIndex(childrenList.size());
        initializeLayout();
    }

    private void initializeLayout() {
        TextView showPicker = findViewById(R.id.showPicker);
        flipCoin.setPicker(index);
        String message = "It's " + flipCoin.getPicker().getName() + "'s turn! Pick a side.";
        showPicker.setText(message);

        ImageView coin = (ImageView) findViewById(R.id.iv_coin);

        Button btnHeads = (Button) findViewById(R.id.heads);
        Button btnTails = (Button) findViewById(R.id.tails);

        btnHeads.setOnClickListener(View -> {
            btnHeads.setEnabled(false);
            btnTails.setEnabled(false);
        });



    }

}
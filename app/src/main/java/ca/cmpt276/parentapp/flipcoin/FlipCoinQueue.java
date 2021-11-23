package ca.cmpt276.parentapp.flipcoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.FlipCoinQueueAdapter;
import ca.cmpt276.parentapp.model.FlipCoinManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class FlipCoinQueue extends AppCompatActivity {
    FlipCoinQueueAdapter flipCoinQueueAdapter;
    FlipCoinManager coinManager;

    //Create an intent for this activity
    public static Intent makeIntent(Context context){
        return new Intent(context, FlipCoinQueue.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin_queue);

        this.setTitle("Flip Coin Queue");

        coinManager = FlipCoinManager.getInstance();

        Button default_none = findViewById(R.id.default_empty_btn);
        default_none.setOnClickListener(view ->{
            coinManager.setDefaultEmpty(true);
            finish();
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        flipCoinQueueAdapter = new FlipCoinQueueAdapter(FlipCoinQueue.this,
                                                            coinManager.getPlayerList());
        ListView gameList = findViewById(R.id.flipCoinQueue);
        gameList.setAdapter(flipCoinQueueAdapter);

        setClickGameList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //Function to edit default player when clicked
    private void setClickGameList(){
        ListView list = findViewById(R.id.flipCoinQueue);
        list.setOnItemClickListener((parent, viewClicked, position, id) -> {
            addConfirmationDialog_Override(position);
        });
    }

    //Adds a confirmation dialog when user tries to delete a game
    private void addConfirmationDialog_Override(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to override the default with this child?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    coinManager.overrideDefault(position);
                    flipCoinQueueAdapter.notifyDataSetChanged();
                    Toast.makeText(FlipCoinQueue.this,
                            "Default child overridden successfully",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
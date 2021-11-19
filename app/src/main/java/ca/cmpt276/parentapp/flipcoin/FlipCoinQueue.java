package ca.cmpt276.parentapp.flipcoin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.FlipCoin_Queue_Adapter;
import ca.cmpt276.parentapp.model.FlipCoinManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class FlipCoinQueue extends AppCompatActivity {
    FlipCoin_Queue_Adapter flipCoin_queue_adapter;
    FlipCoinManager coin_manager;

    //Create an intent for this activity
    public static Intent makeIntent(Context context){
        return new Intent(context, FlipCoinQueue.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin_queue);

        this.setTitle("Flip Coin Queue");

        coin_manager = FlipCoinManager.getInstance();

        Button default_none = findViewById(R.id.default_empty_btn);
        default_none.setOnClickListener(view ->{
            coin_manager.setDefaultEmpty(true);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        flipCoin_queue_adapter = new FlipCoin_Queue_Adapter(FlipCoinQueue.this,coin_manager.getPlayerList());
        ListView gameList = findViewById(R.id.flipCoinQueue);
        gameList.setAdapter(flipCoin_queue_adapter);

        setClickGameList();
    }

    //Function to edit default player when clicked
    private void setClickGameList(){
        ListView list = findViewById(R.id.flipCoinQueue);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                addConfirmationDialog_Override(position);
            }
        });
    }

    //Adds a confirmation dialog when user tries to delete a game
    private void addConfirmationDialog_Override(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to override the default with this child?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        coin_manager.overrideDefault(position);
                        flipCoin_queue_adapter.notifyDataSetChanged();
                        Toast.makeText(FlipCoinQueue.this,
                                "Default child overridden successfully",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
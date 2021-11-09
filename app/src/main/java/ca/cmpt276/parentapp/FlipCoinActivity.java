package ca.cmpt276.parentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.FlipCoin;
import ca.cmpt276.parentapp.model.FlipCoinManager;
import ca.cmpt276.parentapp.model.childManager;

public class FlipCoinActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCE = "Shared Preference";
    public static final String SAVE_COIN_MANAGER = "SAVE_COIN_MANAGER";

    childManager manager;
    ArrayList<Child> childrenList;

    FlipCoinManager flipCoinManager;
    FlipCoin flipCoinGame, newGame;
    int index;

    FlipCoin.CoinSide currentCoinSideInImg = FlipCoin.CoinSide.HEADS; //Initial coin side in image
    FlipCoin.CoinSide coinResult;
    FlipCoin.CoinSide pickerChoice;

    ObjectAnimator animStage1, animStage2;
    Button headButton, tailButton;
    ImageView coinImg;
    TextView showPicker;

    Button historyButton;

    MediaPlayer coinFlipSound;

    int rotationCount = 0;
    int maxRepeat = 6;
    boolean emptyChildrenList;

    Toolbar toolbar;

    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin);
         //For testing
//        childrenList.add(able);
//        childrenList.add(betty);
//        childrenList.add(peter);

        coinFlipSound = MediaPlayer.create(this, R.raw.coin_flip_sound);

        initializeLayout();
        initializeAnimation();

        this.setTitle("Flip Coin");
    }

    @Override
    protected void onResume() {
        loadData();
        enableButtons();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        flipCoinManager.resetIndex();
        saveData();
        super.onDestroy();
    }

    //Save current data of the gameManager using SharedPreferences
    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Convert gridManager to json format
        Gson gson = new Gson();
        String json = gson.toJson(flipCoinManager);

        //Save the json
        editor.putString(SAVE_COIN_MANAGER,json);
        editor.apply();
    }

    //Load data from saved state
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);

        //Get the model in json format
        Gson gson = new Gson();
        String coin_json = sharedPreferences.getString(SAVE_COIN_MANAGER,null);
        String child_json = sharedPreferences.getString(ChildConfigurationActivity.CHILD_LIST,null);

        ///----------------------------- Get ChildManager data --------------------------------/////
        //Covert the childManager into an Object and set the instance to the specified gameManager
        manager = gson.fromJson(child_json, childManager.class);
        childManager.setInstance(manager);

        if(manager == null){
            manager = childManager.getInstance();
        }

        childrenList = manager.getChildList();

        ///----------------------------- Get FlipCoinManager data ----------------------------/////

        //Covert the gameManager into an Object and set the instance to the specified gameManager
        flipCoinManager = gson.fromJson(coin_json,FlipCoinManager.class);
        FlipCoinManager.setInstance(flipCoinManager);

        if(flipCoinManager == null){
            flipCoinManager = FlipCoinManager.getInstance();
        }

        if (childrenList.size() > 0) {
            flipCoinGame = new FlipCoin(childrenList);

            index = flipCoinManager.getCurrentIndex(childrenList.size());
            flipCoinGame.setPicker(childrenList.get(index));
            String message = getString(R.string.player_turn,flipCoinGame.getPicker().getName());
            showPicker.setText(message);
        }
        else {
            emptyChildrenList = true;
            showPicker.setText(R.string.no_configured_children);
        }


    }

    ///--------------------------Functions for initialization-------------------------///

    private void initializeLayout() {
        resultText = findViewById(R.id.resultMessage);
        coinImg = findViewById(R.id.iv_coin);
        showPicker = findViewById(R.id.showPicker);

        /*toolbar = findViewById(R.id.flip_coin_toolbar);
        toolbar.setTitle("Flip Coin");
        setSupportActionBar(toolbar);
*/
        setUpButtons();
    }

    private void initializeAnimation(){
        animStage1 = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.flipx1);
        animStage2 = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.flipx2);

        animStage1.setTarget(coinImg);
        animStage2.setTarget(coinImg);

        animStage1.setDuration(100);
        animStage2.setDuration(100);

        animStage1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (currentCoinSideInImg == FlipCoin.CoinSide.HEADS){
                    coinImg.setImageResource(R.drawable.loonie_tails);
                    currentCoinSideInImg = FlipCoin.CoinSide.TAILS;
                }
                else{
                    coinImg.setImageResource(R.drawable.loonie_heads);
                    currentCoinSideInImg = FlipCoin.CoinSide.HEADS;
                }
                animStage2.start();
            }
        });

        animStage2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                rotationCount++;

                if (rotationCount < maxRepeat){
                    animStage1.start();
                }

                else{

                    try {
                        coinFlipSound.stop();
                        coinFlipSound.prepare();
                    } catch (IOException e) {
                        Toast.makeText(FlipCoinActivity.this, "Error in ending sound",
                                Toast.LENGTH_SHORT).show();
                    }

                    enableButtons();

                    //Only update if there are children in the list
                    if (!emptyChildrenList){

                        //Set results onto the object and save that data
                        //flipCoinGame.setFlipResult(coinResult);
                        flipCoinManager.addGame(flipCoinGame);
                        index = flipCoinManager.updateIndex(childrenList.size());
                        displayResultMessage();
                        saveData();

                        //Create a new game
                        newGame = new FlipCoin(childrenList);
                        flipCoinGame = newGame;
                        flipCoinGame.setPicker(childrenList.get(index));

                        String message = getString(R.string.player_turn,
                                flipCoinGame.getPicker().getName());
                        showPicker.setText(message);
                    }

                    else{
                        displayMessageForEmpty(pickerChoice,coinResult);
                    }

                    rotationCount = 0;
                }
            }
        });
    }

    private void setUpButtons(){
        headButton = findViewById(R.id.btn_heads);
        tailButton = findViewById(R.id.btn_tails);
        historyButton = findViewById(R.id.historyButton);

        headButton.setOnClickListener(view -> {
            disableButtons();

            if (!emptyChildrenList) {
                String message = getString(R.string.player_choice,
                        flipCoinGame.getPicker().getName(),
                        FlipCoin.CoinSide.HEADS.toString());
                showPicker.setText(message);
                flipCoinGame.setPickerChoice(FlipCoin.CoinSide.HEADS);
            }

            pickerChoice = FlipCoin.CoinSide.HEADS;
            resultText.setText("");
            coinFlipSound.start();
            flipCoinImg();
        });

        tailButton.setOnClickListener(view -> {
            disableButtons();

            if (!emptyChildrenList) {
                String message = getString(R.string.player_choice,
                                            flipCoinGame.getPicker().getName(),
                                            FlipCoin.CoinSide.TAILS.toString());
                showPicker.setText(message);
                flipCoinGame.setPickerChoice(FlipCoin.CoinSide.TAILS);
            }

            pickerChoice = FlipCoin.CoinSide.TAILS;
            resultText.setText("");
            coinFlipSound.start();
            flipCoinImg();
        });

        historyButton.setOnClickListener(View -> {
            disableButtons();

            Intent intent = FlipCoinHistory.makeIntent(this);
            startActivity(intent);
        });
    }

    ///--------------------------Functions to update Views-------------------------///

    private void displayResultMessage() {
        if (flipCoinGame.isPickerWinner()){
            resultText.setText(getString(R.string.win_text,
                    flipCoinGame.getFlipResult().toString()));
        }
        else {
            resultText.setText(getString(R.string.lose_text,
                    flipCoinGame.getFlipResult().toString()));
        }
    }

    private void displayMessageForEmpty(FlipCoin.CoinSide pickerChoice,
                                        FlipCoin.CoinSide flipResult) {

        if (pickerChoice == flipResult){
            resultText.setText(getString(R.string.win_text,
                    flipResult.toString()));
        }
        else {
            resultText.setText(getString(R.string.lose_text,
                    flipResult.toString()));
        }
    }

    private void flipCoinImg(){
        disableButtons();
        coinResult = !emptyChildrenList ? flipCoinGame.flipCoin() : new FlipCoin().flipCoin();
        //coinResult = new FlipCoin().flipCoin();
        if (coinResult == FlipCoin.CoinSide.HEADS){
            Log.i("CoinResult:", "HEADS");
            if (currentCoinSideInImg == FlipCoin.CoinSide.HEADS){
                maxRepeat = 6; // Lands on same side
            }
            else{
                maxRepeat = 7; // Land on the different side
            }
        }
        else {
            Log.i("CoinResult:", "TAILS");
            if (currentCoinSideInImg == FlipCoin.CoinSide.TAILS) {
                maxRepeat = 6;
            } else {
                maxRepeat = 7;
            }
        }

        animStage1.start();
    }

    public void enableButtons() {
        headButton.setEnabled(true);
        tailButton.setEnabled(true);
        historyButton.setEnabled(true);
    }

    public void disableButtons(){
        headButton.setEnabled(false);
        tailButton.setEnabled(false);
        historyButton.setEnabled(false);
    }

}

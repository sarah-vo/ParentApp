package ca.cmpt276.parentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.FlipCoin;
import ca.cmpt276.parentapp.model.FlipCoinManager;

public class FlipCoinActivity extends AppCompatActivity {
    // For testing
    ArrayList<Child> childrenList = new ArrayList<>();
    Child able = new Child("Able");
    Child betty = new Child("Betty");
    Child peter = new Child("Peter");

    FlipCoinManager flipCoinManager = FlipCoinManager.getInstance();
    FlipCoin flipCoin;
    int index;
    FlipCoin.CoinSide currentCoinSideInImg = FlipCoin.CoinSide.HEADS;     //Initial coin side in image
    FlipCoin.CoinSide coinResult;

    ObjectAnimator animStage1, animStage2;
    Button headButton, tailButton;
    ImageView coinImg;
    TextView showPicker;

    MediaPlayer coinFlipSound;

    int repeatCount = 0;
    int maxRepeat = 6;
    boolean emptyChildrenList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin);
         //For testing
        childrenList.add(able);
        childrenList.add(betty);
        childrenList.add(peter);

        initializeHistoryButton();

        showPicker = findViewById(R.id.showPicker);
        if (childrenList.size() > 0) {
            flipCoin = new FlipCoin(childrenList);
            flipCoinManager.addGame(flipCoin);
            index = flipCoinManager.getCurrentIndex(childrenList.size());
            initializeLayout();
        }
        else {
            emptyChildrenList = true;
            showPicker.setText("There are no configured children, but you can sill play with the coin!");
            coinImg = findViewById(R.id.iv_coin);
            setUpButtons();
        }
        initializeAnimation();

        coinImg.setOnClickListener(view -> {
            maxRepeat = 1;
            animStage1.start();
        });

    }

    private void initializeHistoryButton() {
        Button historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(View -> {
            Intent intent = FlipCoinHistory.makeIntent(this);
            startActivity(intent);
        });
    }

    private void initializeLayout() {
        flipCoin.setPicker(index);
        String message = "It's " + flipCoin.getPicker().getName() + "'s turn! Pick a side.";
        showPicker.setText(message);

        coinImg = findViewById(R.id.iv_coin);
        setUpButtons();
    }

    private void setUpButtons(){
        headButton = findViewById(R.id.btn_heads);
        tailButton = findViewById(R.id.btn_tails);
        coinFlipSound = MediaPlayer.create(this, R.raw.coin_flip_sound);

        headButton.setOnClickListener(view -> {
            if (!emptyChildrenList) {
                String message = flipCoin.getPicker().getName() + " chose " + FlipCoin.CoinSide.HEADS.toString();
                showPicker.setText(message);
                flipCoin.setIsPickerWinner(FlipCoin.CoinSide.HEADS);
            }
            coinFlipSound.start();
            flipCoinImg();
        });

        tailButton.setOnClickListener(view -> {
            if (!emptyChildrenList) {
                String message = flipCoin.getPicker().getName() + " chose " + FlipCoin.CoinSide.TAILS.toString();
                showPicker.setText(message);
                flipCoin.setIsPickerWinner(FlipCoin.CoinSide.TAILS);
            }
            coinFlipSound.start();
            flipCoinImg();
        });
    }

    private void flipCoinImg(){
        disableButtons();
        if (emptyChildrenList) {
            Random rand = new Random();
            coinResult = FlipCoin.CoinSide.values()[rand.nextInt(2)];
        }
        else {
            coinResult = flipCoin.getFlipResult();
        }

        if (coinResult == FlipCoin.CoinSide.HEADS){
            Log.i("FlipResult:", "HEADS");
            if (currentCoinSideInImg == FlipCoin.CoinSide.HEADS){
                maxRepeat = 6; // Land on the same side as before flip
            }
            else{
                maxRepeat = 7; // Land on the different side
            }
        }
        else {
            Log.i("FlipResult:", "TAILS");
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
    }

    public void disableButtons(){
        headButton.setEnabled(false);
        tailButton.setEnabled(false);
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
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                repeatCount++;

                if (repeatCount < maxRepeat){
                    animStage1.start();
                }
                else{
                    if (repeatCount > 1 && !emptyChildrenList){ // repeatCount > 1 means animation is not triggered by changing side before flip
                        displayResultMessage();
                    }
                    if (emptyChildrenList) {
                        enableButtons();
                    }


                    try {
                        coinFlipSound.stop();
                        coinFlipSound.prepare();
                    } catch (IOException e) {
                        Toast.makeText(FlipCoinActivity.this, "Error in ending sound",
                                Toast.LENGTH_SHORT).show();
                    }
                    repeatCount = 0;
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void displayResultMessage() {
        TextView tvResultMessage = findViewById(R.id.resultMessage);
        String showResult = "The result is " + flipCoin.getFlipResult().toString();
        if (flipCoin.isPickerWinner()){
            tvResultMessage.setText(showResult + ". Congratulations! You won.");
        }
        else {
            tvResultMessage.setText(showResult + ". Sorry you guessed wrong, better luck next time.");
        }
    }

}

package ca.cmpt276.parentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    //Initial coin side in image
    FlipCoin.CoinSide current_coin_side_in_img = FlipCoin.CoinSide.HEADS;
    FlipCoin.CoinSide coin_result;

    ObjectAnimator animStage1, animStage2;

    Button head_button, tail_button;

    int repeat_count = 0;
    int max_repeat = 6;
    ImageView coin_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin);
        // For testing
        childrenList.add(able);
        childrenList.add(betty);
        childrenList.add(peter);

        coin_img = findViewById(R.id.iv_coin);

        flipCoin = new FlipCoin(childrenList);
        flipCoinManager.addGame(flipCoin);
        index = flipCoinManager.getCurrentIndex(childrenList.size());
        initializeLayout();

        //Initial side of the img coin
        setHead();
        resetRepeatCount();

        coin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRepeat(1);
                animStage1.start();
            }
        });

        initializeAnimation();
        setUpButtons();
    }

    private void initializeLayout() {
        TextView showPicker = findViewById(R.id.showPicker);
        flipCoin.setPicker(index);
        String message = "It's " + flipCoin.getPicker().getName() + "'s turn! Pick a side.";
        showPicker.setText(message);

        ImageView coin = (ImageView) findViewById(R.id.iv_coin);

        initializeAnimation();
        setUpButtons();
    }

    private void initializeAnimation(){

        animStage1 =  (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.flipx1);
        animStage2 = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.flipx2);
        animStage1.setTarget(coin_img);
        animStage2.setTarget(coin_img);
        animStage1.setDuration(100);
        animStage2.setDuration(100);

        animStage1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (current_coin_side_in_img == FlipCoin.CoinSide.HEADS){
                    coin_img.setImageResource(R.drawable.loonie_tails);
                    setTail();
                }
                else{
                    coin_img.setImageResource(R.drawable.loonie_heads);
                    setHead();
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

                addRepeatCount();
                if (repeat_count < max_repeat){
                    animStage1.start();
                }
                else{
                    coin_img.setClickable(true);
                    enableButtons();
                    resetRepeatCount();
                }
            }
        });
    }

    private void setUpButtons(){
        head_button = findViewById(R.id.btn_heads);
        tail_button = findViewById(R.id.btn_tails);


        head_button.setOnClickListener(view -> {
            flipCoin_img();
        });

        tail_button.setOnClickListener(view -> {
            flipCoin_img();
        });
    }

    private void flipCoin_img(){
        disableButtons();
        coin_result = flipCoin.flipCoin();

        if (coin_result == FlipCoin.CoinSide.HEADS){
            Log.i("FlipResult:", "HEADS");
            if (getCurrentCoinSide() == FlipCoin.CoinSide.HEADS){
                setRepeat(6);
            }

            else{
                setRepeat(7);
            }

            animStage1.start();
        }

        else{
            Log.i("FlipResult:", "TAILS");
            if (getCurrentCoinSide() == FlipCoin.CoinSide.HEADS){
                setRepeat(7);
            }

            else{
                setRepeat(6);
            }

            animStage1.start();
        }
    }

    private void enableButtons(){
        head_button.setClickable(true);
        tail_button.setClickable(true);
    }

    public void disableButtons(){
        head_button.setClickable(false);
        tail_button.setClickable(false);
    }


    //////------------------Functions For Animation --------------------/////////////////
    public void setHead(){
        current_coin_side_in_img = FlipCoin.CoinSide.HEADS;
    }

    public void setTail(){
        current_coin_side_in_img = FlipCoin.CoinSide.TAILS;
    }

    //If repeat coin is odd, the coin will change sides when flipped. Else, it will be the same side
    public void setRepeat(int count){
        max_repeat = count;
    }

    public void addRepeatCount(){
        repeat_count++;
    }

    public void resetRepeatCount(){
        repeat_count = 0;
    }

    public FlipCoin.CoinSide getCurrentCoinSide(){
        return current_coin_side_in_img;
    }

}
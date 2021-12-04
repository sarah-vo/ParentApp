package ca.cmpt276.parentapp.breath;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.parentapp.R;

public class breathActivity extends AppCompatActivity {
    // State Pattern's base states
    private abstract class State {
        // If putting in new file, you might want State base class to
        // hold a reference to the activity

        //TODO: should this activity be MainActivity or breathActivity
        private breathActivity context;
        public State(breathActivity context) {
            this.context = context;
        }


        // Empty implementations, so derived class don't need to
        // override methods they don't care about.
        void handleEnter() {}

        void handleExit() {}

        void handleClickOff() {}

        void handleThreeSecsLess() {}

    }

    public final State inState = new inState(breathActivity.this);
    public final State outState = new outState(breathActivity.this);
    private State currentState = new IdleState(breathActivity.this);
    public final State preBreathState = new preBreathState(breathActivity.this);

    public void setState(State newState) {
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }

    // Android Code
    //Android global variables
    int breathNum;
    CountDownTimer countDownTimer;
    int miliseconds = 0;
    final int MINIMUM_MILLISECONDS_FOR_INHALE = 3000;
    MediaPlayer music;
    Button breathButton;
    Button addBreath;
    Button decreaseBreath;
    boolean outOfBreath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);
        music = MediaPlayer.create(this, R.raw.piano_moment);
        addBreath = findViewById(R.id.btnAddBreath);
        decreaseBreath = findViewById(R.id.btnRemoveBreath);

        setState(preBreathState);
    }

    //TODO fix string extract
    private void configureTextView() {
        Log.d("configureTextView", "In!");

        updateTextView();

        Button addBreath = findViewById(R.id.btnAddBreath);
        addBreath.setOnClickListener(view -> {
            if (breathNum < 10) {
                breathNum++;
                outOfBreath = false;
                updateTextView();

            } else {
                Toast.makeText(this, "Please select between 1 and 10 breaths", Toast.LENGTH_SHORT).show();
            }
        });

        Button decreaseBreath = findViewById(R.id.btnRemoveBreath);
        decreaseBreath.setOnClickListener(view -> {
            if (breathNum > 0) {
                breathNum--;
                updateTextView();
            } else {
                Toast.makeText(this, "Please select between 1 and 10 breaths", Toast.LENGTH_SHORT).show();
            }
        });

    }
    /**Call whenever breathout is triggered**/
    private void updateTextView(){
        TextView heading = findViewById(R.id.tvHeading);
        heading.setText("Let's take " + breathNum + " breaths together");
    }



    // ************************************************************
    // Breath in State
    // ************************************************************
    private class inState extends State {

        public inState(breathActivity context) {
            super(context);
        }

        @SuppressLint("ClickableViewAccessibility")
        private void setUpBreathInButton(){
            breathButton.setOnTouchListener((view, motionEvent) -> {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        Toast.makeText(getApplicationContext(), "Now inhale ...", Toast.LENGTH_LONG).show();
                        breathInAnimation();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        //currentState would be inState
                        if(countDownTimer != null){
                            countDownTimer.cancel();
                        }
                        music.pause();
                        currentState.handleClickOff();
                        break;
                    }
                }
                return false;
            });
        }

        /**Animation for breath in, used in inState**/
        @SuppressLint("ClickableViewAccessibility")
        private void breathInAnimation(){
            music.start();
            breathButton.setText("In");
            countDownTimer = new CountDownTimer(10000, 100) {
                @Override
                public void onTick(long l) {
                    ViewGroup.LayoutParams params = breathButton.getLayoutParams();
                    params.width *= 1.01;
                    params.height *= 1.01;
                    breathButton.setLayoutParams(params);
                    miliseconds += 100;
                }

                @Override
                public void onFinish() {
                    countDownTimer.cancel();
                    music.pause();
                    currentState.handleClickOff();
                }
            }.start();

        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        void handleEnter() {
            Log.d("inState", "In!");

            addBreath.setVisibility(View.INVISIBLE);
            decreaseBreath.setVisibility(View.INVISIBLE);
            if(breathNum <= 0){
                setState(preBreathState);

            }
            else{
                Log.d("inState", "Animation setup-ed!");
                setUpBreathInButton();
            }
        }

        @Override
        void handleClickOff() {
            if (miliseconds < MINIMUM_MILLISECONDS_FOR_INHALE) {
                currentState.handleThreeSecsLess();
            }
            else{
                setState(outState);
            }
        }

        @Override
        void handleThreeSecsLess() {
            Log.d("inState", "Is 3 seconds or less condition triggered");
            music.pause();
            if(countDownTimer != null){
                countDownTimer.cancel();
            }
            resetButton();
            miliseconds = 0;
            setState(preBreathState);
        }
    }

    // ************************************************************
    // Breath out state
    // ************************************************************
    private class outState extends State {

        public outState(breathActivity context) {
            super(context);
        }
        @Override
        void handleEnter() {
            breathOutAnimation();
        }

        @Override
        void handleClickOff() {

            Log.i("outState", "Exiting outState");
            if(breathNum <= 0){
                setState(preBreathState);
            }
            else{
                setState(inState);
            }
        }

        /**Animation for breath out, used in outState**/
        private void breathOutAnimation() {
            //starts inward animation, start breathing sound
            breathButton.setText("Out");
            breathButton.setOnTouchListener(null);
            music.start();

            long interval = 100;

            Toast.makeText(getApplicationContext(), "and exhale.", Toast.LENGTH_LONG).show();

            countDownTimer = new CountDownTimer(miliseconds, interval) {
                @Override
                public void onTick(long l) {
                    Log.d("BreathOutAnimation", "triggered");
                    ViewGroup.LayoutParams params = breathButton.getLayoutParams();

                    params.width /= 1.01;
                    params.height /= 1.01;

                    // Since width and height are stored in int, there will be errors when dividing with a float.
                    // In order to shrink the button to the exact same size, we will manually set the parameters
                    // when the division result is close to 450
                    if (params.width <= 450) {
                        params.width = 450;
                        params.height = 450;
                    }
                    breathButton.setLayoutParams(params);
                }

                @Override
                public void onFinish() {
                    music.pause();
                    breathButton.setText("In");
                    resetButton();

                    --breathNum;
                    if(breathNum == 0){
                        outOfBreath = true;
                    }

                    updateTextView();
                    miliseconds = 0;

                    handleClickOff();

                }
            }.start();
        }
    }

    // ************************************************************
    // Pre Breath State
    // ************************************************************
    public class preBreathState extends State {
        public preBreathState(breathActivity context) {
            super(context);
        }

        @Override
        void handleEnter() {
            Log.d("preBreathState", "In!");

            addBreath.setVisibility(View.VISIBLE);
            decreaseBreath.setVisibility(View.VISIBLE);
            Toast.makeText(breathActivity.this, "PreSetState!", Toast.LENGTH_LONG).show();

            configureTextView();
            configureButton();

            if(outOfBreath){
                breathButton.setText("Good job!");
            }
            else{
                breathButton.setText("Begin");
            }
        }
        @Override
        void handleClickOff() {
            Log.d("preBreathState/handleClickOff", "In!");
            setState(inState);
        }

        private void configureButton() {
            breathButton = findViewById(R.id.breathButton);
            breathButton.setOnClickListener(View -> {
                currentState.handleClickOff();
            });
        }
    }

    private void resetButton() {
        ViewGroup.LayoutParams initialParams = breathButton.getLayoutParams();
        initialParams.width = 450;
        initialParams.height = 450;
        breathButton.setLayoutParams(initialParams);
    }
    // Use "Null Object" pattern: This class, does nothing! It's like a safe null
    private class IdleState extends State {
        public IdleState(breathActivity context) {
            super(context);
        }
    }
}
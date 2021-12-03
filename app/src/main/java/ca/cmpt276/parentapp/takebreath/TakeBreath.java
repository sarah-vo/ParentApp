package ca.cmpt276.parentapp.takebreath;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.ChildManager;


// https://www.pngaaa.com/detail/1843470
// https://pixabay.com/music/solo-piano-piano-moment-9835/
public class TakeBreath extends AppCompatActivity {
    int breathCount = 0;
    int breathNum;
    CountDownTimer countDownTimer;
    int seconds = 0;
    final int MINIMUM_MILLISECONDS_FOR_INHALE = 3000;
    MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);

        music = MediaPlayer.create(this, R.raw.piano_moment);
        getBreathNumFromSharedPreferences();
        setupHeading();
        inhaleStateButton();
    }

    private void inhaleStateButton() {
        Button breathButton = findViewById(R.id.breathButton);

        breathButton.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        Toast.makeText(getApplicationContext(), "Now inhale ...", Toast.LENGTH_LONG).show();
                        music.start();
                        if (breathCount == 0) {
                            breathButton.setText("In");
                        }
                        countDownTimer = new CountDownTimer(10000, 100) {
                            @Override
                            public void onTick(long l) {
                                ViewGroup.LayoutParams params = breathButton.getLayoutParams();
                                params.width *= 1.01;
                                params.height *= 1.01;
                                breathButton.setLayoutParams(params);
                                seconds += 100;
                            }

                            @Override
                            public void onFinish() {
                                breathButton.setText("Out");
                                music.pause();
                                //For testing
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        exhaleStateButton();
//                                    }
//                                }, 2000);
                                //Testing code end
                            }
                        }.start();
                        break;
                    }

                    case MotionEvent.ACTION_UP: {
                        music.pause();
                        countDownTimer.cancel();
                        if (seconds < MINIMUM_MILLISECONDS_FOR_INHALE) {
                            resetButton();
                            seconds = 0;
                        }
//                        For testing
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                exhaleStateButton();
                            }
                        }, 2000);
//                        Testing code end
                        break;
                    }
                }
                return false;
            }
        });
    }

    private void exhaleStateButton() {
        Button breathButton = findViewById(R.id.breathButton);
        breathButton.setText("Out");
        breathButton.setOnTouchListener(null);
        music.start();
        Toast.makeText(getApplicationContext(), "and exhale.", Toast.LENGTH_LONG).show();
        countDownTimer = new CountDownTimer(seconds, 100) {
            @Override
            public void onTick(long l) {
                ViewGroup.LayoutParams params = breathButton.getLayoutParams();
                params.width /= 1.01;
                params.height /= 1.01;
                // Since width and height are stored in int, there will be errors when dividing with a float.
                // In order to shrink the button to the exact same size, we will manually set the parameters
                // when the division result is close to 450
                if (params.width > 450) {
                    breathButton.setLayoutParams(params);
                }
            }

            @Override
            public void onFinish() {
                music.pause();
                resetButton();
                breathCount++;
                seconds = 0;
            }
        }.start();

        breathButton.setOnClickListener(view -> {
            music.pause();
            countDownTimer.cancel();
            breathCount++;
            seconds = 0;
        });
    }

    private void resetButton() {
        Button breathButton = findViewById(R.id.breathButton);
        ViewGroup.LayoutParams initialParams = breathButton.getLayoutParams();
        initialParams.width = 450;
        initialParams.height = 450;
        breathButton.setLayoutParams(initialParams);
    }

    private void setupHeading() {
        TextView heading = findViewById(R.id.tvHeading);
        heading.setText("Let's take " + breathNum + " breaths together");

        Button addBreath = findViewById(R.id.btnAddBreath);
        addBreath.setOnClickListener( view -> {
            if (breathNum < 10) {
                breathNum++;
                heading.setText("Let's take " + breathNum + " breaths together");
            }
            else {
                Toast.makeText(this, "Please select between 1 and 10 breaths", Toast.LENGTH_SHORT).show();
            }
        });
        Button decreaseBreath = findViewById(R.id.btnRemoveBreath);
        decreaseBreath.setOnClickListener( view -> {
            if (breathNum > 0) {
                breathNum--;
                heading.setText("Let's take " + breathNum + " breaths together");
            }
            else {
                Toast.makeText(this, "Please select between 1 and 10 breaths", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeBreathNumToSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Shared Preference", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("BreathNum", breathNum);
        editor.commit();
    }

    private void getBreathNumFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Shared Preference", MODE_PRIVATE);
        breathNum = sharedPreferences.getInt("BreathNum", 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        storeBreathNumToSharedPreferences();
    }
}
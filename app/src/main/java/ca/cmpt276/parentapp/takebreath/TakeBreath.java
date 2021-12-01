package ca.cmpt276.parentapp.takebreath;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import ca.cmpt276.parentapp.R;


// https://www.pngaaa.com/detail/1843470
public class TakeBreath extends AppCompatActivity {
    int breathCount = 0;
    int breathNum;
    CountDownTimer countDownTimer;
    int seconds = 0;
    final int MINIMUM_MILLISECONDS_FOR_INHALE = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);

        setupHeading();
        inhaleStateButton();

    }

    private void inhaleStateButton() {
        Button breathButton = findViewById(R.id.breathButton);

        breathButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
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
                        countDownTimer.cancel();
                        if (seconds < MINIMUM_MILLISECONDS_FOR_INHALE) {
                            resetButton();
                            seconds = 0;
                        }
                        //For testing
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                exhaleStateButton();
                            }
                        }, 2000);
                        //Testing code end
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
                resetButton();
                breathCount++;
                seconds = 0;
            }
        }.start();

        breathButton.setOnClickListener(view -> {
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
        EditText breathNumInput = findViewById(R.id.etBreathNum);
        breathNumInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = breathNumInput.getText().toString();
                if (!"".equals(temp)) {
                    breathNum = Integer.parseInt(temp);
                    Toast.makeText(getApplicationContext(), "Let's take " + breathNum + " breaths together...", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

}
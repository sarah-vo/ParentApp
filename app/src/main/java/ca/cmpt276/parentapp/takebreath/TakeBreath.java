package ca.cmpt276.parentapp.takebreath;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.parentapp.R;


// https://www.pngaaa.com/detail/1843470
public class TakeBreath extends AppCompatActivity {
    int breathCount = 0;
    int breathNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);

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

        Button breathButton = findViewById(R.id.breathButton);
        breathButton.setOnClickListener(view -> {
            Button button = (Button) view;
            String text = button.getText().toString();
            Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();

            if (breathCount == 0) {
                breathButton.setText("In");
                new CountDownTimer(10000, 100) {
                    @Override
                    public void onTick(long l) {
                        ViewGroup.LayoutParams params = breathButton.getLayoutParams();
                        params.width *= 1.01;
                        params.height *= 1.01;
                        breathButton.setLayoutParams(params);
                        breathCount++;
                    }

                    @Override
                    public void onFinish() {
                        breathButton.setText("Out");
                    }
                }.start();
            }
            else if (breathCount > 10) {
                new CountDownTimer(10000, 100) {
                    @Override
                    public void onTick(long l) {
                        ViewGroup.LayoutParams params = breathButton.getLayoutParams();
                        params.width *= 0.99;
                        params.height *= 0.99;
                        breathButton.setLayoutParams(params);
                    }

                    @Override
                    public void onFinish() {
                        breathButton.setText("In");
                        breathCount = 0;
                    }
                }.start();
            }
        });

    }

}
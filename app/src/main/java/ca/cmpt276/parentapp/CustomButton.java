package ca.cmpt276.parentapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButton extends androidx.appcompat.widget.AppCompatButton {
    private int value = 0;

    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setValue(int value){
        this.value = value;
    }
}

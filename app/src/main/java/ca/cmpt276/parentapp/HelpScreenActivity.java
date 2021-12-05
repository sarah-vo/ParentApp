package ca.cmpt276.parentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * State the name of the team and developers which created the app. Show links to material used in the app
 */
public class HelpScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen);

        setTitle("Help Screen");
        setupLink();


    }

    private void setupLink() {
        TextView tv9 = findViewById(R.id.textView9);
        tv9.setMovementMethod(LinkMovementMethod.getInstance());
        TextView tv10 = findViewById(R.id.textView10);
        tv10.setMovementMethod(LinkMovementMethod.getInstance());
        TextView tv11 = findViewById(R.id.textView11);
        tv11.setMovementMethod(LinkMovementMethod.getInstance());
        TextView tv12 = findViewById(R.id.textView12);
        tv12.setMovementMethod(LinkMovementMethod.getInstance());
        TextView tv13 = findViewById(R.id.textView13);
        tv13.setMovementMethod(LinkMovementMethod.getInstance());
        TextView tv14 = findViewById(R.id.textView14);
        tv14.setMovementMethod(LinkMovementMethod.getInstance());
        TextView tv15 = findViewById(R.id.textView15);
        tv15.setMovementMethod(LinkMovementMethod.getInstance());
        TextView tv16 = findViewById(R.id.textView16);
        tv16.setMovementMethod(LinkMovementMethod.getInstance());
        TextView tv17 = findViewById(R.id.textView17);
        tv17.setMovementMethod(LinkMovementMethod.getInstance());
        TextView tv18 = findViewById(R.id.textView18);
        tv18.setMovementMethod(LinkMovementMethod.getInstance());
        TextView breathBtnLink = findViewById(R.id.breathBtnLink);
        breathBtnLink.setMovementMethod(LinkMovementMethod.getInstance());
        TextView breathMusicLink = findViewById(R.id.breathMusicLink);
        breathMusicLink.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
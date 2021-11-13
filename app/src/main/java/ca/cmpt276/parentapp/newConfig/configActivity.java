package ca.cmpt276.parentapp.newConfig;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.childManager;

public class configActivity extends AppCompatActivity {
    childManager manager = childManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        buildChildList();
        buildFloatingButton();
    }



    private void buildChildList() {
        ArrayAdapter<Child> adapter = new listViewAdapter(this, manager.getChildList());
        ListView list = findViewById(R.id.childListView);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, viewClicked, position, id) -> {
            Intent intent = new Intent(this, modifyDeleteChildren.class);
            startActivity(intent);
        });
    }

    private void buildFloatingButton() {
        FloatingActionButton button = findViewById(R.id.configFloatingButton);
        button.setOnClickListener(View ->{
            Intent intent = new Intent(this, addChildren.class);
            startActivity(intent);
        });
    }
}
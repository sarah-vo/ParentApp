package ca.cmpt276.parentapp.whoseturn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.flipcoin.FlipCoinActivity;

public class WhoseTurnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whose_turn);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent i = new Intent(this, AddTask.class);
            startActivity(i);
        });

        displayTask();
        registerClickCallback();
    }

    private void displayTask() {
        String[] tasks = {"First bath" + "\nName", "Put pop can into can cooler" + "\nName"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.task_list,
                tasks);

        ListView list = (ListView) findViewById(R.id.listViewTask);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.listViewTask);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view;

            }
        });
    }
}
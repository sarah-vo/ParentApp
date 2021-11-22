package ca.cmpt276.parentapp.whoseturn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Task;
import ca.cmpt276.parentapp.model.TaskManager;

public class WhoseTurnActivity extends AppCompatActivity {
    TaskManager taskManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whose_turn);

        FloatingActionButton mAddTaskButton = findViewById(R.id.add_task_button);
        mAddTaskButton.setOnClickListener(view -> {
            Intent i = new Intent(this, AddTaskActivity.class);
            startActivity(i);
        });

        setTitle("Whose Turn");
        getTaskListFromSharedPreferences();
        displayTask();
        registerClickCallback();
    }

    private void displayTask() {

        // For testing
        Task t1 = new Task("Pick a tv channel to watch");
        Task t2 = new Task("Wash the dished");
        taskManager.addTask(t1);
        taskManager.addTask(t2);
        // For testing end

        ArrayList<Task> tasks = taskManager.getTaskList();

        ArrayAdapter<Task> adapter = new ArrayAdapter<>(
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
                Intent intent = new Intent(WhoseTurnActivity.this, EditTask.class);
                intent.putExtra("task index", i);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //displayTask();

        ArrayList<Task> tasks = taskManager.getTaskList();

        ArrayAdapter<Task> adapter = new ArrayAdapter<>(
                this,
                R.layout.task_list,
                tasks);

        ListView list = (ListView) findViewById(R.id.listViewTask);
        list.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        storeTaskListToSharedPreferences();
    }

    private void storeTaskListToSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Shared Preference", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(taskManager);
        editor.putString("Task List",json);
        editor.apply();
    }

    private void getTaskListFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Shared Preference", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("Task List",null);
        taskManager = gson.fromJson(json, TaskManager.class);
        taskManager.setInstance(taskManager);

        if(taskManager == null){
            taskManager = TaskManager.getInstance();
        }
    }
}
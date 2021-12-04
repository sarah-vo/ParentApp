package ca.cmpt276.parentapp.whoseturn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Task;
import ca.cmpt276.parentapp.model.TaskManager;

/**
 * Display the list of tasks onto the screen along with next turn's child name. The user can add
 * tasks from the floating action button and edit tasks by clicking on them (this include conforming
 * the child has complete the task).
 */
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
        ArrayList<Task> tasks = taskManager.getTaskList();

        ArrayAdapter<Task> adapter = new ArrayAdapter<>(
                this,
                R.layout.task_list_item,
                tasks);

        ListView list = findViewById(R.id.listViewTask);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView list = findViewById(R.id.listViewTask);
        list.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(WhoseTurnActivity.this, EditTaskActivity.class);
            intent.putExtra("task index", i);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //displayTask();

        ArrayList<Task> tasks = taskManager.getTaskList();

        ArrayAdapter<Task> adapter = new ArrayAdapter<>(
                this,
                R.layout.task_list_item,
                tasks);

        ListView list = findViewById(R.id.listViewTask);
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
        TaskManager.setInstance(taskManager);

        if(taskManager == null){
            taskManager = TaskManager.getInstance();
        }
    }
}
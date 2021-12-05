package ca.cmpt276.parentapp.whoseturn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Task;
import ca.cmpt276.parentapp.model.TaskManager;

/**
 * Display the task completion history with information including
 * child portrait, child name, last turn date.
 */

public class TaskHistory extends AppCompatActivity {

    RecyclerView taskHistoryRecyclerView;
    TaskManager taskManager = TaskManager.getInstance();
    Task task;

    int taskIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        taskIndex = intent.getIntExtra(EditTaskActivity.TASK_INDEX, -1);
        task = taskManager.getTask(taskIndex);

        setTitle(task.getTaskName());

        if (task.getTaskHistoryList() == null || task.getTaskHistoryList().size() == 0) {
            setContentView(R.layout.task_history_empty);
        }
        else {
            setContentView(R.layout.activity_task_history);

            taskHistoryRecyclerView = findViewById(R.id.task_history_recycler_view);

            TaskHistoryAdapter taskHistoryAdapter = new TaskHistoryAdapter(this,
                    task.getTaskHistoryList());

            taskHistoryRecyclerView.setAdapter(taskHistoryAdapter);
            taskHistoryRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                    StaggeredGridLayoutManager.VERTICAL));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        task = taskManager.getTask(taskIndex);
        if (task.getTaskHistoryList() == null || task.getTaskHistoryList().size() == 0) {
            setContentView(R.layout.task_history_empty);
        }
        else {
            setContentView(R.layout.activity_task_history);

            taskHistoryRecyclerView = findViewById(R.id.task_history_recycler_view);

            TaskHistoryAdapter taskHistoryAdapter = new TaskHistoryAdapter(this,
                    task.getTaskHistoryList());

            taskHistoryRecyclerView.setAdapter(taskHistoryAdapter);
            taskHistoryRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                    StaggeredGridLayoutManager.VERTICAL));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}